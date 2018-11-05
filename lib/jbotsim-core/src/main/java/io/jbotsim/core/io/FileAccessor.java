package io.jbotsim.core.io;

import java.io.*;

/**
 * The interface defines methods to be implemented for file access.
 *
 * Specific implementation should be provided for each platform.
 */
public interface FileAccessor {


    /**
     * Returns an {@link InputStream} object corresponding to the provided file name
     * @param filename the name of the file to open
     * @return a {@link InputStream}
     */
    InputStream getInputStreamForName(String filename) throws IOException;

    /**
     * Returns an {@link OutputStream} object corresponding to the provided file name
     * @param filename the name of the file to open
     * @return a {@link OutputStream}
     */
    OutputStream getOutputStreamForName(String filename) throws IOException;

}
