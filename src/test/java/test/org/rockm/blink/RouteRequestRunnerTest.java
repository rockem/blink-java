package test.org.rockm.blink;

import org.junit.Test;
import org.rockm.blink.*;

import java.net.URI;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class RouteRequestRunnerTest {

    private static final String MESSAGE = "error message";
    private RequestHandler requestHandler = mock(RequestHandler.class);
    private BlinkRequest request = mock(BlinkRequest.class);
    private BlinkResponse response = mock(BlinkResponse.class);

    @Test
    public void shouldSetResponseBadRequestAndMessage() throws Exception {
        Route route = new Route(Method.GET, "/popo", requestHandler);
        RouteRequestRunner runner = new RouteRequestRunner(route);
        when(requestHandler.handleRequest(any(), any())).thenThrow(new BlinkRequest.HeaderNotFoundException(MESSAGE));
        when(request.uri()).thenReturn(URI.create("http://a.com"));
        String body = (String) runner.run(request, response);
        assertThat(body, is(MESSAGE));
        verify(response).status(400);
    }
}