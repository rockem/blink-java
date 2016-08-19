package test.org.rockm.blink;

import org.junit.Test;
import org.rockm.blink.RoutesContainer;

import static org.junit.Assert.assertNull;

public class RoutesContainerTest {

    @Test
    public void shouldRetrieveNullWhenNoRoutes() throws Exception {
        assertNull(new RoutesContainer().getRouteFor("GET", "http://stam"));
    }
}