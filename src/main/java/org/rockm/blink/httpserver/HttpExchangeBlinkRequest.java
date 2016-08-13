package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkRequest;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
