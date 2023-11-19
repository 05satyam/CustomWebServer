package com.example.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ClientHandler implements Runnable {

    public ClientHandler() {
    }

    @Override
    public void run() {
        ServerSocket serverSocket=null;
        try {
         serverSocket = new ServerSocket(8080); // Listening on port 80
        System.out.println("Listening for connection on port 8080 ....");
        while (!serverSocket.isClosed()) {
                Socket clientSocket = serverSocket.accept();
                InputStreamReader isr = new InputStreamReader(clientSocket.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
            System.out.println("Thread started: " + Thread.currentThread().getId());
            Thread.sleep(5000); // Sleep for 1 second

            String line = reader.readLine();
                String[] requestParts = line.split(" ");
                String fileName;
                int statusCode = 200;
                if (!(requestParts[1].equalsIgnoreCase("/") || requestParts[1].equalsIgnoreCase("/index.html"))) {
                    fileName = "src/com/example/web/BadRequest.html";
                    statusCode = 400;
                } else {
                    fileName = "src/com/example/web/index.html";
                }

                File f = new File(fileName);
                FileInputStream fis = new FileInputStream(f);

                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                OutputStream clientOutput = clientSocket.getOutputStream();

                PrintWriter writer = new PrintWriter(clientOutput, true);
                writer.print("HTTP/1.1 " + statusCode + " OK\r\n");
                writer.print("Content-Type: text/html; charset=utf-8\r\n");
                writer.print("Content-Length: " + f.length() + "\r\n");
                writer.print("\r\n");

                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    writer.println(responseLine);
                }
                writer.flush();
            }
            System.out.println("Thread finished: " + Thread.currentThread().getName());



        }catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            }
            catch (IOException e) {

                e.printStackTrace();
            }
        }
    }
}
