package org.rockm.blink;

import java.net.URISyntaxException;

public interface RequestHandler {

    Object handleRequest(BlinkRequest request, BlinkResponse response);
}
