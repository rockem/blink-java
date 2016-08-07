package test.org.rockm.blink.httpserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;
import org.junit.Test;
import org.rockm.blink.httpserver.HttpExchangeBlinkRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
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