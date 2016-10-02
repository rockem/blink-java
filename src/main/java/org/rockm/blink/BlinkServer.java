package org.rockm.blink;

import org.rockm.blink.httpserver.JavaHttpServer;

import java.io.IOException;

public class BlinkServer {

    private final Server server;
    private final RoutesContainer routesContainer = new RoutesContainer();

    public BlinkServer(int port) {
        this.server = new JavaHttpServer(port, routesContainer);
    }

    public void get(String path, RequestHandler rh) throws IOException {
        registerRoute(path, Method.GET, rh);
    }

    private void registerRoute(String path, Method method, RequestHandler rh) throws IOException {
        server.startIfNeeded();
        routesContainer.addRoute(new Route(method, path, rh));
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
        routesContainer.clear();
        server.setDefaultContentType(null);
    }

    public void contentType(String type) {
        server.setDefaultContentType(type);
    }
}
