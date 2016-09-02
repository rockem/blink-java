package test.groovy.org.rockm.blink

import groovyx.net.http.RESTClient
import org.junit.Test
import org.rockm.blink.BlinkServer

class BlinkServerTest_Groovy {

    static final int PORT = 1234

    RESTClient restClient = new RESTClient("http://localhost:$PORT/")

    @Test
    void shouldHandleRequestWithNoReturn() throws Exception {
        new BlinkServer(PORT) {{
            get("/hello", { req, res -> noReturnValue() })
        }}
        restClient.get(path: 'hello')
        // passed
    }

    void noReturnValue() {
        // do nothing
    }

}
