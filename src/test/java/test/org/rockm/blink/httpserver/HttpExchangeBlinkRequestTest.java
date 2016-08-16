package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.httpserver.HttpExchangeBlinkRequest;

import java.net.URI;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpExchangeBlinkRequestTest {

    private final HttpExchange httpExchange = mock(HttpExchange.class);
    private final BlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);

    private final Headers headers = new Headers();

    @Before
    public void setUp() throws Exception {
        when(httpExchange.getRequestHeaders()).thenReturn(headers);
    }

    @Test
    public void shouldRetrieveParamByName() throws Exception {
        when(httpExchange.getRequestURI()).thenReturn(URI.create("http://domain.com?name=popo"));
        assertThat(request.param("name"), is("popo"));
    }

    @Test
    public void shouldRetrieveHeaderByName() throws Exception {
        headers.put("key", Arrays.asList("kuku"));
        assertThat(request.header("key"), is("kuku"));
    }

    @Test
    public void shouldRetrieveMultipleValuesHeaderByName() throws Exception {
        headers.put("key", Arrays.asList("kuku1", "kuku2"));
        assertThat(request.header("key"), is("kuku1,kuku2"));
    }

    @Test(expected = BlinkRequest.HeaderNotFoundException.class)
    public void shouldFailWhenReadNonExistingHeader() throws Exception {
        assertNull(request.header("lala"));
    }
}