import java.net.*;
import java.io.*;
import java.util.concurrent.*;

public class UploadServer {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws IOException {

            try (ServerSocket serverSocket = new ServerSocket(8082)){
              System.out.println("UploadServer started on port 8082...");

              while(true){
                  Socket clientSocket = serverSocket.accept();
                  System.out.println("New connection accepted: " + clientSocket);

                  threadPool.submit(new UploadServerThread(clientSocket));
              }

            
            } catch (IOException e) {
                System.err.println("Could not listen on port: 8082.");
                System.exit(-1);
            }
        }
}

