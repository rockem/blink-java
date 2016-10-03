package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.QueryParamsExtractor;
import org.rockm.blink.io.InputStreamReader;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpExchangeBlinkRequest implements BlinkRequest {

    private static final String COOKIE_HEADER = "Cookie";

    private final HttpExchange httpExchange;
    private final String body;
    private final Map<String, String> queryParams;

    public HttpExchangeBlinkRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        body = getAsString(httpExchange.getRequestBody());
        queryParams = new QueryParamsExtractor(httpExchange.getRequestURI().getQuery()).toMap();
    }

    private String getAsString(InputStream requestBody) {
        return new InputStreamReader(requestBody).readAsString();
    }

    @Override
    public String body() {
        return body;
    }

    @Override
    public String param(String name) {
        return queryParams.get(name);
    }

    @Override
    public String pathParam(String id) {
        throw new UnsupportedOperationException("Path queryParams are unsupported here ");
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
        if (httpExchange.getRequestHeaders().get(name) == null) {
            throw new HeaderNotFoundException("Header: [" + name + "] does not exist in request");
        }
    }

    @Override
    public String cookie(String name) {
        if (cookieHeaderExists()) {
            String[] collect = httpExchange.getRequestHeaders().get(COOKIE_HEADER).get(0).split(";");
            return Arrays.stream(collect).collect(Collectors.toMap(x -> x.split("=")[0], x -> x.split("=")[1])).get(name);
        }
        return null;
    }

    private boolean cookieHeaderExists() {
        return httpExchange.getRequestHeaders().get(COOKIE_HEADER) != null;
    }

}
