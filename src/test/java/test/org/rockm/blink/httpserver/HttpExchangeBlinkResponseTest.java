package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.rockm.blink.httpserver.HttpExchangeBlinkResponse.CONTENT_TYPE;

public class HttpExchangeBlinkResponseTest {

    private final BlinkResponse blinkResponse = new HttpExchangeBlinkResponse();
    private final HttpExchange httpExchange = mock(HttpExchange.class);
    private final ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    private final Headers headers = new Headers();

    @Before
    public void setUp() throws Exception {
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
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

    private void apply() throws IOException {
        ((HttpExchangeBlinkResponse)blinkResponse).apply(httpExchange);
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
}