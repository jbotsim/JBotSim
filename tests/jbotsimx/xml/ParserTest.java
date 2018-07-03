package jbotsimx.xml;

import jbotsim.Topology;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.IsNull;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.rules.ExpectedException;
import org.xml.sax.SAXParseException;

import java.io.InputStream;

public class ParserTest {
    private static final String TEST_RC_ROOT = "/xmlinputs/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void empty() {
        try {
            loadXMLFile("empty.xml");
        } catch (XMLTopologyParser.ParserException e) {
            assertThat(e.getCause(), new IsInstanceOf(SAXParseException.class));
        }
    }

    @Test
    public void missingXMLVersionNumber() {
        try {
            loadXMLFile("missing-version.xml");
        } catch (XMLTopologyParser.ParserException e) {
            assertThat(e.getCause(), new IsNull<>());
        }
    }

    @Test
    public void twonodes() throws XMLTopologyParser.ParserException {
        Topology T = loadXMLFile("twonodes.xml");
    }

    private Topology loadXMLFile(String xmlFileName) throws XMLTopologyParser.ParserException {
        Topology T = new Topology();
        XMLTopologyParser tpp = new XMLTopologyParser(T);
        String resource = TEST_RC_ROOT + xmlFileName;
        InputStream is = getClass().getResourceAsStream(resource);
        if (is == null) {
            throw new XMLTopologyParser.ParserException("unable to open/locate resource: " + resource);
        }
        tpp.parse(is);

        return T;
    }
}
