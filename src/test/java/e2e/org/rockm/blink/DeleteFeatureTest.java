package e2e.org.rockm.blink;

import org.apache.http.client.methods.HttpDelete;
import org.junit.Test;
import org.rockm.blink.BlinkServer;

import java.io.IOException;

import static e2e.org.rockm.blink.support.HttpUtil.fullPath;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class DeleteFeatureTest extends BlinkServerTest {

    @Test
    public void shouldDeleteWithPathParameters() throws Exception {
        KukuDeleteServerStub serverStub = new KukuDeleteServerStub(blinkServer);
        httpClient.execute(new HttpDelete(fullPath("/kukus/542")));
        assertThat(serverStub.idToDelete, is("542"));
    }

    private class KukuDeleteServerStub {
        String idToDelete;

        KukuDeleteServerStub(BlinkServer server) throws IOException {
            server.delete("/kukus/{id}", (req, res) -> idToDelete = req.pathParam("id"));
        }
    }
}
