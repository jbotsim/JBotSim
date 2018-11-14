package io.jbotsim.dynamicity.movement.trace;

/**
 * The {@link TraceFileWriter} is store a sequence of {@link TraceEvent}s and store them in a file.
 */
public interface TraceFileWriter {

    /**
     * Add a {@link TraceEvent} to the set of events.
     * @param e the event to add
     */
    void addTraceEvent(TraceEvent e);

    /**
     * Write the current trace content to the corresponding file
     * @param filename the name of the destination file
     * @throws Exception
     */
    void write(String filename) throws Exception;
}
