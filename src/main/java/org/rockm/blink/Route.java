package org.rockm.blink;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toMap;

public class Route {
    private static final String PATH_PARAM_PLACEHOLDERS_REGEX = "\\{([A-Za-z][A-Za-z0-9]*)\\}";
    private static final String PATH_PARAM_REGEX = "([A-Za-z\\-~\\.\\_0-9]+)";
    private static final String PATH_PARAM_ID_REGEX = "\\{[A-Za-z][A-Za-z0-9]*}";
    public static final String END_OF_LINE = "$";
    private final String route;
    private final String routeRegex;
    private final Method method;
    private final RequestHandler handler;
    private final List<String> paramKeys;

    public Route(Method method, String path, RequestHandler handler) {
        this.method = method;
        this.route = path;
        this.handler = handler;
        this.routeRegex = createRegexFor(route);
        this.paramKeys = extract(PATH_PARAM_PLACEHOLDERS_REGEX, route);
    }

    private String createRegexFor(String route) {
        String regexPath = route.replaceAll(PATH_PARAM_ID_REGEX, PATH_PARAM_REGEX);
        return regexPath.replaceAll("/", "\\\\/") + END_OF_LINE;
    }

    private List<String> extract(String regex, String input) {
        Matcher v = Pattern.compile(regex).matcher(input);
        List<String> values = new ArrayList<>();
        while (v.find() && v.groupCount() > 0) {
            IntStream.range(0, v.groupCount()).boxed().forEach(i -> values.add(v.group( i +1)));
        }
        return values;
    }

    public boolean isMatchedPath(String path) {
        Pattern p = Pattern.compile(routeRegex);
        Matcher m = p.matcher(path);
        return m.find();
    }

    public Map<String, String> getParamsFor(String path) {
        List<String> values = extract(routeRegex, path);
        return IntStream.range(0,values.size()).boxed().collect(toMap(paramKeys::get, values::get));
    }

    public RequestHandler getHandler() {
        return handler;
    }

    public Method getMethod() {
        return method;
    }

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
}
