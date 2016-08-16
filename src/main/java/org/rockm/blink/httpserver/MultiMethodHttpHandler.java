package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.rockm.blink.Method;
import org.rockm.blink.RequestHandler;
import org.rockm.blink.Route;
import org.rockm.blink.RouteRequestRunner;

import java.io.IOException;
import java.util.*;

public class MultiMethodHttpHandler implements HttpHandler {

    private Map<Method, Set<Route>> methodToRoutes = new HashMap<>();

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Route route = getRouteFor(httpExchange);
        HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse();
        HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);
        response.setBody(new RouteRequestRunner(route).run(request, response));
        response.apply(httpExchange);
    }

    private Route getRouteFor(HttpExchange httpExchange) {
        Set<Route> routes = methodToRoutes.get(Method.valueOf(httpExchange.getRequestMethod()));
        Optional<Route> route = routes.stream()
                .filter(r -> r.isMatchedPath(httpExchange.getRequestURI().getPath()))
                .findFirst();
        return route.orElse(null);
    }

    public void addHandler(String path, Method method, RequestHandler handler) {
        Set<Route> routes = getRoutesFor(method);
        Route r = new Route(method, path, handler);
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