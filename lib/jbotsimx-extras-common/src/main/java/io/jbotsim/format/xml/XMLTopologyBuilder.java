package io.jbotsim.format.xml;

import io.jbotsim.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static io.jbotsim.format.xml.XMLKeys.*;

/**
 * Builder for {@link Topology topologies} objects.
 *
 * This class is used, mainly, through methods inherited from {@link XMLBuilder}. The class method
 * {@link #buildTopologyElement} can be invoked to build the element that stores an entire
 * {@link Topology}; for instance it is used by {@link XMLTraceBuilder}.
 */
public class XMLTopologyBuilder extends XMLBuilder {
    /**
     * This constructor add to the root element of the {@link Document document} the {@code topology} element that
     * stores the {@link Topology topology}.
     *
     * @param tp the {@link Topology} for which the {@link Document} is built.
     * @throws BuilderException is raised if an error occurs during the construction of the document.
     */
    public XMLTopologyBuilder(Topology tp) throws BuilderException {
        super();
        Document doc = getDocument();
        Element topo = buildTopologyElement(doc, tp);
        doc.getDocumentElement().appendChild(topo);
    }

    /**
     * Method that builds the actual element (and subtrees) that stores the givent {@link Topology} {@code tp}.
     *
     * Note that this method does not add the generated element to the XML document. This task is devoted to client
     * methods.
     *
     * @param doc the {@link Document document} for which elements are created.
     * @param tp the {@link Topology topology} to be translated in XML.
     * @return the {@link Element element} represetning the {@link Topology} {@code tp}
     */
    public static Element buildTopologyElement(Document doc, Topology tp) {
        Element topo = TOPOLOGY.createElement(doc);

        WIRELESS_ENABLED_ATTR.setAttribute(topo, tp.getWirelessStatus());
        CLOCK_SPEED_ATTR.setAttribute(topo, tp.getClockSpeed());

        WIDTH_ATTR.setNotDefaultAttribute(topo, tp.getWidth(),
                Topology.DEFAULT_WIDTH);
        HEIGHT_ATTR.setNotDefaultAttribute(topo, tp.getHeight(),
                Topology.DEFAULT_HEIGHT);
        SENSING_RANGE_ATTR.setNotDefaultAttribute(topo, tp.getSensingRange(),
                Topology.DEFAULT_SENSING_RANGE);
        COMMUNICATION_RANGE_ATTR.setNotDefaultAttribute(topo, tp.getCommunicationRange(),
                Topology.DEFAULT_COMMUNICATION_RANGE);

        topo.appendChild(buildClasses(doc, tp));
        topo.appendChild(buildGraph(doc, tp));

        return topo;
    }

    private static void addModel(Document doc, Element parent, XMLKeys key, String id, Object object,
                                 Class default_class) {
        addModel(doc, parent, key, id, object.getClass(), default_class);
    }

    private static void addModel(Document doc, Element parent, XMLKeys key, String id, Class c, Class default_class) {
        if (c != null) {
            Element e = key.createElement(doc, parent);
            IDENTIFIER_ATTR.setAttribute(e, id);
            CLASS_ATTR.setAttribute(e, c.getName());
        }
    }

    private static Element buildClasses(Document doc, Topology tp) {
        Element classes = CLASSES.createElement(doc);
        addNodeModels(doc, tp, classes);
        addModel(doc, classes, MESSAGE_ENGINE, "default", tp.getMessageEngine(), MessageEngine.class);
        addModel(doc, classes, LINK_RESOLVER, "default", tp.getLinkResolver(), LinkResolver.class);
        addModel(doc, classes, SCHEDULER, "default", tp.getScheduler(), DefaultScheduler.class);
        addModel(doc, classes, CLOCKCLASS, "default", tp.getClockModel(), DefaultClock.class);

        return classes;
    }

    private static void addNodeModels(Document doc, Topology tp, Element classes) {
        for (String mname : tp.getModelsNames()) {
            Class cls = tp.getNodeModel(mname);
            addModel(doc, classes, NODECLASS, mname, cls, tp.getDefaultNodeModel());
        }
    }


    private static Element buildGraph(Document doc, Topology tp) {
        Element graph = GRAPH.createElement(doc);
        for (Node n : tp.getNodes())
            addNode(doc, tp, graph, n);
        for (Link l : tp.getLinks(true))
            if (! l.isWireless())
                addLink(doc, graph, l);

        return graph;
    }

    private static void addNode(Document doc, Topology tp, Element graph, Node n) {
        Element ne = NODE.createElement(doc, graph);

        IDENTIFIER_ATTR.setAttribute(ne, n.getID());
        COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(n.getColor()), colorToXml(Node.DEFAULT_COLOR));
        SIZE_ATTR.setNotDefaultAttribute(ne, n.getSize(), Node.DEFAULT_SIZE);
        COMMUNICATION_RANGE_ATTR.setNotDefaultAttribute(ne, n.getCommunicationRange(),
                tp.getCommunicationRange());
        SENSING_RANGE_ATTR.setNotDefaultAttribute(ne, n.getSensingRange(),
                tp.getSensingRange());
        DIRECTION_ATTR.setNotDefaultAttribute(ne, n.getDirection(), Node.DEFAULT_DIRECTION);
        LOCATION_X_ATTR.setAttribute(ne, n.getX());
        LOCATION_Y_ATTR.setAttribute(ne, n.getY());
        LOCATION_Z_ATTR.setNotDefaultAttribute(ne, n.getZ(), 0.0);
        if (! n.getClass().equals (tp.getDefaultNodeModel()))
            CLASS_ATTR.setNotDefaultAttribute(ne, n.getClass().getName(), "default");
    }

    private static String colorToXml(Color color) {
        if (color == null)
            return "None";
        else
            return Integer.toHexString(color.getRGB());
    }

    private static void addLink(Document doc, Element graph, Link l) {
        Element ne = LINK.createElement(doc, graph);
        DIRECTED_ATTR.setAttribute(ne, l.isDirected());
        SOURCE_ATTR.setAttribute(ne, l.endpoint(0).getID());
        DESTINATION_ATTR.setAttribute(ne, l.endpoint(1).getID());
        WIDTH_ATTR.setNotDefaultAttribute(ne, l.getWidth(), Link.DEFAULT_WIDTH);
        COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(l.getColor()), colorToXml(Link.DEFAULT_COLOR));
    }
}
