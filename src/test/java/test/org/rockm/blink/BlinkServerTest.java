package test.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rockm.blink.BlinkServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BlinkServerTest {

    public static final int PORT = 4567;
    public static final String DOMAIN = "http://localhost:" + PORT;


    private static BlinkServer blinkServer;
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    @BeforeClass
    public static void startBlink() throws Exception {
        blinkServer = new BlinkServer(PORT);
    }

    @Test
    public void shouldGetAStringResponse() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        HttpResponse response = httpClient.execute(new HttpGet(format("%s/hello", DOMAIN)));
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
        HttpPost request = new HttpPost(format("%s/hello", DOMAIN));
        request.setEntity(new StringEntity("Kuku"));
        return request;
    }

    @Test
    public void shouldGetWithPathParameter() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello " + req.param("name"));
        HttpResponse response = httpClient.execute(new HttpGet(format("%s/hello?name=Popo", DOMAIN)));
        assertThat(getBodyFrom(response), is("Hello Popo"));
    }

    @Test
    public void shouldDeleteWithPathParameters() throws Exception {
        KukuDeleteServerStub serverStub = new KukuDeleteServerStub(blinkServer);
        httpClient.execute(new HttpDelete(format("%s/kukus/542", DOMAIN)));
        assertThat(serverStub.idToDelete, is("542"));
    }

    private class KukuDeleteServerStub {

        public String idToDelete;

        public KukuDeleteServerStub(BlinkServer server) throws IOException {
            server.delete("/kukus/{id}", (req, res) -> idToDelete = req.pathParam("id"));
        }
     }

    @AfterClass
    public static void stopBlink() throws Exception {
        blinkServer.stop();
    }
}
