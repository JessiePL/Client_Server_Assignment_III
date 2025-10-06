Assignment 3 — Upload Server and Client

Author: Lin
Course: COMP 3940 – Client-Server Systems
Assignment: III – File Upload Server and Native Client

------------------------------------------------------------
Project Overview
------------------------------------------------------------
This project implements a multithreaded HTTP Upload Server and a Java Console Client
that can upload files to it using a multipart/form-data POST request.

The server can:
- Handle both GET and POST HTTP requests.
- Serve an HTML file upload form when accessed via browser (GET /).
- Parse multipart POST requests from browser or console client.
- Save uploaded files (binary or text) in a local folder (/uploads).
- Return an HTML response listing all uploaded files.
- Manage concurrent uploads using a thread pool (behavioral pattern).

------------------------------------------------------------
Architecture
------------------------------------------------------------
Component Descriptions:
- UploadServer.java: Creates a ServerSocket on port 8082 and manages client connections using a fixed thread pool.
- UploadServerThread.java: Handles each client connection, identifies HTTP method (GET/POST), and delegates to the servlet.
- UploadServlet.java: Extends HttpServlet. Implements doGet() and doPost() for handling file uploads and returning HTML.
- HttpServlet / HttpServletRequest / HttpServletResponse: Simplified servlet abstraction.
- UploadClient.java: Console app that builds and sends a multipart POST request to the server.
- Activity.java: Launches the console upload client.
- uploads/: Output directory automatically created to store uploaded files.

------------------------------------------------------------
Design Patterns Used
------------------------------------------------------------
- Thread Pool Pattern (Behavioral): Used in UploadServer to manage concurrent upload requests.
- Custom Exception Handling: Robust error handling during I/O operations.
- Servlet-based Component Architecture: UploadServlet as a reusable component.

------------------------------------------------------------
How to Compile and Run
------------------------------------------------------------
1. Compile all .java files
   javac *.java

2. Start the Upload Server
   java UploadServer

   Expected output:
   UploadServer started on port 8082...

3. Test via Browser
   Open your browser and visit:
   http://localhost:8082/

   You should see an HTML upload form. Select a file, enter caption/date, and submit.

4. Test via Console Client
   In another terminal:
   java Activity

   This launches the console client which uploads AndroidLogo.png to the server.

------------------------------------------------------------
Expected Output Example
------------------------------------------------------------
Server console:
  UploadServer started on port 8082...
  New connection accepted: Socket[addr=/127.0.0.1,port=...]
  File saved: AndroidLogo.png

Browser page after upload:
  Upload completed!
  File saved as: AndroidLogo.png
  File list in the same path:
    - AndroidLogo.png

------------------------------------------------------------
File Structure
------------------------------------------------------------
.
├── Activity.java
├── UploadClient.java
├── UploadServer.java
├── UploadServerThread.java
├── UploadServlet.java
├── HttpServlet.java
├── HttpServletRequest.java
├── HttpServletResponse.java
├── uploads/              (auto-created folder)
└── AndroidLogo.png       (sample file for console upload)

------------------------------------------------------------
Notes
------------------------------------------------------------
- The server handles both binary and text files.
- Port 8082 must be available; change in UploadServer.java if needed.
- Ensure AndroidLogo.png exists in the same folder when testing UploadClient.java.
- Adjust thread pool size by editing:
  private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);
