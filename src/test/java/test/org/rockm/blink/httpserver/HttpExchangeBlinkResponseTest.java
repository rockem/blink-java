package test.org.rockm.blink.httpserver;

import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.rockm.blink.httpserver.HttpExchangeBlinkResponse.CONTENT_TYPE;

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
    public void shouldAddCookieToResponse() throws IOException {
        blinkResponse.cookie("myCookie", "zabo");
        apply();
        assertThat(headers.get("Set-Cookie"), is(Arrays.asList("myCookie=zabo")));
    }

    @Test
    public void shouldAddNumberOfCookiesToResponse() throws IOException {
        blinkResponse.cookie("myCookie", "zabo");
        blinkResponse.cookie("mySecondCookie", "kuku");
        apply();
        assertThat(headers.get("Set-Cookie"), is(Arrays.asList("myCookie=zabo;mySecondCookie=kuku")));
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
    public void shouldRetrieveByteArray() throws Exception {
        byte[] bytes = new byte[] {3, 2, 45, 23, 67, 12};
        ((HttpExchangeBlinkResponse)blinkResponse).setBody(bytes);
        apply();
        assertThat(responseOutputStream.toByteArray(), is(bytes));
    }
}