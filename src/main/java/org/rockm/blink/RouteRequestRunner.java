package org.rockm.blink;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class RouteRequestRunner {
    public static final String NOT_FOUND_MESSAGE = "%s Not Found";

    private final Route route;

    public RouteRequestRunner(Route route) {
        this.route = route;
    }

    public Object run(BlinkRequest request, BlinkResponse response) {
        Object responseBody;
        try {
            String path = request.uri().getPath();
            validateRoute(path);
            responseBody = route.getHandler().handleRequest(
                    new PathParamsBlinkRequest(request, route.getParamsFor(path)),
                    response);
        } catch (Exception e) {
            responseBody = new ExceptionHandler().handle(e, request, response);
        }
        return responseBody;
    }

    private void validateRoute(String path) {
        if(route == null) {
            throw new RouteNotFoundException(format(NOT_FOUND_MESSAGE, path));
        }
    }

    private class PathParamsBlinkRequest implements BlinkRequest {
        private final BlinkRequest decoratedRequest;
        private final Map<String, String> params;

        public PathParamsBlinkRequest(BlinkRequest request, Map<String, String> params) {
            this.decoratedRequest = request;
            this.params = params;
        }

        @Override
        public String body() {
            return decoratedRequest.body();
        }

        @Override
        public String param(String name) {
            return decoratedRequest.param(name);
        }

        @Override
        public String pathParam(String name) {
            return params.get(name);
        }

        @Override
        public URI uri() {
            return decoratedRequest.uri();
        }

        @Override
        public String header(String name) {
            return decoratedRequest.header(name);
        }

        @Override
        public String cookie(String name) {
            return decoratedRequest.cookie(name);
        }
    }
}
