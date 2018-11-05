package io.jbotsim.core.io;

import java.io.*;

public class DefaultFileAccessor implements FileAccessor {

    @Override
    public InputStream getInputStreamForName(String filename) throws FileNotFoundException {
        return new FileInputStream(new File(filename));
    }

    @Override
    public OutputStream getOutputStreamForName(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(filename));
    }

}
