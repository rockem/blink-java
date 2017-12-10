package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.Method;
import org.rockm.blink.httpserver.MultiMethodHttpHandler;
import org.rockm.blink.RequestHandler;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MultiMethodHttpHandlerTest {

    private static final String PATH_WITH_PARAM = "/blinks/{id}";
    private static final String PARAM_ID = "234";

    private final HttpExchange httpExchange = mock(HttpExchange.class);
    private final MultiMethodHttpHandler handler = new MultiMethodHttpHandler();
    private final StubRequestHandler requestHandler = new StubRequestHandler();

    @Before
    public void setUp() throws Exception {
        when(httpExchange.getResponseBody()).thenReturn(new ByteArrayOutputStream());
        when(httpExchange.getRequestBody()).thenReturn(new ByteArrayInputStream("".getBytes()));
    }

    @Ignore
    @Test
    public void shouldRetrievePathParam() throws Exception {
        handler.addHandler(PATH_WITH_PARAM, Method.DELETE, requestHandler);
        when(httpExchange.getRequestURI()).thenReturn(URI.create("http://domain.com/blinks/" + PARAM_ID));
        when(httpExchange.getRequestMethod()).thenReturn("DELETE");
        handler.handle(httpExchange);
        assertThat(requestHandler.receivedParam, is(PARAM_ID));

    }

    private class StubRequestHandler implements RequestHandler {
        public String receivedParam;

        @Override
        public Object handleRequest(BlinkRequest request, BlinkResponse response) {
            receivedParam = request.pathParam("id");
            return "";
        }
    }

}