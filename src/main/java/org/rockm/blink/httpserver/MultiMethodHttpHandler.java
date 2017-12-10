package org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.rockm.blink.*;

import java.io.IOException;

@Deprecated
public class MultiMethodHttpHandler implements HttpHandler {

    private RoutesContainer routesContainer; // = new RoutesContainer();
    private String defaultContentType;

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Route route = routesContainer.getRouteFor(
                httpExchange.getRequestMethod(),
                httpExchange.getRequestURI().getPath());
        HttpExchangeBlinkResponse response = new HttpExchangeBlinkResponse(defaultContentType);
        HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);
        response.setBody(new RouteRequestRunner(route).run(request, response));
        response.apply(httpExchange);
    }

    public void addHandler(String path, Method method, RequestHandler handler) {
        routesContainer.addRoute(new Route(method, path, handler));
    }

    public void reset() {
        //routesContainer.clear();
        defaultContentType = null;
    }

    public void contentType(String type) {
        defaultContentType = type;
    }
}