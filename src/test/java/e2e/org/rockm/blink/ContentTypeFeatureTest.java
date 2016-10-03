package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ContentTypeFeatureTest extends BlinkServerTest {

    @Test
    public void shouldSetDefaultContentType() throws Exception {
        blinkServer.contentType("application/json");
        blinkServer.get("/json", (req, res) -> "HelloWorld");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/json")));
        assertThat(response.getFirstHeader("Content-Type").getValue(), is("application/json"));
    }

}
