/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.io.format.xml;

import io.jbotsim.core.io.FileAsStream;
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
    private static FileAsStream fileAsStream;

    public XMLIO(FileAsStream fileAsStream) {
        this.fileAsStream = fileAsStream;
    }

    /**
     * Outputs the given <code>document</code> into the file <code>filename</code>.
     *
     * @param filename destination file
     * @param document to be written in <code>filename</code>
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    public void write(String filename, Document document) throws XMLIOException {
        try {
            OutputStream outputStream = fileAsStream.getOutputStreamForName(filename);
            write(new OutputStreamWriter(outputStream), document);
        } catch (FileNotFoundException e) {
            throw new XMLIOException(e);
        } catch (IOException e) {
            throw new XMLIOException(e);
        }
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
            // we assume that we don't have a DTD 
            doc.setXmlStandalone(true);

            TransformerFactory tFactory =
                    TransformerFactory.newInstance();

            Transformer transformer =
                    tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

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
    public Document read(String filename) throws XMLIOException {
        try {
            InputStream input = fileAsStream.getInputStreamForName(filename);
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
    protected static Document read(InputStream input) throws XMLIOException {
        return read(new InputSource(input));
    }

    /**
     * Reads an XML document using a  {@link Reader}.
     *
     * @param input the input {@link Reader}
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    protected static Document read(Reader input) throws XMLIOException {
        return read(new InputSource(input));
    }

    /**
     * Reads an XML document from a SAX source.
     *
     * @param input the input source
     * @return the XML document as a {@link Document}
     * @throws XMLIOException is thrown if an XML operation fails or if an IO exception occurs.
     */
    protected static Document read(InputSource input) throws XMLIOException{
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
