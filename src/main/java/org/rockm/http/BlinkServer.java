package org.rockm.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class BlinkServer {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 5;

    private final HttpServer httpServer;
    private volatile boolean running = false;

    public BlinkServer(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void get(String path, RequestHandler rh) {
        startIfNeeded();
        httpServer.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if(httpExchange.getRequestMethod().equals("GET")) {
                    HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
                    response.setBody((String) rh.handleRequest(new HttpExchangeBlinkRequest(httpExchange), response));
                    response.apply(httpExchange);
                }
            }
        });

    }

    private void startIfNeeded() {
        if(!running) {
            running = true;
            httpServer.start();
        }
    }

    public void post(String path, RequestHandler rh) {
        startIfNeeded();
        httpServer.createContext(path, new HttpHandler() {
            @Override
            public void handle(HttpExchange httpExchange) throws IOException {
                if(httpExchange.getRequestMethod().equals("POST")) {
                    HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
                    response.setBody((String) rh.handleRequest(new HttpExchangeBlinkRequest(httpExchange), response));
                    response.apply(httpExchange);
                }
            }
        });
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }

}
