package org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.io.MessageConverter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class HttpExchangeBlinkResponse implements BlinkResponse {
    public static final String CONTENT_TYPE = "Content-Type";
    public static final int DEFAULT_STATUS = 200;

    private Object body = "";
    private int status = DEFAULT_STATUS;
    private final String defaultContentType;
    private final Map<String, String> headers = new HashMap<>();

    public HttpExchangeBlinkResponse(String defaultContentType) {
        this.defaultContentType = defaultContentType;
    }

    public void apply(HttpExchange httpExchange) throws IOException {
        setResponseHeaders(httpExchange);
        byte[] bodyAsBytes = getBodyInBytes();
        httpExchange.sendResponseHeaders(status, bodyAsBytes.length);
        setResponseBody(httpExchange, bodyAsBytes);
    }

    private void setResponseHeaders(HttpExchange httpExchange) throws IOException {
        Headers httpHeaders = httpExchange.getResponseHeaders();
        setDefaultContentType(httpHeaders);
        headers.entrySet().forEach((e) ->
                httpHeaders.put(e.getKey(), Collections.singletonList(e.getValue())));
    }

    private void setDefaultContentType(Headers httpHeaders) {
        if(defaultContentType != null) {
            httpHeaders.put(CONTENT_TYPE, Collections.singletonList(defaultContentType));
        }
    }

    private byte[] getBodyInBytes() {
        return new MessageConverter(body).convert();
    }

    private void setResponseBody(HttpExchange httpExchange, byte[] bytes) throws IOException {
        OutputStream responseBody = httpExchange.getResponseBody();
        responseBody.write(bytes);
        responseBody.close();
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
