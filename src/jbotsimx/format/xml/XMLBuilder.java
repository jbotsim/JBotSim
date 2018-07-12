package jbotsimx.format.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;

import static jbotsimx.format.xml.XMLKeys.JBOTSIM;
import static jbotsimx.format.xml.XMLKeys.VERSION_ATTR;

public abstract class XMLBuilder {
    public static final String VERSION = "1.0";

    private Document document;

    XMLBuilder() throws BuilderException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.newDocument();
            Element root = JBOTSIM.createElement(document);
            VERSION_ATTR.setAttribute(root,VERSION);
            document.appendChild(root);
        } catch (ParserConfigurationException e) {
            throw new BuilderException(e);
        }

    }

    public Document getDocument() {
        return document;
    }

    public void write(String filename) throws BuilderException {
        try {
            XMLIO.write(filename, document);
        } catch (XMLIO.XMLIOException e) {
            throw new BuilderException(e);
        }
    }


    public void write(File file) throws BuilderException {
        try {
            XMLIO.write(file, document);
        } catch (XMLIO.XMLIOException e) {
            throw new BuilderException(e);
        }
    }

    public String writeToString() throws BuilderException {
        try {
            return XMLIO.writeToString(document);
        } catch (XMLIO.XMLIOException e) {
            throw new BuilderException(e);
        }
    }

    public static class BuilderException extends Exception {
        public BuilderException(Throwable cause) {
            this("XML generator yields an exception.", cause);
        }

        public BuilderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
