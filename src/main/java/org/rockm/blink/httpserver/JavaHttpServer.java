package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.rockm.blink.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JavaHttpServer implements Server {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 1;

    private final int port;
    private final RoutesContainer routesContainer;

    private HttpServer httpServer;
    private volatile boolean running = false;
    private String defaultContentType;
    private BlinkRequest lastRequest;


    public JavaHttpServer(int port, RoutesContainer routesContainer) {
        this.port = port;
        this.routesContainer = routesContainer;
    }

    public void startIfNeeded() throws IOException {
        if (!running) {
            running = true;
            createHttpServerFor();
        }
    }

    private void createHttpServerFor() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", new MultiMethodHttpHandler());
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }

    @Override
    public void setDefaultContentType(String contentType) {
        this.defaultContentType = contentType;
    }

    @Override
    public BlinkRequest getLastRequest() {
        return lastRequest;
    }

    private class MultiMethodHttpHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            Route route = routesContainer.getRouteFor(
                    httpExchange.getRequestMethod(),
                    httpExchange.getRequestURI().getPath());
            HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse(defaultContentType);
            HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);
            response.setBody(new RouteRequestRunner(route).run(request, response));
            lastRequest = request;
            response.apply(httpExchange);
        }

    }
}
