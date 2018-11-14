package io.jbotsim.io.serialization.topology;

import io.jbotsim.core.Topology;
import io.jbotsim.core.io.FileAccessor;
import io.jbotsim.core.io.Utils;
import io.jbotsim.io.serialization.topology.string.TopologySerializer;

import java.io.*;

/**
 * The {@link FileTopologySerializer} (de)serializes a {@link Topology} object into a file, using parameters found inside
 * the {@link Topology} object.
 */
public class FileTopologySerializer {

    public FileTopologySerializer() {
        // default constructor available without restriction
    }

    // region Export

    /**
     * Saves the current topology (nodes and links) in the given file using the default format.
     *
     * @param topology The topology to be exported
     * @param filename The absolute path to the file
     */
    public void exportToFile(Topology topology, String filename) {
        exportToFile(topology, filename, topology.getTopologySerializer());
    }

    /**
     * Saves the current topology (nodes and links) in the given file.
     *
     * @param topology The topology to be exported
     * @param filename The absolute path to the file
     * @param topologySerializer What format to use
     */
    public void exportToFile(Topology topology, String filename, TopologySerializer topologySerializer) {
        FileAccessor fileAccessor = topology.getFileAccessor();

        TopologySerializer backupSerializer = topology.getTopologySerializer();

        try {
            OutputStream outputStream = fileAccessor.getOutputStreamForName(filename);
            String exportedString = topology.toString();
            topology.setTopologySerializer(topologySerializer);
            Utils.writeStringToStream(outputStream, exportedString);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        } finally {
            if(topology != null)
                topology.setTopologySerializer(backupSerializer);
        }
    }

    // endregion Export

    // region Import
    /**
     * Imports a topology from the given file using the default format.
     *  @param topology The topology object to be populated
     *  @param filename The absolute path to the file
     * @return the updated {@link Topology} object
     */
    public Topology importFromFile(Topology topology, String filename) {
        return importFromFile(topology, filename, topology.getTopologySerializer());
    }

    /**
     * Imports a topology from the given file.
     *  @param topology The topology to be imported
     * @param filename The absolute path to the file
     * @param topologySerializer The format to use
     * @return the updated {@link Topology} object
     */
    public Topology importFromFile(Topology topology, String filename, TopologySerializer topologySerializer) {
        FileAccessor fileAccessor = topology.getFileAccessor();

        TopologySerializer backupSerializer = topology.getTopologySerializer();

        try {
            InputStream file = fileAccessor.getInputStreamForName(filename);
            String s = readInputStreamContentAsString(file);
            topology.setTopologySerializer(topologySerializer);
            topology.fromString(s);
        } catch (IOException e) {
            e.printStackTrace(); // TODO: should be thrown !
        } finally {
            if(topology != null)
                topology.setTopologySerializer(backupSerializer);
        }
        return topology;
    }

    /**
     * Reads the content of the provided {@link InputStream} object and returns it as a String.<br/>
     * Note: This method uses a {@link BufferedReader} to read the file.
     * @param in the {@link InputStream} from which data must be read
     * @return a String representing the content
     * @throws IOException
     */
    protected static String readInputStreamContentAsString(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        StringBuilder stringBuilder = new StringBuilder();
        String read;
        while ((read = bufferedReader.readLine()) != null)
            stringBuilder.append(read+'\n');

        return stringBuilder.toString();
    }

    // endregion Import

}
