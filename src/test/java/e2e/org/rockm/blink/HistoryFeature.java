package e2e.org.rockm.blink;

import org.apache.http.entity.StringEntity;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.createHttpPost;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class HistoryFeature extends BlinkServerTest {

    @Test
    public void retrieveLastRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        httpClient.execute(createHttpPost("/hello", new StringEntity("Kuku")));
        assertThat(blinkServer.lastRequest().body(), is("Kuku"));
    }

}
