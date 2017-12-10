package org.rockm.blink;

import java.io.IOException;

public interface Server {

    void stop();

    void setDefaultContentType(String contentType);

    BlinkRequest getLastRequest();

    void addRoute(Route route) throws IOException;

    void reset();
}
