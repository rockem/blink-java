package test.org.rockm.blink;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.rockm.blink.BlinkServer;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static test.org.rockm.blink.HttpUtil.PORT;
import static test.org.rockm.blink.HttpUtil.fullPath;
import static test.org.rockm.blink.util.FileUtil.fileInBytes;

public class BlinkServerTest {


    private static BlinkServer blinkServer;
    private final HttpClient httpClient = HttpClientBuilder.create().build();

    @BeforeClass
    public static void startBlink() throws Exception {
        blinkServer = new BlinkServer(PORT);
    }

    @Before
    public void setUp() throws Exception {
        blinkServer.reset();
    }

    @Test
    public void shouldGetAStringResponse() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello")));
        assertThat(getBodyFrom(response), is("Hello World"));
    }

    private String getBodyFrom(HttpResponse response) throws IOException {
        return new BufferedReader(new InputStreamReader(response.getEntity().getContent())).readLine();
    }

    @Test
    public void shouldEchoPostRequest() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpResponse response = httpClient.execute(createHttpPost("/hello", new StringEntity("Kuku")));
        assertThat(getBodyFrom(response), is("Kuku"));
    }

    private HttpPost createHttpPost(String path, HttpEntity entity) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost(fullPath(path));
        request.setEntity(entity);
        return request;
    }

    @Test
    public void shouldEchoPostRequest2() throws Exception {
        blinkServer.post("/hello", (req, res) -> req.body());
        HttpPost request = new HttpPost(fullPath("/hello"));
        request.addHeader(new BasicHeader("Content-Type", "application/json"));
        request.setEntity(new ByteArrayEntity("{\"p\":\"kuku\"}".getBytes()));
        HttpResponse response = httpClient.execute(request);
        assertThat(getBodyFrom(response), is("{\"p\":\"kuku\"}"));
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
            res.header("Content-Type", "application/json");
            return "";
        });
        HttpGet httpGet = new HttpGet(fullPath("/hello"));
        HttpResponse response = httpClient.execute(httpGet);
        assertThat(response.getFirstHeader("Content-type").getValue(), equalTo("application/json"));
    }

    @Test
    public void shouldResetRouting() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        blinkServer.reset();
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello")));
        assertThat(response.getStatusLine().getStatusCode(), is(HttpStatus.SC_NOT_FOUND));
    }

    @Test
    public void shouldSetDefaultContentType() throws Exception {
        blinkServer.contentType("application/json");
        blinkServer.get("/json", (req, res) -> "HelloWorld");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/json")));
        assertThat(response.getFirstHeader("Content-Type").getValue(), is("application/json"));
    }

    @Test
    public void shouldResetContentType() throws Exception {
        blinkServer.contentType("application/json");
        blinkServer.reset();
        blinkServer.get("/json", (req, res) -> "HelloWorld");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/json")));
        assertNull(response.getFirstHeader("Content-Type"));
    }

    @Test
    public void shouldRetrieveImage() throws Exception {
        byte[] imageInBytes = fileInBytes("blink-img.jpg");
        blinkServer.contentType("image/jpg");
        blinkServer.get("/img", (req, res) -> imageInBytes);
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/img")));
        assertThat(IOUtils.toByteArray(response.getEntity().getContent()), is(imageInBytes));
    }

    @AfterClass
    public static void stopBlink() throws Exception {
        blinkServer.stop();
    }

}
