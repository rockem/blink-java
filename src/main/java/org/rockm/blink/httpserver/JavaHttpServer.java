package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.rockm.blink.Server;

import java.io.IOException;
import java.net.InetSocketAddress;

public class JavaHttpServer implements Server {

    private static final int NUM_OF_SECS_WAIT_FOR_STOP = 1;

    private final int port;
    private final HttpHandler httpHandler;

    private HttpServer httpServer;
    private volatile boolean running = false;


    public JavaHttpServer(int port, HttpHandler httpHandler) {
        this.port = port;
        this.httpHandler = httpHandler;
    }

    public void startIfNeeded() throws IOException {
        if (!running) {
            running = true;
            createHttpServerFor();
        }
    }

    private void createHttpServerFor() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);
        httpServer.createContext("/", httpHandler);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(NUM_OF_SECS_WAIT_FOR_STOP);
    }
}
