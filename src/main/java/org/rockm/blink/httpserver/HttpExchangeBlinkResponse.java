package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkResponse;

import java.io.IOException;
import java.io.OutputStream;

public class HttpExchangeBlinkResponse implements BlinkResponse {
    private String body;

    public void apply(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(200, body.length());
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(body.getBytes());
        responseBody.close();
    }

    public void setBody(String body) {
        this.body = body;
    }
}
