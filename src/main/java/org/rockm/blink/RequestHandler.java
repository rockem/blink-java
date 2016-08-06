package org.rockm.blink;

public interface RequestHandler {

    Object handleRequest(BlinkRequest request, BlinkResponse response);
}
