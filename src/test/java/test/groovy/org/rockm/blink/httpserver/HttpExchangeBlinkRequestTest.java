package test.groovy.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import org.junit.Test;
import org.mockito.Matchers;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.httpserver.HttpExchangeBlinkRequest;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpExchangeBlinkRequestTest {

    private final HttpExchangeStub httpExchange = new HttpExchangeStub();
    private final HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);

    @Test
    public void shouldRetrieveParamByName() throws Exception {
        httpExchange.requestURI = URI.create("http://domain.com?name=popo");
        assertThat(request.param("name"), is("popo"));
    }

    @Test
    public void shouldRetrieveHeaderByName() throws Exception {
        httpExchange.requestHeaders.add("key", "kuku");
        assertThat(request.header("key"), is("kuku"));
    }

    @Test
    public void shouldRetrieveMultipleValuesHeaderByName() throws Exception {
        httpExchange.requestHeaders.add("key", "kuku1");
        httpExchange.requestHeaders.add("key", "kuku2");
        assertThat(request.header("key"), is("kuku1,kuku2"));
    }

    @Test(expected = BlinkRequest.HeaderNotFoundException.class)
    public void shouldFailWhenReadNonExistingHeader() throws Exception {
        assertNull(request.header("lala"));
    }
}