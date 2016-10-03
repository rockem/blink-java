package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static e2e.org.rockm.blink.support.HttpUtil.getBodyFrom;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PutFeatureTest extends BlinkServerTest {

    @Test
    public void shouldEchoPutRequest() throws Exception {
        blinkServer.put("/hello", (req, res) -> req.body());
        HttpPut request = new HttpPut(fullPath("/hello"));
        request.setEntity(new StringEntity("Dudu"));
        HttpResponse response = httpClient.execute(request);
        assertThat(getBodyFrom(response), is("Dudu"));
    }
}
