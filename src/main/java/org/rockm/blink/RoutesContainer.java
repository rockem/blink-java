package org.rockm.blink;

import java.util.*;
import java.util.stream.Collectors;

public class RoutesContainer {

    private final Map<Method, Set<Route>> methodToRoutes;

    public RoutesContainer(Map<Method, Set<Route>> methodToRoutes) {
        this.methodToRoutes = Collections.unmodifiableMap(methodToRoutes);
    }

    public Route getRouteFor(String method, String path) {
        Optional<Set<Route>> routes = Optional.ofNullable(methodToRoutes.get(Method.valueOf(method)));
        Optional<Route> route = routes.orElse(new HashSet<>()).stream()
                .filter(r -> r.isMatchedPath(path))
                .findFirst();
        return route.orElse(null);
    }

    public RoutesContainer addRoute(Route route) {
        Map<Method, Set<Route>> c = methodToRoutes.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> new HashSet<>(e.getValue())));
        Set<Route> routes = c.computeIfAbsent(route.getMethod(), k -> new HashSet<>());
        routes.remove(route);
        routes.add(route);
        return new RoutesContainer(c);
    }

    private Set<Route> getRoutesFor(Method method) {
        return methodToRoutes.computeIfAbsent(method, k -> new HashSet<>());
    }

    public void clear() {
        methodToRoutes.clear();
    }
}
