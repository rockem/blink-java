package org.rockm.blink;

import org.rockm.blink.httpserver.JavaHttpServer;

import java.io.IOException;
import java.util.HashMap;

public class BlinkServer {

    private final Server server;

    public BlinkServer(int port) {
        this.server = new JavaHttpServer(port);
    }

    public void get(String path, RequestHandler rh) throws IOException {
        server.addRoute(new Route(Method.GET, path, rh));
    }

    public void post(String path, RequestHandler rh) throws IOException {
        server.addRoute(new Route(Method.POST, path, rh));
    }

    public void delete(String path, RequestHandler rh) throws IOException {
        server.addRoute(new Route(Method.DELETE, path, rh));
    }

    public void put(String path, RequestHandler rh) throws IOException {
        server.addRoute(new Route(Method.PUT, path, rh));
    }

    public void stop() {
        server.stop();
    }

    public void reset() {
        server.reset();
    }

    public void contentType(String type) {
        server.setDefaultContentType(type);
    }

    public BlinkRequest lastRequest() {
        return server.getLastRequest();
    }
}
