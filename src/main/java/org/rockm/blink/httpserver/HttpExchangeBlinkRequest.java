package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;

public class HttpExchangeBlinkRequest implements BlinkRequest {

    private static final int BUFFER_SIZE = 16384;

    private final HttpExchange httpExchange;
    private final String body;

    public HttpExchangeBlinkRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        body = getAsStringFrom(httpExchange.getRequestBody());
    }

    private String getAsStringFrom(InputStream requestBody) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[BUFFER_SIZE];

        try {
            while ((nRead = requestBody.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*private String getAsStringFrom(InputStream is) {
        byte[] targetArray = new byte[0];
        try {
            targetArray = new byte[is.available()];
            httpExchange.getRequestBody().read(targetArray);
        } catch (IOException e) {
            // Ignore
        }
        return new String(targetArray);
    }*/

    @Override
    public String body() {
        return body;
    }

    @Override
    public String param(String name) {
        return httpExchange.getRequestURI().getQuery().split("=")[1];
    }

    @Override
    public String pathParam(String id) {
        throw new UnsupportedOperationException("Path params are unsupported here ");
    }

    @Override
    public URI uri() {
        return httpExchange.getRequestURI();
    }

    @Override
    public String header(String name) {
        validateHeaderExists(name);
        return httpExchange.getRequestHeaders().get(name).stream()
                .reduce((t, u) -> t + "," + u).get();
    }

    private void validateHeaderExists(String name) {
        if(httpExchange.getRequestHeaders().get(name) == null) {
            throw new HeaderNotFoundException("Header: [" + name + "] does not exist in request");
        }
    }

}
