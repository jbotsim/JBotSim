package io.jbotsim.core.io;

import java.io.*;

/**
 * <p>{@link IOUtils} provides static utility functions for IO manipulations.</p>
 */
public class IOUtils {

    /**
     * <p>Writes the provided data in the {@link OutputStream}.</p>
     *
     * @param outputStream the {@link OutputStream} to which the data must be written
     * @param data the data to be written
     * @throws IOException in case of error when writing in the {@link OutputStream}
     */
    public static void writeStringToStream(OutputStream outputStream, String data) throws IOException {
        outputStream.write(data.getBytes());
    }

    /**
     * <p>Reads the content of the provided {@link InputStream} object and returns it as a String.</p>
     * Note: This method uses a {@link BufferedReader} to read the file.
     * @param in the {@link InputStream} from which data must be read
     * @return a String representing the content
     * @throws IOException in case of error when reading the {@link InputStream}
     */
    public static String readInputStreamContentAsString(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        StringBuilder stringBuilder = new StringBuilder();
        String read;
        while ((read = bufferedReader.readLine()) != null)
            stringBuilder.append(read+'\n');

        return stringBuilder.toString();
    }
}
