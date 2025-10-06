import java.io.*;
import java.net.*;
public class UploadClient {
    public UploadClient() { }
    public String uploadFile() {
        String listing = "";
        try {
            Socket socket = new Socket("localhost", 8082);
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

            String boundary = "MyBoundary";
            OutputStream out = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(out, true);
            String header = "POST /upload HTTP/1.1\r\n" +
                          "Host: localhost\r\n" +
                          "User-Agent: ConsoleUploader\r\n" +
                          "Accept: */*\r\n" +
                          "Connection: close\r\n" +
                          "Content-Type: multipart/form-data; boundary=" + boundary + "\r\n" +
                          "\r\n";

            pw.println(header);
            pw.println();
            pw.flush();

            out.write(("--" + boundary +"\r\n").getBytes());
            out.write("Content-Disposition: form-data; name=\"FileName\"; filename=\"AndroidLogo.png\"\r\n".getBytes());
            out.write("Content-Type: image/png\r\n\r\n".getBytes());

            FileInputStream fis = new FileInputStream("AndroidLogo.png");
            byte[] bytes = fis.readAllBytes();
            out.write(bytes);
            fis.close();

            out.write(("\r\n--" + boundary + "--\r\n").getBytes());
            out.flush();
            socket.shutdownOutput();

            System.out.println("Came this far\n");
            String filename = "";
            while ((filename = in.readLine()) != null) {
                listing += filename + "\n";
            }
            socket.shutdownInput();
        } catch (Exception e) {
            System.err.println(e);
        }
        return listing;
    }
}
