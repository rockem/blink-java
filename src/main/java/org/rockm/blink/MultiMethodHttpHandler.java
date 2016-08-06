package org.rockm.blink;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.*;

public class MultiMethodHttpHandler implements HttpHandler {

    private Map<String, Map<String, RequestHandler>> methodToPath = new HashMap<>();
    private Map<Method, Set<Route>> methodToRoutes = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Route route = getRouteFor(httpExchange);
        HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
        HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);
        response.setBody((String) route.handleRequest(request, response));
        response.apply(httpExchange);
    }

    private Route getRouteFor(HttpExchange httpExchange) {
        Set<Route> routes = methodToRoutes.get(Method.valueOf(httpExchange.getRequestMethod()));
        return routes.stream()
                .filter(r -> r.isMatchedPath(httpExchange.getRequestURI().getPath()))
                .findFirst().get();
    }

    public void addHandler(String path, String method, RequestHandler handler) {
        Method m = Method.valueOf(method);
        Set<Route> routes = getRoutesFor(m);
        Route r = new Route(m, path, handler);
        routes.remove(r);
        routes.add(r);
    }

    private Set<Route> getRoutesFor(Method method) {
        Set<Route> routes = methodToRoutes.get(method);
        if(routes == null) {
            routes = new HashSet<>();
            methodToRoutes.put(method, routes);
        }
        return routes;
    }


}