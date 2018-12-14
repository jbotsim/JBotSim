package io.jbotsim.core.io;

import java.io.*;

/**
 * The {@link FileManager} provides the following methods:
 * <ul>
 *     <li>{@link #read(String)}: reads the content of a file</li>
 *     <li>{@link #write(String, String)}: writes some data in a file</li>
 * </ul>
 */
public class FileManager implements FileAsStream {

    @Override
    public InputStream getInputStreamForName(String filename) throws IOException {
        return new FileInputStream(new File(filename));
    }

    @Override
    public OutputStream getOutputStreamForName(String filename) throws IOException {
        return new FileOutputStream(new File(filename));
    }


    /**
     * Saves the provided data to the given file.
     *
     * @param filename The path to the file
     * @param data The data to be written
     */
    public void write(String filename, String data) {

        try {
            OutputStream outputStream = getOutputStreamForName(filename);
            IOUtils.writeStringToStream(outputStream, data);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        }
    }

    /**
     * Read the content of the file specified by the filename.
     *
     * @param filename The absolute path to the file
     * @return the content of the file, as a {@link String}. Can be null.
     */
    public String read(String filename) {

        try {
            InputStream file = getInputStreamForName(filename);
            return IOUtils.readInputStreamContentAsString(file);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        }
        return null;
    }

}
