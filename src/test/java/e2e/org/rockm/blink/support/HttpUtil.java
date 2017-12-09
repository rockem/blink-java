package e2e.org.rockm.blink.support;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static java.lang.String.format;

public class HttpUtil {

    public static final int PORT = 4567;
    public static final String DOMAIN = "http://localhost:" + PORT;

    public static String fullPath(String path) {
        return format("%s" + path, DOMAIN);
    }

    public static String getBodyFrom(HttpResponse response) throws IOException {
        return IOUtils.toString(response.getEntity().getContent(), "UTF-8");
    }

    public static HttpPost createHttpPost(String path, HttpEntity entity) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(fullPath(path));
        request.setEntity(entity);
        return request;
    }
}
