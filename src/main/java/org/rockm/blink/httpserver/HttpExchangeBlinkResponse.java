package org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpExchangeBlinkResponse implements BlinkResponse {
    public static final String CONTENT_TYPE = "Content-Type";

    private Object body = "";
    private int status = 200;
    private final Map<String, String> headers = new HashMap<>();

    public void apply(HttpExchange httpExchange) throws IOException {
        setResponseHeaders(httpExchange);
        byte[] bodyAsBytes = body.toString().getBytes();
        httpExchange.sendResponseHeaders(status, bodyAsBytes.length);
        setResponseBody(httpExchange, bodyAsBytes);
    }

    private void setResponseBody(HttpExchange httpExchange, byte[] bytes) throws IOException {
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(bytes);
        responseBody.close();
    }

    private void setResponseHeaders(HttpExchange httpExchange) throws IOException {
        Headers httpHeaders = httpExchange.getResponseHeaders();
        headers.entrySet().forEach((e) -> {
            httpHeaders.put(e.getKey(), Collections.singletonList(e.getValue()));
        });
    }

    public void setBody(final Object body) {
        this.body = (body == null ? "" : body);
    }

    public void status(int statusCode) {
        this.status = statusCode;
    }

    public void header(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public void type(String contentType) {
        headers.put(CONTENT_TYPE, contentType);
    }
}
