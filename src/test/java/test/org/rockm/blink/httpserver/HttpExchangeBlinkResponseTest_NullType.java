package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.httpserver.HttpExchangeBlinkResponse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;
import static org.rockm.blink.httpserver.HttpExchangeBlinkResponse.CONTENT_TYPE;

public class HttpExchangeBlinkResponseTest_NullType extends AbstractHttpExchangeBlinkResponseTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        blinkResponse = new HttpExchangeBlinkResponse(null);
    }

    @Test
    public void shouldNotSetDefaultContentTypeWhenNull() throws Exception {
        apply();
        assertNull(headers.get(CONTENT_TYPE));
    }
}