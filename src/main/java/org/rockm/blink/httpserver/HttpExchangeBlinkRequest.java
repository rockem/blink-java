package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.io.InputStreamReader;

import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpExchangeBlinkRequest implements BlinkRequest {

    private final HttpExchange httpExchange;
    private final String body;
    private final Map<String, String> queryParams;

    public HttpExchangeBlinkRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        body = getAsString(httpExchange.getRequestBody());
        queryParams = getQueryParams();
    }

    private String getAsString(InputStream requestBody) {
        return new InputStreamReader(requestBody).readAsString();
    }

    private Map<String, String> getQueryParams() {
        return Arrays.stream(allQueryParams()).collect(Collectors.toMap(
           p -> p.split("=")[0], p -> p.split("=")[1]
        ));
    }

    private String[] allQueryParams() {
        if(httpExchange.getRequestURI().getQuery() == null) {
            return new String[0];
        }
        return httpExchange.getRequestURI().getQuery().split("&");
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

    @Override
    public String cookie(String name) {
        validateCookiesExists(name);
        String[] collect = httpExchange.getRequestHeaders().get("Set-Cookie").get(0).split(";");
        return Arrays.stream(collect).collect(Collectors.toMap(x -> x.split("=")[0], x -> x.split("=")[1])).get(name);
    }

    private void validateCookiesExists(String name) {
        if(httpExchange.getRequestHeaders().get("Set-Cookie") == null){
            throw new CookieNotFoundException("Cookie: [" + name + "] does not exist in request");
        }
    }

    private void validateHeaderExists(String name) {
        if(httpExchange.getRequestHeaders().get(name) == null) {
            throw new HeaderNotFoundException("Header: [" + name + "] does not exist in request");
        }
    }

}
