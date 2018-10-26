package io.jbotsim.core.io;

import java.io.*;

/**
 * The interface defines methods to be implemented for file access.
 *
 * Specific implementation should be provided for each platform.
 */
public interface FileAccessor {


    /**
     * Returns a {@link File} object corresponding to the provided file name
     * @param filename the name of the file to open
     * @return a {@link File}
     */
//    File getFileForName(String filename);
    InputStream getInputStreamForResourceName(String filename) throws IOException;
    
    InputStream getInputStreamForName(String filename) throws IOException;

    OutputStream getOutStreamForName(String filename) throws IOException;

}
