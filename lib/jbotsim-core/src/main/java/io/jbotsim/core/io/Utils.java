package io.jbotsim.core.io;

import java.io.IOException;
import java.io.OutputStream;

public class Utils {

    /**
     * Writes the provided data in the {@link OutputStream}.
     *
     * @param outputStream the {@link OutputStream} to which the data must be written
     * @param data the data to be written
     * @throws IOException
     */
    public static void writeStringToStream(OutputStream outputStream, String data) throws IOException {
        outputStream.write(data.getBytes());
    }
}
