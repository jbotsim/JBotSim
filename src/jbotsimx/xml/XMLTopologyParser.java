package jbotsimx.xml;

import jbotsim.*;
import jbotsimx.topology.TopologyGeneratorFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static jbotsim.Link.Mode.WIRED;
import static jbotsim.Link.Type.DIRECTED;
import static jbotsim.Link.Type.UNDIRECTED;
import static jbotsimx.xml.XMLTopologyKeys.*;

public class XMLTopologyParser {
    public static final String DEFAULT_VERSION = XMLTopologyBuilder.VERSION;
    private static final String XSD_RESOURCE_PREFIX = "topology-";
    private static final String XSD_RESOURCE_SUFFIX = ".xsd";

    private DocumentBuilder builder;
    private Topology tp;

    public XMLTopologyParser(Topology tp) throws ParserException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            builder = dbf.newDocumentBuilder();
            this.tp = tp;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    public void parse(String filename) throws ParserException {
        try {
            InputStream input = new FileInputStream(filename);
            parse(input);
            input.close();
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
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
            Document doc = builder.parse(input);
            doc.normalizeDocument();
            Element rootNode = doc.getDocumentElement();
            parseTopology(rootNode);
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private void parseTopology(Element topo) throws ParserException {
        checkElement(topo, XMLTopologyKeys.TOPOLOGY);

        String version = VERSION_ATTR.getValueFor(topo, DEFAULT_VERSION);
        try {
            Schema schema = loadSchemaForVersion(version);
            schema.newValidator().validate(new DOMSource(topo));
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

        if (WIRELESS_ENABLED_ATTR.isAttributeOf(topo)) {
            if (WIRELESS_ENABLED_ATTR.getValueFor(topo, Boolean::valueOf)) {
                tp.enableWireless();
            } else
                tp.disableWireless();
        }

        if (CLOCK_SPEED_ATTR.isAttributeOf(topo)) {
            tp.setClockSpeed(CLOCK_SPEED_ATTR.getValueFor(topo, Integer::valueOf));
        }

        int width = WIDTH_ATTR.getValueFor(topo, Topology.DEFAULT_WIDTH);
        int height = HEIGHT_ATTR.getValueFor(topo, Topology.DEFAULT_HEIGHT);
        double sr = SENSING_RANGE_ATTR.getValueFor(topo, Topology.DEFAULT_SENSING_RANGE);
        double cr = COMMUNICATION_RANGE_ATTR.getValueFor(topo, Topology.DEFAULT_COMMUNICATION_RANGE);

        tp.setDimensions(width, height);
        tp.setSensingRange(sr);
        tp.setCommunicationRange(cr);

        mapElementChildrenOf(topo, e -> {
            if (CLASSES.labelsElement(e))
                mapElementChildrenOf(e, this::parseClass);
            else if (GRAPH.labelsElement(e))
                parseGraphElement(e);
        });
    }

    private void parseClass(Element C) throws ParserException {
        String ID = IDENTIFIER_ATTR.getValueFor(C, "default");
        String className = CLASS_ATTR.getValueFor(C, (String) null);
        if (className == null) // this case should be handled by XSD validator
            throw new ParserException("missing 'class' attribute in element:" + C.getNodeName());

        try {
            Class<?> c = Class.forName(className);
            if (LINK_RESOLVER.labelsElement(C)) {
                Class<? extends LinkResolver> cls = c.asSubclass(LinkResolver.class);
                tp.setLinkResolver(cls.getConstructor().newInstance());
            } else if (MESSAGE_ENGINE.labelsElement(C)) {
                Class<? extends MessageEngine> cls = c.asSubclass(MessageEngine.class);
                tp.setMessageEngine(cls.getConstructor().newInstance());
            } else if (SCHEDULER.labelsElement(C)) {
                Class<? extends Scheduler> cls = c.asSubclass(Scheduler.class);
                tp.setScheduler(cls.getConstructor().newInstance());
            } else if (CLOCKCLASS.labelsElement(C)) {
                Class<? extends Clock> cls = c.asSubclass(Clock.class);
                tp.setClockModel(cls);
            } else if (NODECLASS.labelsElement(C)) {
                Class<? extends jbotsim.Node> cls = c.asSubclass(jbotsim.Node.class);
                if ("default".equals(ID))
                    tp.setDefaultNodeModel(cls);
                else
                    tp.setNodeModel(ID, cls);
            } else {
                // this case should be handled by XSD validator
                throw new ParserException("unknown model class: " + C.getNodeName());
            }
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }

    private void parseGraphElement(Element ge) throws ParserException {
        HashMap<String, jbotsim.Node> nodeids = new HashMap<>();
        mapElementChildrenOf(ge, e -> {
            if (NODE.labelsElement(e))
                parseNode(e, nodeids);
            else if (LINK.labelsElement(e))
                parseLink(e, nodeids);
            else if (GENERATOR.labelsElement(e))
                parseGenerators(e);
            else throw new EnumConstantNotPresentException(XMLTopologyKeys.class, e.getTagName());
        });
    }

    private Color parseColor(Element e, Color default_color) {
        Color result = default_color;
        String color = COLOR_ATTR.getValueFor(e, (String) null);
        if (color != null && !color.equals("None")) {
            result = new Color(Integer.parseUnsignedInt(color, 16));
        }
        return result;
    }

    private Class<? extends jbotsim.Node> getNodeClass(String className) throws ParserException {
        try {
            Class<? extends jbotsim.Node> nodeClass;
            if ("default".equals(className) || className == null) {
                nodeClass = tp.getDefaultNodeModel();
                if (nodeClass == null) {
                    nodeClass = jbotsim.Node.class;
                }
            } else {
                nodeClass = tp.getNodeModel(className);
            }
            if (nodeClass == null) {
                nodeClass = Class.forName(className).asSubclass(jbotsim.Node.class);
            }
            return nodeClass;
        } catch (ClassNotFoundException e) {
            throw new ParserException(e);
        }
    }

    private void parseNode(Element ne, Map<String, jbotsim.Node> nodeids) throws ParserException {
        jbotsim.Node n;

        try {
            Class<? extends jbotsim.Node> nodeClass = tp.getDefaultNodeModel();

            if (CLASS_ATTR.isAttributeOf(ne)) {
                nodeClass = getNodeClass(CLASS_ATTR.getValueFor(ne));
            }
            if (nodeClass == null) {
                nodeClass = jbotsim.Node.class;
            }
            n = nodeClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new ParserException(ex);
        }

        if (!IDENTIFIER_ATTR.isAttributeOf(ne))
            throw new ParserException("node identifier is missing");
        String id = IDENTIFIER_ATTR.getValueFor(ne);
        if (nodeids.containsKey(id))
            throw new ParserException("node identifier is already used: " + id);
        nodeids.put(id, n);

        n.setColor(parseColor(ne, jbotsim.Node.DEFAULT_COLOR));

        int size = SIZE_ATTR.getValueFor(ne, jbotsim.Node.DEFAULT_SIZE);
        n.setSize(size);
        double cr = COMMUNICATION_RANGE_ATTR.getValueFor(ne, tp.getCommunicationRange());
        n.setCommunicationRange(cr);
        double sr = SENSING_RANGE_ATTR.getValueFor(ne, tp.getSensingRange());
        n.setSensingRange(sr);
        double dir = DIRECTION_ATTR.getValueFor(ne, 0.0);
        n.setDirection(dir);
        double x = LOCATION_X_ATTR.getValueFor(ne, -1.0);
        double y = LOCATION_Y_ATTR.getValueFor(ne, -1.0);
        double z = LOCATION_Z_ATTR.getValueFor(ne, 0.0);
        n.setLocation(x, y, z);

        tp.addNode(n);
    }

    private void parseLink(Element e, Map<String, jbotsim.Node> nodeids) throws ParserException {
        Link.Type type = DIRECTED_ATTR.getValueFor(e, false) ? DIRECTED : UNDIRECTED;

        jbotsim.Node src = nodeids.get(SOURCE_ATTR.getValueFor(e));
        if (src == null)
            throw new ParserException("unknown node: " + SOURCE_ATTR.getValueFor(e));
        jbotsim.Node dst = nodeids.get(DESTINATION_ATTR.getValueFor(e));
        if (dst == null)
            throw new ParserException("unknown node: " + DESTINATION_ATTR.getValueFor(e));
        Link l = new Link(src, dst, type, WIRED);

        l.setWidth(WIDTH_ATTR.getValueFor(e, Link.DEFAULT_WIDTH));
        l.setColor(parseColor(e, Link.DEFAULT_COLOR));
        tp.addLink(l, true);
    }

    private void parseGeneratorAttributes(Element e, TopologyGeneratorFactory tgf) throws ParserException {
        tgf.setAbsoluteCoords(ABSOLUTE_COORDS_ATTR.getValueFor(e, false));
        tgf.setX(LOCATION_X_ATTR.getValueFor(e, 0.0));
        tgf.setY(LOCATION_Y_ATTR.getValueFor(e, 0.0));
        tgf.setOrder(ORDER_ATTR.getValueFor(e, 1));
        tgf.setWidth(WIDTH_ATTR.getValueFor(e, 1.0));
        tgf.setHeight(HEIGHT_ATTR.getValueFor(e, 1.0));
        tgf.setWired(WIRED_ATTR.getValueFor(e, false));
        tgf.setWirelessEnabled(WIRELESS_ENABLED_ATTR.getValueFor(e, true));
        tgf.setDirected(DIRECTED_ATTR.getValueFor(e, false));
        tgf.setNodeClass(getNodeClass(NODECLASS_ATTR.getValueFor(e, "default")));
    }

    private void parseLineGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);
        boolean horizontal = HORIZONTAL_ATTR.getValueFor(e,true);
        tgf.newLine(horizontal).generate(tp);
    }

    private void parseRingGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);
        tgf.newRing().generate(tp);
    }

