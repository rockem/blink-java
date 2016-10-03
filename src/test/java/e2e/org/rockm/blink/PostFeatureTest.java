package e2e.org.rockm.blink;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static e2e.org.rockm.blink.support.HttpUtil.getBodyFrom;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PostFeatureTest extends BlinkServerTest {

    @Test
    public void shouldEchoStringPostRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpResponse response = httpClient.execute(createHttpPost("/hello", new StringEntity("Kuku")));
        assertThat(getBodyFrom(response), is("Kuku"));
    }

    private HttpPost createHttpPost(String path, HttpEntity entity) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(fullPath(path));
        request.setEntity(entity);
        return request;
    }

    @Test
    public void shouldEchoByteArrayWithPostRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpPost request = new HttpPost(fullPath("/hello"));
        request.addHeader(new BasicHeader("Content-Type", "application/json"));
        request.setEntity(new ByteArrayEntity("{\"p\":\"kuku\"}".getBytes()));
        HttpResponse response = httpClient.execute(request);
        assertThat(getBodyFrom(response), is("{\"p\":\"kuku\"}"));
    }

}
