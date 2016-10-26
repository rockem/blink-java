package test.org.rockm.blink;

import org.junit.Test;
import org.rockm.blink.Method;
import org.rockm.blink.Route;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class RouteTest {

    @Test
    public void shouldRetrievePathParamValue() throws Exception {
        Route route = new Route(Method.DELETE, "/hello/{id}", null);
        assertThat(route.getParamsFor("/hello/543").get("id"), is("543"));
    }

    @Test
    public void shouldRetrieveMoreThanOnePathParamValue() throws Exception {
        Route route = new Route(Method.DELETE, "/hello/{id}/{name}", null);
        assertThat(route.getParamsFor("/hello/543/kuku").get("id"), is("543"));
        assertThat(route.getParamsFor("/hello/543/kuku").get("name"), is("kuku"));
    }

    @Test
    public void shouldAllowMoreCharsInPathParams() throws Exception {
        Route route = new Route(Method.GET, "/hello/{id}", null);
        String paramWithAllAllowedChars = "a5-._~Lulu";
        assertThat(route.getParamsFor("/hello/" + paramWithAllAllowedChars).get("id"), is(paramWithAllAllowedChars));
    }
}