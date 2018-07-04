package jbotsimx.xml;

import jbotsim.Topology;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.StringContains;
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
    public void emptyFileTest() {
        try {
            loadXMLFile("empty-file.xml");
        } catch (XMLTopologyParser.ParserException e) {
            assertCauseParserExceptionIs(e, SAXParseException.class);
        }
    }

    @Test
    public void missingXMLVersionNumber() {
        missingAttribute("missing-version.xml");
    }

    @Test
    public void twonodes() throws XMLTopologyParser.ParserException {
        Topology T = loadXMLFile("twonodes.xml");
        assertEquals(T.getNodes().size(), 2);
        assertEquals(T.getLinks().size(), 1);
    }

    @Test
    public void missingSrcTest() {
        missingAttribute("missing-src.xml");
    }

    @Test
    public void missingDstTest() {
        missingAttribute("missing-dst.xml");
    }

    @Test
    public void missingGraphTest() {
        testXSDValidationError("missing-graph.xml", "cvc-complex-type.2.4.b");
    }

    @Test
    public void missingClassesTest() {
        testXSDValidationError("missing-classes.xml", "cvc-complex-type.2.4.a");
    }

    @Test
    public void emptyGraphTest() throws XMLTopologyParser.ParserException {
        loadXMLFile("empty-graph.xml");
    }

    @Test
    public void incompleteModel1Test() {
        missingAttribute("incomplete-model-class-1.xml");
    }

    @Test
    public void incompleteModel2Test() {
        missingAttribute("incomplete-model-class-2.xml");
    }

    @Test
    public void incompleteModel3Test() {
        try {
            loadXMLFile("incomplete-model-class-3.xml");
        } catch (XMLTopologyParser.ParserException e) {
            assertCauseParserExceptionIs(e, ClassNotFoundException.class, "nosuchclass");
        }
    }

    private void missingAttribute(String xmlFileName) {
        testXSDValidationError(xmlFileName, "cvc-complex-type.4");
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

    private void testXSDValidationError(String filename, String error) {
        try {
            loadXMLFile(filename);
            thrown.expect(XMLTopologyParser.ParserException.class);
        } catch (XMLTopologyParser.ParserException e) {
            assertXSDValidationError(e, error);
        }
    }

    private void assertXSDValidationError(XMLTopologyParser.ParserException e, String error) {
        assertThat(e.getMessage(), StringContains.containsString("XSD validation error: "+error+" :"));
    }

    private void assertCauseParserExceptionIs(XMLTopologyParser.ParserException e, Class c) {
        assertThat(e.getCause(), IsInstanceOf.instanceOf(c));
    }

    private void assertCauseParserExceptionIs(XMLTopologyParser.ParserException e, Class c, String error) {
        assertNotNull("This is a forwarded exception", e.getCause());
        assertCauseParserExceptionIs(e,c);
        assertThat(e.getCause().getMessage(), StringContains.containsString(error));
    }
}
