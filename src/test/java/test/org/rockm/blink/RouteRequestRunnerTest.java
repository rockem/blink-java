package test.org.rockm.blink;

import org.junit.Before;
import org.junit.Test;
import org.rockm.blink.*;

import java.net.URI;

import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.rockm.blink.ExceptionHandler.BAD_REQUEST;
import static org.rockm.blink.ExceptionHandler.NOT_FOUND;

public class RouteRequestRunnerTest {

    private static final String HEADER_NOR_FOUND_MESSAGE = "error message";
    private static final String PATH = "/lala";
    private RequestHandler requestHandler = mock(RequestHandler.class);
    private BlinkRequest request = mock(BlinkRequest.class);
    private BlinkResponse response = mock(BlinkResponse.class);

    @Before
    public void setUp() throws Exception {
        when(request.uri()).thenReturn(URI.create("http://a.com" + PATH));
    }

    @Test
    public void shouldSetResponseBadRequestAndMessage() throws Exception {
        Route route = new Route(Method.GET, "/popo", requestHandler);
        when(requestHandler.handleRequest(any(), any())).thenThrow(new BlinkRequest.HeaderNotFoundException(HEADER_NOR_FOUND_MESSAGE));
        String body = (String) runWith(route);
        assertThat(body, is(HEADER_NOR_FOUND_MESSAGE));
        verify(response).status(BAD_REQUEST);
    }

    private Object runWith(Route route) {
        return new RouteRequestRunner(route).run(request, response);
    }

    @Test
    public void shouldSetResponseAsNotFound() throws Exception {
        String body = (String) runWith(null);
        assertThat(body, is(format(RouteRequestRunner.NOT_FOUND_MESSAGE, PATH)));
        verify(response).status(NOT_FOUND);

    }
}