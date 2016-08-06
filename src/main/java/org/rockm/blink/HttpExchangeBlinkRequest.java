package org.rockm.blink;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
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
        /*String regexTemplate = templateRoute.replaceAll("\\{[A-Za-z][A-Za-z0-9]*\\}", "([0-9]+)");
        regexTemplate = regexTemplate.replaceAll("\\/", "\\\\/");
        List<String> values = extract(regexTemplate, httpExchange.getRequestURI().getPath());
        List<String> keys = extract("\\{([A-Za-z][A-Za-z0-9]*)\\}", templateRoute);
        return values.get(keys.indexOf(id));*/
        throw new UnsupportedOperationException("Path params is unsupported here ");
    }

    @Override
    public URI uri() {
        return httpExchange.getRequestURI();
    }

    private List<String> extract(String regex, String input) {
        Matcher v = Pattern.compile(regex).matcher(input);
        List<String> values = new ArrayList<>();
        while(v.find()) {
            values.add(v.group(1));
        }
        return values;
    }
}
