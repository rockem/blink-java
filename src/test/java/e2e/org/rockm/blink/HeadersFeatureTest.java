package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class HeadersFeatureTest extends BlinkServerTest {

    @Test
    public void shouldReturnBadRequestWhenFetchingNonExistingHeader() throws Exception {
        blinkServer.get("/kuku", (req, res) -> req.header("stam"));
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/kuku")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldRetrieveResponseHeader() throws Exception {
        blinkServer.get("/hello", (req, res) -> {
            res.header("Content-Type", "application/json");
            return "";
        });
        HttpGet httpGet = new HttpGet(fullPath("/hello"));
        HttpResponse response = httpClient.execute(httpGet);
        assertThat(response.getFirstHeader("Content-type").getValue(), equalTo("application/json"));
    }
}
