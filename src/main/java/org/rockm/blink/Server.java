package org.rockm.blink;

import java.io.IOException;

public interface Server {

    void startIfNeeded() throws IOException;

    void stop();

    void setDefaultContentType(String contentType);

    BlinkRequest getLastRequest();
}
