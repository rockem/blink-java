package test.groovy.org.rockm.blink.httpserver;

import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpExchangeBlinkResponseTest {

    private final HttpExchangeBlinkResponse blinkResponse = new HttpExchangeBlinkResponse();
    private final HttpExchangeStub httpExchange = new HttpExchangeStub();
    private final ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();

    @Before
    public void setUp() throws Exception {
        httpExchange.responseBody = responseOutputStream;
    }

    @Test
    public void bodyShouldNotBeNull() throws Exception {
        apply();
        assertThat(responseOutputStream.toString("UTF-8"), is(""));
    }

    @Test
    public void shouldHandleNullBody() throws Exception {
        blinkResponse.setBody(null);
        apply();
        assertThat(responseOutputStream.toString("UTF-8"), is(""));
    }

    private void apply() throws IOException {
        blinkResponse.apply(httpExchange);
    }

    @Test
    public void shouldSetStatusCode() throws Exception {
        blinkResponse.status(404);
        apply();
        assertThat(httpExchange.statusCode, is(404));
    }

    @Test
    public void defaultStatusShouldBe200() throws Exception {
        apply();
        assertThat(httpExchange.statusCode, is(200));
    }
}