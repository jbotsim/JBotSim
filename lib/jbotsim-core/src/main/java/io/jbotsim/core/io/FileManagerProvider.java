package io.jbotsim.core.io;

/**
 * This simple interface defines basic methods to get a {@link FileManager}.
 */
public interface FileManagerProvider {

    /**
     * Sets the new {@link FileManager} to use.
     *
     * @param fileManager the new {@link FileManager} to use
     */
    void setFileManager(FileManager fileManager);

    /**
     * Provides a {@link FileManager}.
     * @return a {@link FileManager}. May be null.
     */
    FileManager getFileManager();
}
