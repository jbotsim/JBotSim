package io.jbotsim.core.io;

import java.io.*;

public class DefaultFileAccessor implements FileAccessor {
//    @Override
//    public File getFileForName(String filename) {
//        return new File(filename);
//    }

    @Override
    public InputStream getInputStreamForResourceName(String filename) throws IOException {
        return getInputStreamForName(filename);
    }

    @Override
    public InputStream getInputStreamForName(String filename) throws FileNotFoundException {
        return new FileInputStream(new File(filename));
    }

    @Override
    public OutputStream getOutStreamForName(String filename) throws FileNotFoundException {
        return new FileOutputStream(new File(filename));
    }
}
