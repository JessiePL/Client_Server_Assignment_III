import java.net.*;
import java.io.*;
import java.time.Clock;
import java.util.concurrent.Semaphore; 
public class UploadServerThread extends Thread {

   private Socket socket = null;

   public UploadServerThread(Socket socket) {
      super("DirServerThread");
      this.socket = socket;
   }
   public void run() {
      try {

        System.out.println("Thread " + Thread.currentThread().getName() + " started handling upload...");

         InputStream in = socket.getInputStream();

            byte[] peek = new byte[8];
            int n = in.read(peek); 
            String start = new String(peek, 0, n);
            System.out.println("Request: " + start.trim());

            ByteArrayOutputStream cache = new ByteArrayOutputStream();
            //cache.write(peek, 0, n);

            byte[] buf = new byte[4096];
            int len;
            while (in.available() > 0 && (len = in.read(buf)) != -1) {
                cache.write(buf, 0, len);
            }


            byte[] fullRequest = cache.toByteArray();
            InputStream fullIn = new ByteArrayInputStream(fullRequest);

            HttpServletRequest req = new HttpServletRequest(fullIn);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HttpServletResponse res = new HttpServletResponse(baos);

            HttpServlet servlet = new UploadServlet();

            if (start.startsWith("GET")) {
                servlet.doGet(req, res);
            } else if (start.startsWith("POST")) {
                servlet.doPost(req, res);
            } else {
                System.out.println("Unsupported request: " + start);
            }

            OutputStream out = socket.getOutputStream();
            out.write(baos.toByteArray());
            out.flush();
            socket.close();

            System.out.println("Thread " + Thread.currentThread().getName() + " finished upload.");


        //InputStream in = socket.getInputStream();
        //BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        //String requestLine = reader.readLine();
        //if(requestLine != null & requestLine.startsWith("POST")){
        //  System.out.println("Request: " + requestLine);
        //}

        //if(requestLine == null){
        //    socket.close();
        //    return;
        //}

        //HttpServletRequest req = new HttpServletRequest(socket.getInputStream());
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //HttpServletResponse res = new HttpServletResponse(baos);

        //HttpServlet servlet = new UploadServlet();
        //if(requestLine.startsWith("GET")){
        //    servlet.doGet(req, res);
        //}else if(requestLine.startsWith("POST")){
        //    servlet.doPost(req,res);
        //}else {
        //  System.out.println("Unsupported request: " + requestLine);
        //}

        //OutputStream out = socket.getOutputStream();
        //out.write(baos.toByteArray());
        //out.flush();
        //socket.close();
      } catch (Exception e) { e.printStackTrace(); }
   }
}
