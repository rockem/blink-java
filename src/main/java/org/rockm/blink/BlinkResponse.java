package org.rockm.blink;

public interface BlinkResponse {

    /**
     * Set the response status code
     * @param statusCode
     */
    void status(int statusCode);

    /**
     * Add a response header
     * @param name header's name
     * @param value header's value
     */
    void header(String name, String value);

    /**
     * Set the content tyoe of the response
     * @param contentType
     */
    void type(String contentType);

    /**
     * Add a response cookie
     * @param name
     * @param value
     */
    void cookie(String name, String value);
}
