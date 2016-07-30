package org.rockm.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BlinkServer {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 2;

    private final HttpServer httpServer;
    private volatile boolean running = false;
    private RequestHandler getHandler;
    private RequestHandler postHandler;
    private Map<String, MultiMethodHttpHandler> pathAndMethodToHandler = new HashMap<>();

    public BlinkServer(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void get(String path, RequestHandler rh) {
        startIfNeeded();
        MultiMethodHttpHandler httpHandler = pathAndMethodToHandler.get(path);
        if (httpHandler == null) {
            httpHandler = new MultiMethodHttpHandler();
            pathAndMethodToHandler.put(path, httpHandler);
            httpServer.createContext(path, httpHandler);
        }
        httpHandler.setGetHandler(rh);
    }

    private void startIfNeeded() {
        if(!running) {
            running = true;
            httpServer.start();
        }
    }

    public void post(String path, RequestHandler rh) {
        startIfNeeded();
        MultiMethodHttpHandler httpHandler = pathAndMethodToHandler.get(path);
        if (httpHandler == null) {
            httpHandler = new MultiMethodHttpHandler();
            pathAndMethodToHandler.put(path, httpHandler);
            httpServer.createContext(path, httpHandler);
        }
        httpHandler.setPostHandler(rh);
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }

    class MultiMethodHttpHandler implements HttpHandler {

        private RequestHandler getHandler;
        private RequestHandler postHandler;

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("GET")) {
                HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
                response.setBody((String) getHandler.handleRequest(new HttpExchangeBlinkRequest(httpExchange), response));
                response.apply(httpExchange);
            }
            if(httpExchange.getRequestMethod().equals("POST")) {
                HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
                response.setBody((String) postHandler.handleRequest(new HttpExchangeBlinkRequest(httpExchange), response));
                response.apply(httpExchange);
            }
        }

        public void setGetHandler(RequestHandler getHandler) {
            this.getHandler = getHandler;
        }

        public void setPostHandler(RequestHandler postHandler) {
            this.postHandler = postHandler;
        }
    }
}
