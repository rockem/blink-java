package e2e.org.rockm.blink;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static e2e.org.rockm.blink.support.HttpUtil.getBodyFrom;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static e2e.org.rockm.blink.support.FileUtil.fileInBytes;

public class GetFeatureTest extends BlinkServerTest {

    @Test
    public void shouldGetAStringResponse() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello World");
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello")));
        assertThat(getBodyFrom(response), is("Hello World"));
    }

    @Test
    public void shouldGetWithPathParameter() throws Exception {
        blinkServer.get("/hello", (req, res) -> "Hello " + req.param("name"));
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/hello?name=Popo")));
        assertThat(getBodyFrom(response), is("Hello Popo"));
    }

    @Test
    public void shouldRetrieveImage() throws Exception {
        byte[] imageInBytes = fileInBytes("blink-img.jpg");
        blinkServer.contentType("image/jpg");
        blinkServer.get("/img", (req, res) -> imageInBytes);
        HttpResponse response = httpClient.execute(new HttpGet(fullPath("/img")));
        assertThat(IOUtils.toByteArray(response.getEntity().getContent()), is(imageInBytes));
    }
}
