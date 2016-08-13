package org.rockm.blink;

import java.net.URI;
import java.util.Map;

public class RouteRequestRunner {
    private static final int BAD_REQUEST = 400;
    private final Route route;

    public RouteRequestRunner(Route route) {
        this.route = route;
    }

    public Object run(BlinkRequest request, BlinkResponse response) {
        Object responseBody;
        try {
            responseBody = route.getHandler().handleRequest(
                    new PathParamsBlinkRequest(request, route.getParamsFor(request.uri().getPath())),
                    response);
        } catch (BlinkRequest.HeaderNotFoundException e) {
            responseBody = e.getMessage();
            response.status(BAD_REQUEST);
        }
        return responseBody;
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
    }
}
