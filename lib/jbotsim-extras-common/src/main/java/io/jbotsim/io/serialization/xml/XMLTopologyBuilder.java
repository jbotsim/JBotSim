package io.jbotsim.io.serialization.xml;

import io.jbotsim.core.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

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
        Element topo = XMLKeys.TOPOLOGY.createElement(doc);

        XMLKeys.WIRELESS_ENABLED_ATTR.setAttribute(topo, tp.getWirelessStatus());
        XMLKeys.CLOCK_SPEED_ATTR.setAttribute(topo, tp.getClockSpeed());

        XMLKeys.WIDTH_ATTR.setNotDefaultAttribute(topo, tp.getWidth(),
                Topology.DEFAULT_WIDTH);
        XMLKeys.HEIGHT_ATTR.setNotDefaultAttribute(topo, tp.getHeight(),
                Topology.DEFAULT_HEIGHT);
        XMLKeys.SENSING_RANGE_ATTR.setNotDefaultAttribute(topo, tp.getSensingRange(),
                Topology.DEFAULT_SENSING_RANGE);
        XMLKeys.COMMUNICATION_RANGE_ATTR.setNotDefaultAttribute(topo, tp.getCommunicationRange(),
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
            XMLKeys.IDENTIFIER_ATTR.setAttribute(e, id);
            XMLKeys.CLASS_ATTR.setAttribute(e, c.getName());
        }
    }

    private static Element buildClasses(Document doc, Topology tp) {
        Element classes = XMLKeys.CLASSES.createElement(doc);
        addNodeModels(doc, tp, classes);
        addModel(doc, classes, XMLKeys.MESSAGE_ENGINE, "default", tp.getMessageEngine(), MessageEngine.class);
        addModel(doc, classes, XMLKeys.LINK_RESOLVER, "default", tp.getLinkResolver(), LinkResolver.class);
        addModel(doc, classes, XMLKeys.SCHEDULER, "default", tp.getScheduler(), DefaultScheduler.class);
        addModel(doc, classes, XMLKeys.CLOCKCLASS, "default", tp.getClockModel(), DefaultClock.class);

        return classes;
    }

    private static void addNodeModels(Document doc, Topology tp, Element classes) {
        for (String mname : tp.getModelsNames()) {
            Class cls = tp.getNodeModel(mname);
            addModel(doc, classes, XMLKeys.NODECLASS, mname, cls, tp.getDefaultNodeModel());
        }
    }


    private static Element buildGraph(Document doc, Topology tp) {
        Element graph = XMLKeys.GRAPH.createElement(doc);
        for (Node n : tp.getNodes())
            addNode(doc, tp, graph, n);
        for (Link l : tp.getLinks(true))
            if (! l.isWireless())
                addLink(doc, graph, l);

        return graph;
    }

    private static void addNode(Document doc, Topology tp, Element graph, Node n) {
        Element ne = XMLKeys.NODE.createElement(doc, graph);

        XMLKeys.IDENTIFIER_ATTR.setAttribute(ne, n.getID());
        XMLKeys.COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(n.getColor()), colorToXml(Node.DEFAULT_COLOR));
        XMLKeys.SIZE_ATTR.setNotDefaultAttribute(ne, n.getSize(), Node.DEFAULT_SIZE);
        XMLKeys.COMMUNICATION_RANGE_ATTR.setNotDefaultAttribute(ne, n.getCommunicationRange(),
                tp.getCommunicationRange());
        XMLKeys.SENSING_RANGE_ATTR.setNotDefaultAttribute(ne, n.getSensingRange(),
                tp.getSensingRange());
        XMLKeys.DIRECTION_ATTR.setNotDefaultAttribute(ne, n.getDirection(), Node.DEFAULT_DIRECTION);
        XMLKeys.LOCATION_X_ATTR.setAttribute(ne, n.getX());
        XMLKeys.LOCATION_Y_ATTR.setAttribute(ne, n.getY());
        XMLKeys.LOCATION_Z_ATTR.setNotDefaultAttribute(ne, n.getZ(), 0.0);
        if (! n.getClass().equals (tp.getDefaultNodeModel()))
            XMLKeys.CLASS_ATTR.setNotDefaultAttribute(ne, n.getClass().getName(), "default");
    }

    private static String colorToXml(Color color) {
        if (color == null)
            return "None";
        else
            return Integer.toHexString(color.getRGB());
    }

    private static void addLink(Document doc, Element graph, Link l) {
        Element ne = XMLKeys.LINK.createElement(doc, graph);
        XMLKeys.DIRECTED_ATTR.setAttribute(ne, l.isDirected());
        XMLKeys.SOURCE_ATTR.setAttribute(ne, l.endpoint(0).getID());
        XMLKeys.DESTINATION_ATTR.setAttribute(ne, l.endpoint(1).getID());
        XMLKeys.WIDTH_ATTR.setNotDefaultAttribute(ne, l.getWidth(), Link.DEFAULT_WIDTH);
        XMLKeys.COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(l.getColor()), colorToXml(Link.DEFAULT_COLOR));
    }
}
