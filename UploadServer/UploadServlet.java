import java.io.*;
import java.time.Clock;
import java.util.concurrent.Semaphore;
import java.nio.charset.StandardCharsets;

public class UploadServlet extends HttpServlet {


   @Override
   protected void doGet(HttpServletRequest request, HttpServletResponse response){
        PrintWriter out = new PrintWriter(response.getOutputStream(),true);
        out.println(
             "HTTP/1.1 200 OK\r\n"+
             "Content-Type: text/html; charset=UTF-8\r\n"+
             "\r\n"+ 
             "<!DOCTYPE html>" + 
             "<head>" + 
             "<title>File Upload Form</title>" +
             "</head>" +
             "<body>" +
             "<h1>Upload file</h1>" +
             "<form method=\"POST\" action=\"upload\"" +
             "enctype=\"multipart/form-data\">" +
             "<input type=\"file\" name=\"FileName\"/>" +
             "Caption: <input type=\"text\" name=\"Question\"/>" +
             "Date: <input type=\"date\" name=\"UploadDate\"/>" +
             "<input type=\"submit\" value=\"Submit\"/>" +
             "</form>" +
             "</body>" +
             "</html>"
            );
             
   }

  @Override
protected void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      
      InputStream in = request.getInputStream();

      File uploadDir = new File("uploads");
      if(!uploadDir.exists())uploadDir.mkdir();



      byte[] buffer = new byte[4096];
      int bytesRead;
      boolean isHeaderEnded = false;
      ByteArrayOutputStream headerBuffer = new ByteArrayOutputStream();
      ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();

      while((bytesRead = in.read(buffer)) != -1){
        if(!isHeaderEnded){
          headerBuffer.write(buffer,0,bytesRead);
          String content = headerBuffer.toString();

          int headerEnd = content.indexOf("\r\n\r\n");
          if(headerEnd != -1){
            isHeaderEnded = true;
            byte[] allBytes = headerBuffer.toByteArray();
            bodyBuffer.write(allBytes, headerEnd + 4, allBytes.length - headerEnd -4);
          }
        }else{
          bodyBuffer.write(buffer,0,bytesRead);
        }
      }

      //take name caption and date from body 
      String headerStr = headerBuffer.toString("ISO-8859-1");
      String body = bodyBuffer.toString("ISO-8859-1");

      String boundary = null;
      for (String line : headerStr.split("\r\n")) {
          if (line.startsWith("Content-Type: multipart/form-data")) {
              int idx = line.indexOf("boundary=");
              if (idx != -1) {
                  boundary = "--" + line.substring(idx + 9);
                  break;
              }
          }
      }

       if (boundary == null) {
            throw new InvalidUploadException("Missing multipart boundary in request header.");
        }
       
      System.out.println("Boundary = " + boundary);
      String[] parts = body.split(boundary);

      String filename=null;

      for (String part : parts) {
            part = part.trim();
            if (part.isEmpty() || !part.contains("Content-Disposition")) continue;
        
            int dataStart = part.indexOf("\r\n\r\n");
            if (dataStart == -1) continue;
        
            String headers = part.substring(0, dataStart);
            String content = part.substring(dataStart + 4).trim();
        
            if (headers.contains("filename=\"")) {
                filename = headers.split("filename=\"")[1].split("\"")[0];
                int fileEnd = content.lastIndexOf(boundary)-4;
                if(fileEnd < 0) fileEnd = content.length();
                byte[] fileBytes = content.substring(0, fileEnd)
                                          .getBytes(StandardCharsets.ISO_8859_1);
        
                try (FileOutputStream fout = new FileOutputStream("uploads/" + filename)) {
                    fout.write(fileBytes);
                }
                System.out.println("File saved: " + filename);
            } else {
                String name = headers.split("name=\"")[1].split("\"")[0];
                System.out.println(name + " = " + content);
            }
        }


      File dir = new File("./uploads");
      String[] chld = dir.list();
      PrintWriter out = new PrintWriter(response.getOutputStream(), true);
      out.println("HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=UTF-8\r\n\r\n");
      out.println("<html><body><h2>Upload completed!</h2>");
      out.println("<p>File saved as: " + filename + "</p></body></html>");
      out.println("<p>File list in the same path: </p>");
      for(int i = 0; i < chld.length;i++){
        String fileName = chld[i];
        out.println("<p>" + fileName+ "</p>");
      }
      out.flush();

    } catch (Exception ex) {
        System.err.println("❌ Error: " + ex.getMessage());
        ex.printStackTrace();
    } 
}
   
}
