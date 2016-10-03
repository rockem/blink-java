package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class ErrorsFeatureTest extends BlinkServerTest {

    @Test
    public void shouldReturn404ForBadPath() throws Exception {
        blinkServer.get("/hello", (req, res) -> "");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/kululu")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_NOT_FOUND));
    }
}
