package test.org.rockm.blink;

import org.junit.Test;
import org.rockm.blink.BlinkRequest;
import org.rockm.blink.BlinkResponse;
import org.rockm.blink.ExceptionHandler;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ExceptionHandlerTest {

    private static final String ERROR_MESSAGE = "message";

    private final BlinkRequest request = mock(BlinkRequest.class);
    private final BlinkResponse response = mock(BlinkResponse.class);

    @Test
    public void shouldHandleUnexpectedError() throws Exception {
        ExceptionHandler eh = new ExceptionHandler();
        Object responseBody = eh.handle(new Exception(ERROR_MESSAGE), request, response);
        assertThat(responseBody, is(ERROR_MESSAGE));
        verify(response).status(ExceptionHandler.SERVER_ERROR);
    }
}