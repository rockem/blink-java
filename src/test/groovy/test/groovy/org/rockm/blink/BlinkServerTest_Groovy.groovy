package test.groovy.org.rockm.blink

import groovyx.net.http.RESTClient
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.junit.Test
import org.rockm.blink.BlinkServer

import static java.lang.String.format
import static org.hamcrest.core.Is.is
import static org.junit.Assert.assertThat

class BlinkServerTest_Groovy {

    static final int PORT = 1234

    RESTClient restClient = new RESTClient("http://localhost:$PORT/")

    @Test
    public void shouldHandleRequestWithNoReturn() throws Exception {
        new BlinkServer(PORT) {{
            get("/hello", { req, res -> print "do nothing" })
        }}
        restClient.get(path: 'hello')
        // passed
    }
}
