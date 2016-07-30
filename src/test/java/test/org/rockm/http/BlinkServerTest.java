package test.org.rockm.http;

import com.sun.net.httpserver.HttpExchange;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.*;
import org.rockm.http.BlinkResponse;
import org.rockm.http.BlinkServer;

import java.io.*;
import java.net.URL;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BlinkServerTest {

    private static BlinkServer blinkServer;
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    @BeforeClass
    public static void startBlink() throws Exception {
        blinkServer = new BlinkServer(4567);
    }

    @AfterClass
    public static void stopBlink() throws Exception {
        blinkServer.stop();
    }

    @Test
    public void shouldGetAStringResponse() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        HttpResponse response = httpClient.execute(new HttpGet("http://localhost:4567/hello"));
        assertThat(getBodyFrom(response), is("Hello World"));
    }

    private String getBodyFrom(HttpResponse response) throws IOException {
        return new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
    }

    @Test
    public void shouldEchoPostRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpResponse response = httpClient.execute(createHttpPost());
        assertThat(getBodyFrom(response), is("Kuku"));
    }

    private HttpPost createHttpPost() throws UnsupportedEncodingException {
        HttpPost request = new HttpPost("http://localhost:4567/hello");
        request.setEntity(new StringEntity("Kuku"));
        return request;
    }
}
