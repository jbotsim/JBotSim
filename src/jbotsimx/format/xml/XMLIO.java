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

public class XMLIO {
    public static void write(Document document) throws XMLIOException {
        write(new PrintWriter(System.out, true), document);
    }

    public static void write(String filename, Document document) throws XMLIOException {
        try {
            PrintWriter out = new PrintWriter(filename);
            write(out, document);
        } catch (FileNotFoundException e) {
            throw new XMLIOException(e);
        }
    }

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

    public static String writeToString(Document doc) throws XMLIOException {
        StringWriter sw = new StringWriter();
        write(sw, doc);
        return sw.toString();
    }

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

    public static Document readFromString(String input) throws XMLIOException {
        return read(new StringReader(input));
    }

    public static Document read(InputStream input) throws XMLIOException {
        return read(new InputSource(input));
    }

    public static Document read(Reader input) throws XMLIOException {
        return read(new InputSource(input));
    }

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

    public static class XMLIOException extends Exception {
        public XMLIOException(Throwable cause) {
            this("XML generator yields an exception.", cause);
        }

        public XMLIOException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
