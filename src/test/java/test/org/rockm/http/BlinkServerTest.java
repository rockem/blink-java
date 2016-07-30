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
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet("http://localhost:4567/hello"));
        String body = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
        assertThat(body, is("Hello World"));
    }

    @Test
    public void shouldEchoPostRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost("http://localhost:4567/hello");
        request.setEntity(new StringEntity("Kuku"));
        HttpResponse response = httpClient.execute(request);
        String body = new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
        assertThat(body, is("Kuku"));
    }
}
