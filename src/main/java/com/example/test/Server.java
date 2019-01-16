package com.example.test;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.*;

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) throws IOException {
//        if (args.length < 1 || args[0].equals("-help") || args[0].equals("--help")) {
//            System.out.println("Usage: java -jar HttpServer.jar $webroot [$port]");
//            return;
//        }
        HttpServer httpServer = HttpServer.create();
        httpServer.createContext("/", new StaticHandler("/home/lee/IdeaProjects/test/src/main/resources", false, false));
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 8000;
        httpServer.bind(new InetSocketAddress("localhost", port), 100);
        httpServer.start();
    }
}
