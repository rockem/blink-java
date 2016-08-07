package org.rockm.blink;

import com.sun.net.httpserver.HttpServer;
import org.rockm.blink.httpserver.MultiMethodHttpHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class BlinkServer {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 2;

    private HttpServer httpServer;
    private final int port;
    private volatile boolean running = false;
    private final MultiMethodHttpHandler httpHandler = new MultiMethodHttpHandler();

    public BlinkServer(int port) throws IOException {
        this.port = port;
    }

    public void get(String path, RequestHandler rh) throws IOException {
        registerRoute(path, "GET", rh);
    }

    private void registerRoute(String path, String method, RequestHandler rh) throws IOException {
        startIfNeeded();
        httpHandler.addHandler(path, method, rh);
    }

    private void startIfNeeded() throws IOException {
        if (!running) {
            running = true;
            createHttpServer();
        }
    }

    private void createHttpServer() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", httpHandler);
        httpServer.start();
    }

    public void post(String path, RequestHandler rh) throws IOException {
        registerRoute(path, "POST", rh);
    }

    public void delete(String path, RequestHandler rh) throws IOException {
        registerRoute(path, "DELETE", rh);
    }

    public void put(String path, RequestHandler rh) throws IOException {
        registerRoute(path, "PUT", rh);
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }


}
