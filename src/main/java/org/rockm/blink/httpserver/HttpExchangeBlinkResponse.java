package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkResponse;

import java.io.IOException;
import java.io.OutputStream;

public class HttpExchangeBlinkResponse implements BlinkResponse {
    private String body = "";
    private int status = 200;

    public void apply(HttpExchange httpExchange) throws IOException {
        httpExchange.sendResponseHeaders(status, body.length());
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(body.getBytes());
        responseBody.close();
    }

    public void setBody(final String body) {
        this.body = (body == null ? "" : body);
    }

    public void status(int statusCode) {
        this.status = statusCode;
    }
}
