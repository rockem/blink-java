package e2e.org.rockm.blink;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.rockm.blink.BlinkServer;

import static e2e.org.rockm.blink.support.HttpUtil.PORT;

public abstract class BlinkServerTest {

    protected static BlinkServer blinkServer;
    protected final HttpClient httpClient = HttpClientBuilder.create().build();

    @Before
    public void setUp() throws Exception {
        if(blinkServer == null) {
            blinkServer = new BlinkServer(PORT);
        }
        blinkServer.reset();
    }

}
