package org.rockm.blink;

public interface BlinkResponse {

    void status(int statusCode);

    void header(String name, String value);

    void type(String contentType);
}
