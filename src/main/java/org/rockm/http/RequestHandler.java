package org.rockm.http;

public interface RequestHandler {

    Object handleRequest(BlinkRequest request, BlinkResponse response);
}
