package org.rockm.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BlinkServer {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 2;

    private final HttpServer httpServer;
    private final Map<String, MultiMethodHttpHandler> pathAndMethodToHandler = new HashMap<>();
    private volatile boolean running = false;

    public BlinkServer(int port) throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void get(String path, RequestHandler rh) {
        startIfNeeded();
        getAndCreateHandler(path).setGetHandler(rh);
    }

    private MultiMethodHttpHandler getAndCreateHandler(String path) {
        MultiMethodHttpHandler httpHandler = pathAndMethodToHandler.get(path);
        if (httpHandler == null) {
            httpHandler = createNewHandlerFor(path);
        }
        return httpHandler;
    }

    private MultiMethodHttpHandler createNewHandlerFor(String path) {
        MultiMethodHttpHandler httpHandler = new MultiMethodHttpHandler();
        pathAndMethodToHandler.put(path, httpHandler);
        httpServer.createContext(path, httpHandler);
        return httpHandler;
    }

    private void startIfNeeded() {
        if(!running) {
            running = true;
            httpServer.start();
        }
    }

    public void post(String path, RequestHandler rh) {
        startIfNeeded();
        getAndCreateHandler(path).setPostHandler(rh);
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }

    private class MultiMethodHttpHandler implements HttpHandler {

        private Map<String, RequestHandler> methodToRequestHandler = new HashMap<>();

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            handleRequestWith(httpExchange, methodToRequestHandler.get(httpExchange.getRequestMethod()));
        }

        private void handleRequestWith(HttpExchange httpExchange, RequestHandler handler) throws IOException {
            HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
            response.setBody((String) handler.handleRequest(new HttpExchangeBlinkRequest(httpExchange), response));
            response.apply(httpExchange);
        }

        public void setGetHandler(RequestHandler handler) {
            methodToRequestHandler.put("GET", handler);
        }

        public void setPostHandler(RequestHandler handler) {
            methodToRequestHandler.put("POST", handler);
        }
    }
}
