package org.rockm.blink;

import java.util.HashMap;
import java.util.Map;

public class ExceptionHandler {
    public static final int BAD_REQUEST = 400;
    public static final int NOT_FOUND = 404;
    public static final int SERVER_ERROR = 500;

    private static final Map<Class<? extends Exception>, Integer> expToStatus = new HashMap<>();

    static {
        expToStatus.put(BlinkRequest.HeaderNotFoundException.class, BAD_REQUEST);
        expToStatus.put(RouteNotFoundException.class, NOT_FOUND);
    }

    public Object handle(Exception e, BlinkRequest request, BlinkResponse response) {
        response.status(expToStatus.getOrDefault(e.getClass(), SERVER_ERROR));
        return e.getMessage();
    }

}
