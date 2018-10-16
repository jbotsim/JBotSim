package io.jbotsim.format.common;

import io.jbotsim.core.Topology;
import io.jbotsim.format.plain.PlainFormatter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        try (PrintWriter out = new PrintWriter(filename)) {
            out.print(formatter.exportTopology(topology));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
        try {
            String s = new String(Files.readAllBytes(Paths.get(filename)));
            formatter.importTopology(topology, s);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
