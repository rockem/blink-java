package test.groovy.org.rockm.blink.httpserver;

import org.junit.Test;
import org.rockm.blink.httpserver.HttpExchangeBlinkRequest;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class HttpExchangeBlinkRequestTest {


    private static final String ID = "324324";

    @Test
    public void shouldRetrieveParamByName() throws Exception {
        HttpExchangeStub httpExchange = new HttpExchangeStub();
        httpExchange.requestURI = URI.create("http://domain.com?name=popo");
        HttpExchangeBlinkRequest request = new HttpExchangeBlinkRequest(httpExchange);
        assertThat(request.param("name"), is("popo"));
    }

}