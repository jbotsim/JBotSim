package jbotsimx.xml;

import jbotsim.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import static jbotsim.Link.Mode.WIRED;
import static jbotsim.Link.Type.DIRECTED;
import static jbotsim.Link.Type.UNDIRECTED;
import static jbotsimx.xml.XMLTopologyKeys.*;

public class XMLTopologyParser {
    public static final String DEFAULT_VERSION = "1.0";

    private DocumentBuilder builder;
    private Topology tp;
    private String version = DEFAULT_VERSION;

    public XMLTopologyParser(Topology tp) throws ParserException {
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
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

        version = VERSION_ATTR.getValueFor(topo, DEFAULT_VERSION);

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
        if (className == null)
            throw new ParserException("missing 'class' attribute in element:" + C.getNodeName());

        try {
            Class c = getClass().getClassLoader().loadClass(className);
            if (LINK_RESOLVER.labelsElement(C)) {
                tp.setLinkResolver((LinkResolver) c.getConstructor().newInstance());
            } else if (MESSAGE_ENGINE.labelsElement(C)) {
                tp.setMessageEngine((MessageEngine) c.getConstructor().newInstance());
            } else if (SCHEDULER.labelsElement(C)) {
                tp.setScheduler((Scheduler) c.getConstructor().newInstance());
            } else if (CLOCKCLASS.labelsElement(C)) {
                tp.setClockModel(c);
            } else if (NODECLASS.labelsElement(C)) {
                if ("default".equals(ID))
                    tp.setDefaultNodeModel(c);
                else
                    tp.setNodeModel(ID, c);
            } else {
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
        mapElementChildrenWithName(ge, NODE, e -> {
            parseNode(e, nodeids);
        });
        mapElementChildrenWithName(ge, LINK, e -> {
            parseLink(e, nodeids);
        });
    }

    private Color parseColor(Element e, Color default_color) {
        Color result = default_color;
        String color = COLOR_ATTR.getValueFor(e, (String) null);
        if (color != null && ! color.equals("None")) {
            result = new Color(Integer.parseUnsignedInt(color, 16));
        }
        return result;
    }

    private void parseNode(Element ne, Map<String, jbotsim.Node> nodeids) throws ParserException {
        jbotsim.Node n;
        Class nodeClass = tp.getDefaultNodeModel();

        try {
            if (CLASS_ATTR.isAttributeOf(ne)) {
                    String className = CLASS_ATTR.getValueFor(ne);
                    nodeClass = getClass().getClassLoader().loadClass(className);
            }

            n = (jbotsim.Node) nodeClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException ex) {
            throw new ParserException(ex);
        }

        if (!IDENTIFIER_ATTR.isAttributeOf(ne))
            throw new ParserException("node identifier is missing");
        nodeids.put(IDENTIFIER_ATTR.getValueFor(ne), n);

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

    private void parseLink(Element e, Map<String,jbotsim.Node> nodeids) throws ParserException {
        Link.Type type = DIRECTED_ATTR.getValueFor(e,false) ? DIRECTED : UNDIRECTED;

        jbotsim.Node src = nodeids.get (SOURCE_ATTR.getValueFor(e));
        if (src == null)
            throw new ParserException("unknown source node:" + SOURCE_ATTR.getValueFor(e));
        jbotsim.Node dst = nodeids.get (DESTINATION_ATTR.getValueFor(e));
        if (dst == null)
            throw new ParserException("unknown source node:" + DESTINATION_ATTR.getValueFor(e));
        Link l = new Link(src, dst, type, WIRED);

        l.setWidth(WIDTH_ATTR.getValueFor(e, Link.DEFAULT_WIDTH));
        l.setColor(parseColor(e, Link.DEFAULT_COLOR));
        tp.addLink(l,true);
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

    private interface ElementVisitor {
        void accept(Element element) throws ParserException;
    }

    public static class ParserException extends Exception {
        ParserException(Throwable cause) {
            this("XML parser yields an exception.", cause);
        }

        ParserException(String message, Throwable cause) {
            super(message, cause);
        }

        ParserException(String message) {
            super(message);
        }
    }
}
