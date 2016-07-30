package org.rockm.http;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public class HttpExchangeBlinkRequest implements BlinkRequest {

    private final HttpExchange httpExchange;

    public HttpExchangeBlinkRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    @Override
    public String body() {
        byte[] targetArray = new byte[0];
        try {
            targetArray = new byte[httpExchange.getRequestBody().available()];
            httpExchange.getRequestBody().read(targetArray);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(targetArray);
    }
}