    private void parseGridGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);

        TopologyGeneratorFactory.Generator gen;
        if (X_ORDER_ATTR.isAttributeOf(e)) {
            gen = tgf.newGrid(X_ORDER_ATTR.getValueFor(e, 1),Y_ORDER_ATTR.getValueFor(e, 1));
        } else {
            gen = tgf.newSquareGrid();
        }
        gen.generate(tp);
    }

    private void parseTorusGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);

        TopologyGeneratorFactory.Generator gen;
        if (X_ORDER_ATTR.isAttributeOf(e)) {
            gen = tgf.newTorus(X_ORDER_ATTR.getValueFor(e, 1),Y_ORDER_ATTR.getValueFor(e, 1));
        } else {
            gen = tgf.newTorus();
        }
        gen.generate(tp);
    }

    private void parseKNGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);
        tgf.newKN().generate(tp);
    }

    private void parseRndLocationsGenerator(Element e) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tgf);
        tgf.newRandomLocations().generate(tp);
    }

    private void parseGenerators(Element ge) throws ParserException {
        mapElementChildrenOf(ge, e -> {
            if (LINE.labelsElement(e))
                parseLineGenerator(e);
            else if (RING.labelsElement(e))
                parseRingGenerator(e);
            else if (GRID.labelsElement(e))
                parseGridGenerator(e);
            else if (TORUS.labelsElement(e))
                parseTorusGenerator(e);
            else if (KN.labelsElement(e))
                parseKNGenerator(e);
            else if (RANDOM_LOCATIONS.labelsElement(e))
                parseRndLocationsGenerator(e);
            else
                throw new EnumConstantNotPresentException(XMLTopologyKeys.class, e.getTagName());
        });
    }

    private void mapElementChildrenOf(Node parent, ElementVisitor v) throws ParserException {
        for (Node n = parent.getFirstChild(); n != null; n = n.getNextSibling()) {
            if (n instanceof Element)
                v.accept((Element) n);
        }
    }

    private void mapElementChildrenWithName(Node parent, XMLTopologyKeys name, ElementVisitor v) throws ParserException {
        mapElementChildrenOf(parent, e -> {
            if (name.labelsElement(e))
                v.accept(e);
        });
    }

    private void checkElement(Element e, XMLTopologyKeys key) throws ParserException {
        if (!key.equals(e.getNodeName()))
            throw new ParserException("invalid node '" + e.getNodeName() + "' where '" + key + "' was expected");
    }

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

    private interface ElementVisitor {
        void accept(Element element) throws ParserException;
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
