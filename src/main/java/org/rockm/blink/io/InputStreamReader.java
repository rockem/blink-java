package org.rockm.blink.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class InputStreamReader {

    private static final int BUFFER_SIZE = 16384;

    private InputStream requestBody;

    public InputStreamReader(InputStream requestBody) {
        this.requestBody = requestBody;
    }

    public String readAsString() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[BUFFER_SIZE];

        try {
            while ((nRead = requestBody.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            return buffer.toString("UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
