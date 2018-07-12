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

public abstract class XMLParser {
    public static final String DEFAULT_VERSION = XMLBuilder.VERSION;
    private static final String XSD_RESOURCE_PREFIX = "jbotsim-";
    private static final String XSD_RESOURCE_SUFFIX = ".xsd";

    private String version = null;

    public String getVersion() {
        return version;
    }

    public void parse(String filename) throws ParserException {
        try {
            parse(XMLIO.read(filename));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    public void parse(InputStream input) throws ParserException {
        parse(new InputSource(input));
    }

    public void parse(Reader input) throws ParserException {
        parse(new InputSource(input));
    }

    public void parse(InputSource input) throws ParserException {
        try {
            parse(XMLIO.read(input));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    public void parse(Document doc) throws ParserException {
        Element rootNode = doc.getDocumentElement();

        if (!JBOTSIM.labelsElement(rootNode)) {
            throw new ParserException("invalid node '" + rootNode.getNodeName() + "' where '" +
                    JBOTSIM + "' was expected");
        }

        String version = VERSION_ATTR.getValueFor(rootNode, DEFAULT_VERSION);
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

    public abstract void parseRootElement (Element element) throws ParserException;


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

    protected interface ElementVisitor {
        void accept(Element element) throws ParserException;
    }

    protected static void mapElementChildrenOf(Node parent, XMLTopologyParser.ElementVisitor v) throws ParserException {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element)
                v.accept((Element) n);
        }
    }

    public static class ParserException extends Exception {
        ParserException(Throwable cause) {
            super("XML parser yields an exception.", cause);
        }

        ParserException(String message) {
            super(message);
        }
    }
}
