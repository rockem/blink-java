package test.org.rockm.blink;

import static java.lang.String.format;

public class HttpUtil {

    public static final int PORT = 4567;
    public static final String DOMAIN = "http://localhost:" + PORT;

    public static String fullPath(String path) {
        return format("%s" + path, DOMAIN);
    }
}
