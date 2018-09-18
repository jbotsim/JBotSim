package jbotsimx.format.xml;

import jbotsim.Color;
import jbotsim.Node;
import jbotsim.Topology;
import org.hamcrest.core.IsNull;
import org.hamcrest.core.IsInstanceOf;
import org.hamcrest.core.StringContains;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.rules.ExpectedException;

import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;

public class ParserTest {
    private static final String TEST_RC_ROOT = "/xmlinputs/";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void emptyFileTest() {
        try {
            loadXMLFile("empty-file.xml");
        } catch (XMLParser.ParserException e) {
            assertCauseParserExceptionIs(e, XMLIO.XMLIOException.class);
        }
    }

    @Test
    public void missingXMLVersionNumber() {
        missingAttribute("missing-version.xml");
    }

    @Test
    public void twonodes() throws XMLParser.ParserException {
        Topology T = loadXMLFile("twonodes.xml");
        assertEquals(T.getNodes().size(), 2);
        assertEquals(T.getLinks().size(), 1);
    }

    @Test
    public void noDefaultConstructorTest() {
        try {
            loadXMLFile("no-default-constructor.xml");
            thrown.expect(XMLParser.ParserException.class);
        } catch (XMLParser.ParserException e) {
            assertCauseParserExceptionIs(e, NoSuchMethodException.class);
        }
    }

    @Test
    public void nodeRedeclaredTest() throws XMLParser.ParserException {
        thrown.expect(XMLParser.ParserException.class);
        thrown.expectCause(IsNull.nullValue(Throwable.class));
        thrown.expectMessage("node identifier is already used: 2");
        loadXMLFile("node-redeclared.xml");
    }

    @Test
    public void namedNodeModelTest() throws XMLParser.ParserException {
        Topology tp = loadXMLFile("named-node-models.xml");
        ArrayList<Node> nodes = new ArrayList<>(tp.getNodes());
        int nbNodes = nodes.size();
        assertEquals(3, nbNodes);
        Class foo = tp.getNodeModel("foo");
        assertNotNull("model should have a class of nodes called foo", foo);
        Class bar = tp.getNodeModel("foo");
        assertNotNull("model should have a class of nodes called bar", bar);

        for (Node n : nodes) {
            if (n  instanceof DummyNode1) {
                nodes.remove(n);
                break;
            }
        }
        assertEquals("should have found an instance of DummyNode1", nodes.size(), nbNodes-1);

        for (Node n : nodes) {
            if (n  instanceof DummyNode2) {
                nodes.remove(n);
                break;
            }
        }
        assertEquals("should have found an instance of DummyNode1", nodes.size(), nbNodes-2);
        assertEquals("should have found an instance of Node", nodes.size(), 1);
    }

    @Test
    public void unknownModelClass() {
        testXSDValidationError("unknown-model-class.xml", "cvc-complex-type.2.4.a");
    }

    @Test
    public void badSrcNodeTest() throws XMLParser.ParserException {
        thrown.expect(XMLParser.ParserException.class);
        thrown.expectCause(IsNull.nullValue(Throwable.class));
        thrown.expectMessage("unknown node: 1");
        loadXMLFile("bad-src-node.xml");
    }

    @Test
    public void badDstNodeTest() throws XMLParser.ParserException {
        thrown.expect(XMLParser.ParserException.class);
        thrown.expectCause(IsNull.nullValue(Throwable.class));
        thrown.expectMessage("unknown node: 1");
        loadXMLFile("bad-dst-node.xml");
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
    public void missingGraphTest() throws XMLParser.ParserException {
        loadXMLFile("missing-graph.xml");
    }

    @Test
    public void missingClassesTest() throws XMLParser.ParserException {
        loadXMLFile("missing-classes.xml");
    }

    @Test
    public void emptyGraphTest() throws XMLParser.ParserException {
        loadXMLFile("empty-graph.xml");
    }

    @Test
    public void incompleteModelClass1Test() {
        missingAttribute("incomplete-model-class-1.xml");
    }

    @Test
    public void incompleteModelClass2Test() {
        missingAttribute("incomplete-model-class-2.xml");
    }

    @Test
    public void incompleteModelClass3Test() {
        try {
            loadXMLFile("incomplete-model-class-3.xml");
            thrown.expect(XMLParser.ParserException.class);
        } catch (XMLParser.ParserException e) {
            assertCauseParserExceptionIs(e, ClassNotFoundException.class, "nosuchclass");
        }
    }

    @Test
    public void invalidClockClassTest() {
        invalidClassTest("invalid-clock-class.xml");
    }

    @Test
    public void invalidSchedulerClassTest() {
        invalidClassTest("invalid-scheduler-class.xml");
    }

    @Test
    public void invalidLinkResolverClassTest() {
        invalidClassTest("invalid-link-resolver-class.xml");
    }

    @Test
    public void invalidNodeClassTest() {
        invalidClassTest("invalid-node-class.xml");
    }

    @Test
    public void invalidMessageEngineClassTest() {
        invalidClassTest("invalid-message-engine-class.xml");
    }

    @Test
    public void invalidDoubleTest() {
        testXSDValidationError("invalid-double.xml", "cvc-datatype-valid.1.2.1");
    }

    @Test
    public void invalidIntegerTest() {
        testXSDValidationError("invalid-double.xml", "cvc-datatype-valid.1.2.1");
    }

    //
    // Helper methods
    //

    private void invalidClassTest(String filename) {
        try {
            loadXMLFile(filename);
            thrown.expect(XMLParser.ParserException.class);
        } catch (XMLParser.ParserException e) {
            assertCauseParserExceptionIs(e, ClassCastException.class, "java.lang.Object");
        }
    }

    private void missingAttribute(String xmlFileName) {
        testXSDValidationError(xmlFileName, "cvc-complex-type.4");
    }

    public static Topology loadXMLFile(String xmlFileName) throws XMLParser.ParserException {
        Topology T = new Topology();
        XMLTopologyParser tpp = new XMLTopologyParser(T);
        String resource = TEST_RC_ROOT + xmlFileName;
        InputStream is = ParserTest.class.getResourceAsStream(resource);
        if (is == null) {
            throw new XMLParser.ParserException("unable to open/locate resource: " + resource);
        }
        tpp.parse(is);

        return T;
    }

    private void testXSDValidationError(String filename, String error) {
        try {
            loadXMLFile(filename);
            thrown.expect(XMLParser.ParserException.class);
        } catch (XMLParser.ParserException e) {
            assertXSDValidationError(e, error);
        }
    }

    private void assertXSDValidationError(XMLParser.ParserException e, String error) {
        assertThat(e.getMessage(), StringContains.containsString("XSD validation error: "+error+" :"));
    }

    private void assertCauseParserExceptionIs(XMLParser.ParserException e, Class c) {
        assertThat(e.getCause(), IsInstanceOf.instanceOf(c));
    }

    private void assertCauseParserExceptionIs(XMLParser.ParserException e, Class c, String error) {
        assertNotNull("This is a forwarded exception", e.getCause());
        assertCauseParserExceptionIs(e,c);
        assertThat(e.getCause().getMessage(), StringContains.containsString(error));
    }

    public static class NoDefaultConstructor extends Node {
        NoDefaultConstructor(boolean dummy) {}
    }

    public static class DummyNode1 extends Node {
        @Override
        public void onStart() {
            super.onStart();
            setColor(Color.BLUE);
        }
    }

    public static class DummyNode2 extends Node {
        @Override
        public void onStart() {
            super.onStart();
            setColor(Color.RED);
        }
    }
}
