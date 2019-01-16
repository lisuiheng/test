package com.example.test;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] arg) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8001), 0);
        server.createContext("/text.txt", new TestHandler());
        server.start();
    }

    static class TestHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange t)  {
            Headers h = t.getResponseHeaders();



            String line;
            String resp = "";

            try {
                File newFile = new File("/home/lee/IdeaProjects/test/src/main/resources/test/hello.text");
                System.out.println("*****lecture du fichier*****");
                System.out.println("nom du fichier: " + newFile.getName());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));

                while ((line = bufferedReader.readLine()) != null) {
                    resp += line + "\r\n";
                }
                bufferedReader.close();
                byte[] bs = resp.getBytes();
                t.sendResponseHeaders(200, bs.length);
                OutputStream out = t.getResponseBody();
                out.write(bs);
                out.close();
                t.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}