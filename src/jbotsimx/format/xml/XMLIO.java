package jbotsimx.format.xml;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Helper class used for IO operations on JBotSim xml files. The read operations only ensure that the input files
 * follow XML syntax; the validation of the document must be realized elsewhere.
 */
public class XMLIO {
    /**
     * Outputs the given <code>document</code> on standard output.
     *
     * @param document writen on standard output
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static void write(Document document) throws XMLIOException {
        write(new PrintWriter(System.out, true), document);
    }

    /**
     * Outputs the given <code>document</code> into the file <code>filename</code>.
     *
     * @param filename destination file
     * @param document to be written in <code>filename</code>
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static void write(String filename, Document document) throws XMLIOException {
        try {
            PrintWriter out = new PrintWriter(filename);
            write(out, document);
        } catch (FileNotFoundException e) {
            throw new XMLIOException(e);
        }
    }

    /**
     * Outputs the given <code>document</code> to a file descriptor.
     *
     * @param file descriptor of the destination file
     * @param document to be written in <code>filename</code>
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static void write(File file, Document document) throws XMLIOException {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(file));
            write(out, document);
        } catch (FileNotFoundException e) {
            throw new XMLIOException(e);
        } catch (IOException e) {
            throw new XMLIOException(e);
        }
    }

    /**
     * Outputs the given <code>document</code> to a stream.
     *
     * @param ostream the output stream
     * @param document to be written in <code>filename</code>
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static void write(OutputStream ostream, Document document) throws XMLIOException {
        write(new OutputStreamWriter(ostream), document);
    }

    /**
     * Outputs the given <code>document</code> using the given {@link Writer}.
     *
     * @param out the Writer object
     * @param doc the document that has to be written using the {@link Writer}.
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static void write(Writer out, Document doc) throws XMLIOException {
        try {
            TransformerFactory tFactory =
                    TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);

            Transformer transformer =
                    tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new XMLIOException("XML generator yield an exception.", e);
        }
    }

    /**
     * Outputs the given <code>document</code> into a {@link String}.
     *
     * @param doc the document compiled into a {@link String}.
     * @return the document <code>doc</code>as a {@link String}.
     * @throws XMLIOException is thrown if an XML operation fails.
     */
    public static String writeToString(Document doc) throws XMLIOException {
        StringWriter sw = new StringWriter();
        write(sw, doc);
        return sw.toString();
    }

    /**
     * Reads an XML document from the file specified by <code>filename</code>.
     *
     * @param filename the input file name.
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document read(String filename) throws XMLIOException {
        try {
            InputStream input = new FileInputStream(filename);
            Document result = read(input);
            input.close();
            return result;
        } catch (XMLIOException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLIOException(e);
        }
    }

    /**
     * Reads an XML document from a <code>file</code> descriptor.
     *
     * @param file the descriptor of the input file
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document read(File file) throws XMLIOException {
        try {
            InputStream input = new FileInputStream(file);
            Document result = read(input);
            input.close();
            return result;
        } catch (XMLIOException e) {
            throw e;
        } catch (Exception e) {
            throw new XMLIOException(e);
        }
    }

    /**
     * Reads an XML document from a {@link String}.
     *
     * @param input the string that contains the text of the XML document
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document readFromString(String input) throws XMLIOException {
        return read(new StringReader(input));
    }

    /**
     * Reads an XML document from an stream.
     *
     * @param input the input stream
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document read(InputStream input) throws XMLIOException {
        return read(new InputSource(input));
    }

    /**
     * Reads an XML document using a  {@link Reader}.
     *
     * @param input the input {@link Reader}
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document read(Reader input) throws XMLIOException {
        return read(new InputSource(input));
    }

    /**
     * Reads an XML document from a SAX source.
     *
     * @param input the input source
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public static Document read(InputSource input) throws XMLIOException{
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.normalizeDocument();
            return doc;
        } catch (Exception e) {
            throw new XMLIOException(e);
        }
    }

    /**
     * Exception raised when an IO exception occurs or if the underlying XML reader yields an error.
     */
    public static class XMLIOException extends Exception {
        public XMLIOException(Throwable cause) {
            this("XML generator yields an exception.", cause);
        }

        public XMLIOException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
