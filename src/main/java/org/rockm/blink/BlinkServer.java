package org.rockm.blink;

import org.rockm.blink.httpserver.JavaHttpServer;
import org.rockm.blink.httpserver.MultiMethodHttpHandler;

import java.io.IOException;

public class BlinkServer {

    private final Server server;
    private final MultiMethodHttpHandler httpHandler = new MultiMethodHttpHandler();

    public BlinkServer(int port) {
        this.server = new JavaHttpServer(port, httpHandler);
    }

    public void get(String path, RequestHandler rh) throws IOException {
        registerRoute(path, Method.GET, rh);
    }

    private void registerRoute(String path, Method method, RequestHandler rh) throws IOException {
        server.startIfNeeded();
        httpHandler.addHandler(path, method, rh);
    }

    public void post(String path, RequestHandler rh) throws IOException {
        registerRoute(path, Method.POST, rh);
    }

    public void delete(String path, RequestHandler rh) throws IOException {
        registerRoute(path, Method.DELETE, rh);
    }

    public void put(String path, RequestHandler rh) throws IOException {
        registerRoute(path, Method.PUT, rh);
    }

    public void stop() {
        server.stop();
    }

    public void reset() {
        httpHandler.reset();
    }

    public void contentType(String type) {
        httpHandler.contentType(type);
    }
}
