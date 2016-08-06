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

    private class HttpExchangeStub extends HttpExchange {
        public URI requestURI;

        @Override
        public Headers getRequestHeaders() {
            return null;
        }

        @Override
        public Headers getResponseHeaders() {
            return null;
        }

        @Override
        public URI getRequestURI() {
            return requestURI;
        }

        @Override
        public String getRequestMethod() {
            return null;
        }

        @Override
        public HttpContext getHttpContext() {
            return null;
        }

        @Override
        public void close() {

        }

        @Override
        public InputStream getRequestBody() {
            return null;
        }

        @Override
        public OutputStream getResponseBody() {
            return null;
        }

        @Override
        public void sendResponseHeaders(int i, long l) throws IOException {

        }

        @Override
        public InetSocketAddress getRemoteAddress() {
            return null;
        }

        @Override
        public int getResponseCode() {
            return 0;
        }

        @Override
        public InetSocketAddress getLocalAddress() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public Object getAttribute(String s) {
            return null;
        }

        @Override
        public void setAttribute(String s, Object o) {

        }

        @Override
        public void setStreams(InputStream inputStream, OutputStream outputStream) {

        }

        @Override
        public HttpPrincipal getPrincipal() {
            return null;
        }
    }
}