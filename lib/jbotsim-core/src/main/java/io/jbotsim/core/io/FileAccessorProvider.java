package io.jbotsim.core.io;

/**
 * This simple interface defines basic methods to get a FileAccessor.
 */
public interface FileAccessorProvider {

    /**
     * Sets the new {@link FileAccessor} to use.
     *
     * @param accessor the new {@link FileAccessor} to use
     */
    void setFileAccessor(FileAccessor accessor);

    /**
     * Provides a {@link FileAccessor}.
     * @return a {@link FileAccessor}. May be null.
     */
    FileAccessor getFileAccessor();
}
