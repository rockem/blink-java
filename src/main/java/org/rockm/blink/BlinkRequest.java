package org.rockm.blink;

import java.net.URI;

public interface BlinkRequest {

    String body();

    String param(String name);

    String pathParam(String id);

    URI uri();
}
