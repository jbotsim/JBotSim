package io.jbotsim.format.common;

import io.jbotsim.core.Topology;
import io.jbotsim.core.io.FileAccessor;
import io.jbotsim.format.plain.PlainFormatter;

import java.io.*;
import java.util.regex.Pattern;

public class Format {
    public static Formatter defaultFormatter = new PlainFormatter();

    public static Formatter getDefaultFormatter() {
        return defaultFormatter;
    }

    public static void setDefaultFormatter(Formatter defaultFormatter) {
        Format.defaultFormatter = defaultFormatter;
    }

    /**
     * Saves the current topology (nodes and links) in the given file using the default format.
     *
     * @param topology The topology to be exported
     * @param filename The absolute path to the file
     */
    public static void exportToFile(Topology topology, String filename) {
        exportToFile(topology, filename, getFormatterFor(filename));
    }

    /**
     * Saves the current topology (nodes and links) in the given file.
     *
     * @param topology The topology to be exported
     * @param filename The absolute path to the file
     * @param formatter What format to use
     */
    public static void exportToFile(Topology topology, String filename, Formatter formatter) {
        FileAccessor fileAccessor = topology.getFileAccessor();

        try {
            OutputStream outputStream = fileAccessor.getOutputStreamForName(filename);
            String exportedString = formatter.exportTopology(topology);
            writeStringToStream(outputStream, exportedString);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void writeStringToStream(OutputStream outputStream, String data) throws IOException {
        outputStream.write(data.getBytes());
    }

    /**
     * Imports a topology from the given file using the default format.
     *
     * @param filename The absolute path to the file
     * @return A new topology object containing the corresponding nodes and links
     */
    public static Topology importFromFile(String filename) {
        return importFromFile(filename, getFormatterFor(filename));
    }

    /**
     * Imports a topology from the given file using the default format.
     *
     * @param filename The absolute path to the file
     * @return A new topology object containing the corresponding nodes and links
     */
    public static Topology importFromFile(String filename, Formatter formatter) {
        Topology topology = new Topology();
        importFromFile(topology, filename, formatter);
        return topology;
    }

    /**
     * Imports a topology from the given file using the default format.
     *
     * @param topology The topology object to be populated
     * @param filename The absolute path to the file
     */
    public static void importFromFile(Topology topology, String filename) {
        importFromFile(topology, filename, getFormatterFor(filename));
    }

    /**
     * Imports a topology from the given file.
     *
     * @param topology The topology to be imported
     * @param filename The absolute path to the file
     * @param formatter The format to use
     */
    public static void importFromFile(Topology topology, String filename, Formatter formatter) {
        FileAccessor fileAccessor = topology.getFileAccessor();
        try {
            InputStream file = fileAccessor.getInputStreamForName(filename);
            String s = readInputStreamContentAsString(file);
            formatter.importTopology(topology, s);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /**
     * Returns a string representation of this topology based on the default
     * formatter. The output of this method can be subsequently used to reconstruct
     * a topology based on the same formatter.
     *
     * @param topology The topology to be exported
     * @result A String representation of the topology
     */
    public String exportToString(Topology topology){
        return exportToString(topology, defaultFormatter);
    }

    /**
     * Returns a string representation of this topology based on the specified
     * formatter. The output of this method can be subsequently used to reconstruct
     * a topology based on the same formatter.
     * @param topology The topology to be imported
     * @param formatter The format to use

     */
    public String exportToString(Topology topology, Formatter formatter){
        return formatter.exportTopology(topology);
    }

    /**
     * Imports nodes and wired links from the specified string representation of a
     * topology.
     *
     * @param topology The topology to be imported
     * @param s A string representation of the topology
     */
    public void importFromString(Topology topology, String s){
        importFromString(topology, s, defaultFormatter);
    }

    /**
     * Imports nodes and wired links from the specified string representation of a
     * topology.
     *
     * @param topology The topology to be imported
     * @param s A string representation of the topology
     * @param formatter The format to use
     */
    public void importFromString(Topology topology, String s, Formatter formatter){
        formatter.importTopology(topology, s);
    }


    public static Formatter getFormatterFor(String filename) {
        for(SupportedFormat fmt : SUPPORTED_FORMATS) {
            if (Pattern.matches(fmt.regex, filename)) {
                return fmt.formatter;
            }
        }
        return defaultFormatter;
    }

    private static class SupportedFormat {
        String regex;
        Formatter formatter;

        public SupportedFormat(String regex, Formatter formatter) {
            this.regex = regex;
            this.formatter = formatter;
        }
    }

    private static final SupportedFormat[] SUPPORTED_FORMATS = new SupportedFormat[] {
//        new SupportedFormat(".*\\.xml$", new XMLTopologyFormatter()),
//        new SupportedFormat(".*\\.x?dot$", new DotFormatter())
    };
}
