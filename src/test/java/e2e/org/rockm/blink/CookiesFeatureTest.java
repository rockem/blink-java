package e2e.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;

public class CookiesFeatureTest extends BlinkServerTest {

    private static final String COOKIE_NAME = "name";

    @Test
    public void sendAndRetrieveCookies() throws Exception {
        blinkServer.get("/cookies", (req, res) -> {
            res.cookie(COOKIE_NAME, req.cookie(COOKIE_NAME));
            return "";
        });
        HttpGet get = new HttpGet(fullPath("/cookies"));
        get.addHeader("Cookie", COOKIE_NAME + "=kuku");
        HttpResponse response = httpClient.execute(get);
        Assert.assertThat(response.getFirstHeader("Set-Cookie").getValue(), is(COOKIE_NAME + "=kuku"));
    }
}
