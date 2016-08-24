package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.rockm.blink.BlinkRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpExchangeBlinkRequest implements BlinkRequest {

    private final HttpExchange httpExchange;
    private final String body;
    private final Map<String, String> queryParams;

    public HttpExchangeBlinkRequest(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
        body = getAsString(httpExchange.getRequestBody());
        queryParams = setQueryParams();
    }

    private String getAsString(InputStream requestBody) {
        return new InputStreamReader(requestBody).toString();
    }

    private Map<String, String> setQueryParams() {
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

    private void validateHeaderExists(String name) {
        if(httpExchange.getRequestHeaders().get(name) == null) {
            throw new HeaderNotFoundException("Header: [" + name + "] does not exist in request");
        }
    }

    private class InputStreamReader {
        private static final int BUFFER_SIZE = 16384;

        private InputStream requestBody;

        public InputStreamReader(InputStream requestBody) {
            this.requestBody = requestBody;
        }

        public String toString() {
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
            return "";
        }
    }
}
