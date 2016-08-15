package test.org.rockm.blink;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
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
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class BlinkServerTest {

    private static final int PORT = 4567;
    private static final String DOMAIN = "http://localhost:" + PORT;


    private static BlinkServer blinkServer;
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    @BeforeClass
    public static void startBlink() throws Exception {
        blinkServer = new BlinkServer(PORT);
    }

    @Test
    public void shouldGetAStringResponse() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello")));
        assertThat(getBodyFrom(response), is("Hello World"));
    }

    private String fullPath(String path) {
        return format("%s" + path, DOMAIN);
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
        HttpPost request = new HttpPost(fullPath("/hello"));
        request.setEntity(new StringEntity("Kuku"));
        return request;
    }

    @Test
    public void shouldGetWithPathParameter() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello " + req.param("name"));
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello?name=Popo")));
        assertThat(getBodyFrom(response), is("Hello Popo"));
    }

    @Test
    public void shouldDeleteWithPathParameters() throws Exception {
        KukuDeleteServerStub serverStub = new KukuDeleteServerStub(blinkServer);
        httpClient.execute(new HttpDelete(fullPath("/kukus/542")));
        assertThat(serverStub.idToDelete, is("542"));
    }

    private class KukuDeleteServerStub {
        String idToDelete;

        KukuDeleteServerStub(BlinkServer server) throws IOException {
            server.delete("/kukus/{id}", (req, res) -> idToDelete = req.pathParam("id"));
        }
     }

    @Test
    public void shouldEchoPutRequest() throws Exception {
        blinkServer.put("/hello", (req, res) -> req.body());
        HttpPut request = new HttpPut(fullPath("/hello"));
        request.setEntity(new StringEntity("Dudu"));
        HttpResponse response = httpClient.execute(request);
        assertThat(getBodyFrom(response), is("Dudu"));
    }

    @Test
    public void shouldReturnBadRequestWhenFetchingNonExistingHeader() throws Exception {
        blinkServer.get("/kuku", (req, res) -> req.header("stam"));
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/kuku")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_BAD_REQUEST));
    }

    @Test
    public void shouldReturn404ForBadPath() throws Exception {
        blinkServer.get("/hello", (req, res) -> "");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/kululu")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void shouldRetrieveResponseHeader() throws Exception {
        blinkServer.get("/hello", (req, res) -> {
            res.header("Content-Type", "application/json"); return "";
        });
        HttpGet httpGet = new HttpGet(fullPath("/hello"));
        HttpResponse response = httpClient.execute(httpGet);
        assertThat(response.getFirstHeader("Content-type").getValue(), equalTo("application/json"));
    }

    @AfterClass
    public static void stopBlink() throws Exception {
        blinkServer.stop();
    }

}
