package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;
import test.org.rockm.blink.util.FileUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.rockm.blink.httpserver.HttpExchangeBlinkResponse.CONTENT_TYPE;
import static test.org.rockm.blink.HttpUtil.fullPath;
import static test.org.rockm.blink.util.FileUtil.fileInBytes;

public class HttpExchangeBlinkResponseTest extends AbstractHttpExchangeBlinkResponseTest {

    private static final String DEFAULT_CONTENT_TYPE = "app/json";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        blinkResponse = new HttpExchangeBlinkResponse(DEFAULT_CONTENT_TYPE);
    }

    @Test
    public void bodyShouldNotBeNull() throws Exception {
        apply();
        assertThat(responseOutputStream.toString("UTF-8"), is(""));
    }

    @Test
    public void shouldHandleNullBody() throws Exception {
        ((HttpExchangeBlinkResponse)blinkResponse).setBody(null);
        apply();
        assertThat(responseOutputStream.toString("UTF-8"), is(""));
    }

    @Test
    public void shouldSetStatusCode() throws Exception {
        blinkResponse.status(404);
        apply();
        verify(httpExchange).sendResponseHeaders(404, 0);
    }

    @Test
    public void defaultStatusShouldBe200() throws Exception {
        apply();
        verify(httpExchange).sendResponseHeaders(200, 0);
    }

    @Test
    public void shouldAddHeadersToResponse() throws Exception {
        blinkResponse.header("content", "kuku");
        blinkResponse.header("Accept", "popo");
        apply();
        assertThat(headers.get("content"), is(Arrays.asList("kuku")));
        assertThat(headers.get("Accept"), is(Arrays.asList("popo")));
    }

    @Test
    public void shouldSetContentTypeHeader() throws Exception {
        blinkResponse.type("application/json");
        apply();
        assertThat(headers.get(CONTENT_TYPE), is(Arrays.asList("application/json")));
    }

    @Test
    public void shouldSetDefaultContentType() throws Exception {
        apply();
        assertThat(headers.get(CONTENT_TYPE), is(Arrays.asList(DEFAULT_CONTENT_TYPE)));
    }

    @Test
    public void shouldRetrieveAPicture() throws Exception {
        byte[] imageInBytes = fileInBytes("blink-img.jpg");
        ((HttpExchangeBlinkResponse)blinkResponse).setBody(imageInBytes);
        apply();
        assertThat(responseOutputStream.toByteArray(), is(imageInBytes));
    }
}