package org.rockm.blink;

import com.sun.net.httpserver.HttpExchange;

import java.util.*;

public class RoutesContainer {

    private Map<Method, Set<Route>> methodToRoutes = new HashMap<>();

    public Route getRouteFor(String method, String path) {
        Optional<Set<Route>> routes = Optional.ofNullable(methodToRoutes.get(Method.valueOf(method)));
        Optional<Route> route = routes.orElse(new HashSet<>()).stream()
                .filter(r -> r.isMatchedPath(path))
                .findFirst();
        return route.orElse(null);
    }

    public void addRoute(Route route) {
        Set<Route> routes = getRoutesFor(route.getMethod());
        routes.remove(route);
        routes.add(route);
    }

    private Set<Route> getRoutesFor(Method method) {
        Set<Route> routes = methodToRoutes.get(method);
        if(routes == null) {
            routes = new HashSet<>();
            methodToRoutes.put(method, routes);
        }
        return routes;
    }

    public void clear() {
        methodToRoutes.clear();
    }
}
