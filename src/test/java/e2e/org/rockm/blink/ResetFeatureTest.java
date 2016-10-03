package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ResetFeatureTest extends BlinkServerTest {

    @Test
    public void shouldResetRouting() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        blinkServer.reset();
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void shouldResetContentType() throws Exception {
        blinkServer.contentType("application/json");
        blinkServer.reset();
        blinkServer.get("/json", (req, res) -> "HelloWorld");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/json")));
        assertNull(response.getFirstHeader("Content-Type"));
    }
}
