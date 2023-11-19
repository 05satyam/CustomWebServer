package com.example.web;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleWebServer {

    public static void main(String[] args) throws IOException {
        new Thread(new ClientHandler()).start();
    }
}
