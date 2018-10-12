package io.jbotsim.format.xml;

import io.jbotsim.*;
import io.jbotsim.topology.TopologyGeneratorFactory;
import org.w3c.dom.Element;

import java.util.HashMap;
import java.util.Map;

import static io.jbotsim.Link.Mode.WIRED;
import static io.jbotsim.Link.Type.DIRECTED;
import static io.jbotsim.Link.Type.UNDIRECTED;
import static io.jbotsim.format.xml.XMLKeys.*;

/**
 * Interpreter for an {@link org.w3c.dom.Document XML document} that represents a {@link Topology}.
 *
 * This class is used through its parent class {@link XMLParser}. It overrides {@link #parseRootElement(Element)} to
 * interpret the document as a {@link Topology}.
 *
 * The class does not create an new topology but populates an existing one passed to the constructor. The client code
 * is in charge of clearing the given topology.
 */
public class XMLTopologyParser extends XMLParser {
    private final Topology tp;

    /**
     * Construct a new topology parser.
     *
     * @param tp the {@link Topology} populated by the parser.
     */
    public XMLTopologyParser(Topology tp) {
        this.tp = tp;
    }

    @Override
    public void parseRootElement(Element element) throws ParserException {
        parseTopologyElement(element, tp);
    }

    /**
     * Interprets the root element of a topology and fulfilled the one passed as argument.
     *
     * @param topo the {@link Element root element} that describes the topology
     * @param tp the {@link Topology topology} that is populated with XML data.
     * @throws ParserException is raised if the given XML document is malformed.
     */
    public static void parseTopologyElement(Element topo, Topology tp) throws ParserException {
        if (!TOPOLOGY.equals(topo.getNodeName()))
            throw new ParserException("invalid node '" + topo.getNodeName() + "' where '" + TOPOLOGY + "' was expected");

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
                mapElementChildrenOf(e, el -> parseClass(el,tp) );
            else if (GRAPH.labelsElement(e))
                parseGraphElement(e, tp);
        });
    }

    private static void parseClass(Element C, Topology tp) throws ParserException {
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
                Class<? extends Node> cls = c.asSubclass(Node.class);
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

    private static void parseGraphElement(Element ge, Topology tp) throws ParserException {
        HashMap<String, Node> nodeids = new HashMap<>();
        mapElementChildrenOf(ge, e -> {
            if (NODE.labelsElement(e))
                parseNode(e, tp, nodeids);
            else if (LINK.labelsElement(e))
                parseLink(e, tp, nodeids);
            else if (GENERATOR.labelsElement(e))
                parseGenerators(e, tp);
            else throw new EnumConstantNotPresentException(XMLKeys.class, e.getTagName());
        });
    }

    private static Color parseColor(Element e, Color default_color) {
        Color result = default_color;
        String color = COLOR_ATTR.getValueFor(e, (String) null);
        if (color != null && !color.equals("None")) {
            result = new Color(Integer.parseUnsignedInt(color, 16));
        }
        return result;
    }

    private static Class<? extends Node> getNodeClass(Topology tp, String className) throws ParserException {
        try {
            Class<? extends Node> nodeClass;
            if ("default".equals(className) || className == null) {
                nodeClass = tp.getDefaultNodeModel();
                if (nodeClass == null) {
                    nodeClass = Node.class;
                }
            } else {
                nodeClass = tp.getNodeModel(className);
            }
            if (nodeClass == null) {
                nodeClass = Class.forName(className).asSubclass(Node.class);
            }
            return nodeClass;
        } catch (ClassNotFoundException e) {
            throw new ParserException(e);
        }
    }

    private static void parseNode(Element ne, Topology tp, Map<String, Node> nodeids) throws ParserException {
        Node n;

        try {
            Class<? extends Node> nodeClass = tp.getDefaultNodeModel();

            if (CLASS_ATTR.isAttributeOf(ne)) {
                nodeClass = getNodeClass(tp, CLASS_ATTR.getValueFor(ne));
            }
            if (nodeClass == null) {
                nodeClass = Node.class;
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

        n.setColor(parseColor(ne, Node.DEFAULT_COLOR));

        int size = SIZE_ATTR.getValueFor(ne, Node.DEFAULT_SIZE);
        n.setSize(size);
        double cr = COMMUNICATION_RANGE_ATTR.getValueFor(ne, tp.getCommunicationRange());
        n.setCommunicationRange(cr);
        double sr = SENSING_RANGE_ATTR.getValueFor(ne, tp.getSensingRange());
        n.setSensingRange(sr);
        double dir = DIRECTION_ATTR.getValueFor(ne, Node.DEFAULT_DIRECTION);
        n.setDirection(dir);
        double x = LOCATION_X_ATTR.getValueFor(ne, -1.0);
        double y = LOCATION_Y_ATTR.getValueFor(ne, -1.0);
        double z = LOCATION_Z_ATTR.getValueFor(ne, 0.0);
        n.setLocation(x, y, z);

        tp.addNode(n);
    }

    private static void parseLink(Element e, Topology tp, Map<String, Node> nodeids) throws ParserException {
        Link.Type type = DIRECTED_ATTR.getValueFor(e, false) ? DIRECTED : UNDIRECTED;

        Node src = nodeids.get(SOURCE_ATTR.getValueFor(e));
        if (src == null)
            throw new ParserException("unknown node: " + SOURCE_ATTR.getValueFor(e));
        Node dst = nodeids.get(DESTINATION_ATTR.getValueFor(e));
        if (dst == null)
            throw new ParserException("unknown node: " + DESTINATION_ATTR.getValueFor(e));
        Link l = new Link(src, dst, type, WIRED);

        l.setWidth(WIDTH_ATTR.getValueFor(e, Link.DEFAULT_WIDTH));
        l.setColor(parseColor(e, Link.DEFAULT_COLOR));
        tp.addLink(l, true);
    }

    private static void parseGeneratorAttributes(Element e, Topology tp, TopologyGeneratorFactory tgf) throws ParserException {
        tgf.setAbsoluteCoords(ABSOLUTE_COORDS_ATTR.getValueFor(e, false));
        tgf.setX(LOCATION_X_ATTR.getValueFor(e, 0.0));
        tgf.setY(LOCATION_Y_ATTR.getValueFor(e, 0.0));
        tgf.setOrder(ORDER_ATTR.getValueFor(e, 1));
        tgf.setWidth(WIDTH_ATTR.getValueFor(e, 1.0));
        tgf.setHeight(HEIGHT_ATTR.getValueFor(e, 1.0));
        tgf.setWired(WIRED_ATTR.getValueFor(e, false));
        tgf.setWirelessEnabled(WIRELESS_ENABLED_ATTR.getValueFor(e, true));
        tgf.setDirected(DIRECTED_ATTR.getValueFor(e, false));
        tgf.setNodeClass(getNodeClass(tp, NODECLASS_ATTR.getValueFor(e, "default")));
    }

    private static void parseLineGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);
        boolean horizontal = HORIZONTAL_ATTR.getValueFor(e,true);
        tgf.newLine(horizontal).generate(tp);
    }

    private static void parseRingGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);
        tgf.newRing().generate(tp);
    }

    private static void parseGridGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);

        TopologyGeneratorFactory.Generator gen;
        if (X_ORDER_ATTR.isAttributeOf(e)) {
            gen = tgf.newGrid(X_ORDER_ATTR.getValueFor(e, 1),Y_ORDER_ATTR.getValueFor(e, 1));
        } else {
            gen = tgf.newSquareGrid();
        }
        gen.generate(tp);
    }

    private static void parseTorusGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);

        TopologyGeneratorFactory.Generator gen;
        if (X_ORDER_ATTR.isAttributeOf(e)) {
            gen = tgf.newTorus(X_ORDER_ATTR.getValueFor(e, 1),Y_ORDER_ATTR.getValueFor(e, 1));
        } else {
            gen = tgf.newTorus();
        }
        gen.generate(tp);
    }

    private static void parseKNGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);
        tgf.newKN().generate(tp);
    }

    private static void parseRndLocationsGenerator(Element e, Topology tp) throws ParserException {
        TopologyGeneratorFactory tgf = new TopologyGeneratorFactory();
        parseGeneratorAttributes(e, tp, tgf);
        tgf.newRandomLocations().generate(tp);
    }

    private static void parseGenerators(Element ge, Topology tp) throws ParserException {
        mapElementChildrenOf(ge, e -> {
            if (LINE.labelsElement(e))
                parseLineGenerator(e, tp);
            else if (RING.labelsElement(e))
                parseRingGenerator(e, tp);
            else if (GRID.labelsElement(e))
                parseGridGenerator(e, tp);
            else if (TORUS.labelsElement(e))
                parseTorusGenerator(e, tp);
            else if (KN.labelsElement(e))
                parseKNGenerator(e, tp);
            else if (RANDOM_LOCATIONS.labelsElement(e))
                parseRndLocationsGenerator(e, tp);
            else
                throw new EnumConstantNotPresentException(XMLKeys.class, e.getTagName());
        });
    }
}
