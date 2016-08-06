package org.rockm.blink;

import java.net.URI;
import java.util.Map;

public class RouteRequestRunner {
    private final Route route;

    public RouteRequestRunner(Route route) {
        this.route = route;
    }

    public Object run(BlinkRequest request, BlinkResponse response) {
        return route.getHandler().handleRequest(
                new PathParamsBlinkRequest(request, route.getParamsFor(request.uri().getPath())),
                response);
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
    }
}
