package org.rockm.blink;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BlinkServer {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 2;

    private HttpServer httpServer;
    private final Map<String, MultiMethodHttpHandler> pathAndMethodToHandler = new HashMap<>();
    private final int port;
    private volatile boolean running = false;
    private MultiMethodHttpHandler httpHandler = new MultiMethodHttpHandler();

    public BlinkServer(int port) throws IOException {
        this.port = port;
    }

    public void get(String path, RequestHandler rh) throws IOException {
        startIfNeeded();
        httpHandler.addHandler(path, "GET", rh);
        //getAndCreateHandler(path).setGetHandler(rh);
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

    private void startIfNeeded() throws IOException {
        if(!running) {
            running = true;
            httpServer = HttpServer.create(new InetSocketAddress(port), 0);
            httpHandler = new MultiMethodHttpHandler();
            httpServer.createContext("/", httpHandler);
            httpServer.start();
        }
    }

    public void post(String path, RequestHandler rh) throws IOException {
        startIfNeeded();
        //getAndCreateHandler(path).setPostHandler(rh);
        httpHandler.addHandler(path, "POST", rh);
    }

    public void delete(String path, RequestHandler rh) throws IOException {
        startIfNeeded();
        httpHandler.addHandler(path, "DELETE", rh);
        //getAndCreateHandler("/kukus").setDeleteHandler(rh);
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }


}
