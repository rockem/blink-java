package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.rockm.blink.httpserver.HttpExchangeBlinkResponse.CONTENT_TYPE;

public class AbstractHttpExchangeBlinkResponseTest {

    protected BlinkResponse blinkResponse;
    protected final HttpExchange httpExchange = mock(HttpExchange.class);
    protected final ByteArrayOutputStream responseOutputStream = new ByteArrayOutputStream();
    protected final Headers headers = new Headers();

    public void setUp() throws Exception {
        when(httpExchange.getResponseBody()).thenReturn(responseOutputStream);
        when(httpExchange.getResponseHeaders()).thenReturn(headers);
    }

    protected void apply() throws IOException {
        ((HttpExchangeBlinkResponse)blinkResponse).apply(httpExchange);
    }

}
