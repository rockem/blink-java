package test.org.rockm.blink;

import org.junit.Test;
import org.rockm.blink.Method;
import org.rockm.blink.Route;
import org.rockm.blink.RoutesContainer;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class RoutesContainerTest {

    @Test
    public void shouldRetrieveNullWhenNoRoutes() throws Exception {
        assertNull(new RoutesContainer().getRouteFor("GET", "http://stam"));
    }

}