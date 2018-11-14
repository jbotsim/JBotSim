package io.jbotsim.dynamicity.movement.trace;

/**
 * The {@link TraceFileReader} is able to parse a file content and put it into a {@link TracePlayer}.
 */
public interface TraceFileReader {

    /**
     * Parse the content of the file named filename and put it in the provided {@link TracePlayer}.
     * @param filename the filename of the source file
     * @param tracePlayer the {@link TracePlayer} which must be populated
     * @throws Exception
     */
    void parse(String filename, TracePlayer tracePlayer) throws Exception;
}
