package org.rockm.blink;

import java.net.URI;

public interface BlinkRequest {

    /**
     * @return Request body as string
     */
    String body();

    /**
     * Allow to retrieve query parameters
     * @param name The name of the parameter
     * @return The value of the specific parameter
     */
    String param(String name);

    /**
     * Retrieve path parameters
     * @param name The name of the parameter
     * @return The value of the specific parameter
     */
    String pathParam(String name);

    /**
     * @return request uri
     */
    URI uri();

    /**
     * Retrieve header values
     * @param name header name
     * @return header's value
     */
    String header(String name);

    class HeaderNotFoundException extends BlinkException {

        public HeaderNotFoundException(String message) {
            super(message);
        }
    }

    /**
     *  Retrieve cookie values
     * @param name
     * @return
     */
    String cookie(String name);

    class CookieNotFoundException extends BlinkException {

        public CookieNotFoundException(String message) {
            super(message);
        }
    }
}
