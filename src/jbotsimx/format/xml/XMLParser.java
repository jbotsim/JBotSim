package jbotsimx.format.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

import static jbotsimx.format.xml.XMLKeys.JBOTSIM;
import static jbotsimx.format.xml.XMLKeys.VERSION_ATTR;

/**
 * Base class for onesused to interpret XML documents that store JBotSim objects like a
 * {@link jbotsim.Topology topology} or a {@link jbotsimx.replay.TraceRecorder trace}.
 *
 * <p>The class proposes several helper functions used to load and interpret a document; IO operations use {@link XMLIO}
 * methods. It parses the root node of the document to determine the version of the XSD schema used to format the
 * document; then it delegates the interpretation of XML element to subclasses.</p>
 *
 * @see #parseRootElement
 */
public abstract class XMLParser {
    private static final String XSD_RESOURCE_PREFIX = "jbotsim-";
    private static final String XSD_RESOURCE_SUFFIX = ".xsd";

    /**
     * The version read from the XML document. This member variable is used to interpret the XML document according
     * to a given version of the XSD schema.
     */
    private String version = null;

    /**
     * Gets the version of the interpreted document.
     *
     * @return the version of underlying {@link Document document}.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Loads and parses the document contained in (@code filename}.
     *
     * @param filename the name of the file to read.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parse(String filename) throws ParserException {
        try {
            parse(XMLIO.read(filename));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    /**
     * Load and parses the document from the given {@link InputStream input stream}.
     *
     * @param input the stream from which the document is read.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parse(InputStream input) throws ParserException {
        parse(new InputSource(input));
    }

    /**
     * Loads and parses the document from the given {@link Reader reader}.
     *
     * @param input the reader with which the document if loaded.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parse(Reader input) throws ParserException {
        parse(new InputSource(input));
    }

    /**
     * Loads and parses the document from the given {@link InputSource SAX source}.
     *
     * @param input the source of the document
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parse(InputSource input) throws ParserException {
        try {
            parse(XMLIO.read(input));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    /**
     * Loads and parses the document from the given {@link String string}.
     *
     * @param input the string containing the document
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parseFromString(String input) throws ParserException {
        try {
            parse(XMLIO.readFromString(input));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    /**
     * Interprets a JBotSim XML document.
     *
     * <p>First the {@link Document document} is validated wrt the JBotSim schema and then it is interpreted to build
     * JBotSim objects. The methods checks if the root element of the document labelled with the {@code jbotsim}
     * keyword and if it has a {@code version} attribute in which case its value is stored (mainly) for subclasses
     * usage.</p>
     *
     * Finally the first child of the {@code jbotsim} element is passed to {@link #parseRootElement(Element)} method
     * that must be implemented by subclasses.
     *
     * @param doc the {@link Document document} that is interpreted
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public void parse(Document doc) throws ParserException {
        Element rootNode = doc.getDocumentElement();

        if (!JBOTSIM.labelsElement(rootNode)) {
            throw new ParserException("invalid node '" + rootNode.getNodeName() + "' where '" +
                    JBOTSIM + "' was expected");
        }

        String version = VERSION_ATTR.getValueFor(rootNode, XMLBuilder.DEFAULT_VERSION);
        try {
            Schema schema = loadSchemaForVersion(version);
            schema.newValidator().validate(new DOMSource(rootNode));
        } catch (SAXParseException e) {
            String msg;
            if (e.getPublicId() == null) {
                msg = "XSD validation error: ";
            } else {
                msg = e.getPublicId() + ":";
                if (e.getLineNumber() >= 1) {
                    msg += e.getLineNumber() + ":";
                    if (e.getColumnNumber() >= 1) {
                        msg += e.getColumnNumber() + ":";
                    }
                }
                msg += "error: ";
            }
            throw new ParserException(msg + e.getMessage());
        } catch (SAXException e) {
            throw new ParserException("unable to validate XML topology:" + e.getMessage());
        } catch (IOException e) {
            throw new ParserException("some IO exception occurs while trying to validate XML topology:" +
                    e.getMessage());
        }

        for (Node n = rootNode.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element) {
                parseRootElement((Element) n);
                break;
            }
        }
    }

    /**
     * Interprets the root element of the document.
     *
     * This element is the first one found under the actual root element of the document. It is interpreted by
     * subclasses according to the {@code version} attribute of the root element.
     *
     * @param element the element to be interpreted.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    public abstract void parseRootElement (Element element) throws ParserException;


    /**
     * Loads the XSD schema that corresponds to the {@code version} of the document.
     *
     * @param version the version of the document.
     * @return the {@link Schema} that must be fulfilled by the document.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    private Schema loadSchemaForVersion(String version) throws ParserException {
        SchemaFactory sF = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        String xsdpath = "/" + getClass().getPackage().getName().replace('.', '/');
        xsdpath += "/" + XSD_RESOURCE_PREFIX + version + XSD_RESOURCE_SUFFIX;
        try {
            InputStream is = getClass().getResourceAsStream(xsdpath);
            return sF.newSchema(new StreamSource(is));
        } catch (SAXException e) {
            String msg;

            if (e instanceof SAXParseException) {
                int c = ((SAXParseException) e).getColumnNumber();
                int l = ((SAXParseException) e).getLineNumber();
                msg = xsdpath + ":" + l + ":" + c + ": error: " + e.getMessage();
            } else {
                msg = "unable to load schema file (" + xsdpath + "):" + e.getMessage();
            }
            throw new ParserException(msg);
        }
    }

    /**
     * A visitor class that may raise a {@link ParserException}.
     * @see #mapElementChildrenOf(Node, ElementVisitor)
     */
    protected interface ElementVisitor {
        void accept(Element element) throws ParserException;
    }

    /**
     * Applies a visitor function to all children elements of a node.
     *
     * @param parent the node for which the children are visited.
     * @param v the visitor function.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    protected static void mapElementChildrenOf(Node parent, XMLTopologyParser.ElementVisitor v) throws ParserException {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element)
                v.accept((Element) n);
        }
    }

    /**
     * Exception raised whenever an error occurs while interpreting the XML document.
     */
    public static class ParserException extends Exception {
        ParserException(Throwable cause) {
            super("XML parser yields an exception.", cause);
        }

        ParserException(String message) {
            super(message);
        }
    }
}
