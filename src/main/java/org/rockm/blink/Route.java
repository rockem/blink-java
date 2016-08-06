package org.rockm.blink;

import java.net.URI;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Route {
    private final String route;
    private final String routeRegex;
    private final Method method;
    private final RequestHandler handler;
    private final List<String> paramKeys;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Route)) return false;
        Route route1 = (Route) o;
        return Objects.equals(route, route1.route) &&
                method == route1.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, method);
    }

    public Route(Method method, String path, RequestHandler handler) {
        this.method = method;
        this.route = path;
        this.handler = handler;
        this.routeRegex = createRegexFor(route);
        this.paramKeys = extract("\\{([A-Za-z][A-Za-z0-9]*)\\}", route);
    }

    private String createRegexFor(String route) {
        String regexPath = route.replaceAll("\\{[A-Za-z][A-Za-z0-9]*\\}", "([0-9]+)");
        return regexPath.replaceAll("/", "\\\\/");
    }

    public boolean isMatchedPath(String path) {
        Pattern p = Pattern.compile(routeRegex);
        Matcher m = p.matcher(path);
        return m.find();
    }

    private Map<String, String> getParamsFor(String path) {
        List<String> values = extract(routeRegex, path);
        Map<String, String> params = new HashMap<>();
        for (int i = 0; i < values.size(); i++) {
            params.put(paramKeys.get(i), values.get(i));
        }
        return params;
    }

    private List<String> extract(String regex, String input) {
        Matcher v = Pattern.compile(regex).matcher(input);
        List<String> values = new ArrayList<>();
        while (v.find() && v.groupCount() > 0) {
            values.add(v.group(1));
        }
        return values;
    }

    public Object handleRequest(BlinkRequest request, BlinkResponse response) {
        return handler.handleRequest(new PathParamsBlinkRequest(request, getParamsFor(request.uri().getPath())), response);
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
