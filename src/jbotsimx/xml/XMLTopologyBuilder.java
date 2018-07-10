package jbotsimx.xml;

import jbotsim.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import java.util.Stack;
import java.util.function.BiConsumer;

import static jbotsimx.xml.XMLTopologyKeys.*;

public class XMLTopologyBuilder {
    public static final String VERSION = "1.0";

    private final Topology tp;
    private final Stack<String> elements = new Stack<>();

    public XMLTopologyBuilder(Topology tp) {
        this.tp = tp;
    }

    public Document buildDocument() throws BuilderException {
        DocumentBuilder builder;
        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.newDocument();
            addTopologyElement(doc);
            return doc;
        } catch (Exception e) {
            throw new BuilderException(e);
        }
    }

    public void write() throws BuilderException {
        write(new PrintWriter(System.out, true));
    }

    public void write(Writer out) throws BuilderException {
        Document doc = buildDocument();
        try {
            TransformerFactory tFactory =
                    TransformerFactory.newInstance();
            tFactory.setAttribute("indent-number", 2);

            Transformer transformer =
                    tFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(out);
            transformer.transform(source, result);
        } catch (Exception e) {
            throw new BuilderException("XML generator yield an exception.", e);
        }
    }

    public String writeToString() throws BuilderException {
        StringWriter sw = new StringWriter();
        write(sw);
        return sw.toString();
    }

    private void addTopologyElement(Document doc) {
        Element topo = TOPOLOGY.createElement(doc);
        doc.appendChild(topo);

        VERSION_ATTR.setAttribute(topo, VERSION);
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

        topo.appendChild(buildClasses(doc));
        topo.appendChild(buildGraph(doc));
    }

    private void addModel(Document doc, Element parent, XMLTopologyKeys key, String id, Object object,
                          Class default_class) {
        addModel(doc, parent, key, id, object.getClass(), default_class);
    }

    private void addModel(Document doc, Element parent, XMLTopologyKeys key, String id, Class c, Class default_class) {
        Element e = key.createElement(doc, parent);
        IDENTIFIER_ATTR.setAttribute(e, id);
        CLASS_ATTR.setAttribute(e, c.getName());
    }

    private Element buildClasses(Document doc) {
        Element classes = CLASSES.createElement(doc);
        addNodeModels(doc, classes);
        addModel(doc, classes, MESSAGE_ENGINE, "default", tp.getMessageEngine(), MessageEngine.class);
        addModel(doc, classes, LINK_RESOLVER, "default", tp.getLinkResolver(), LinkResolver.class);
        addModel(doc, classes, SCHEDULER, "default", tp.getScheduler(), DefaultScheduler.class);
        addModel(doc, classes, CLOCKCLASS, "default", tp.getClockModel(), DefaultClock.class);

        return classes;
    }

    private void addNodeModels(Document doc, Element classes) {
        for (String mname : tp.getModelsNames()) {
            Class cls = tp.getNodeModel(mname);
            addModel(doc, classes, NODECLASS, mname, cls, tp.getDefaultNodeModel());
        }
    }


    private Element buildGraph(Document doc) {
        Element graph = GRAPH.createElement(doc);
        for (Node n : tp.getNodes())
            addNode(doc, graph, n);
        for (Link l : tp.getLinks(true))
            if (! l.isWireless())
                addLink(doc, graph, l);

        return graph;
    }

    private void addNode(Document doc, Element graph, Node n) {
        Element ne = NODE.createElement(doc, graph);

        IDENTIFIER_ATTR.setAttribute(ne, n.getID());
        COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(n.getColor()), colorToXml(Node.DEFAULT_COLOR));
        SIZE_ATTR.setNotDefaultAttribute(ne, n.getSize(), Node.DEFAULT_SIZE);
        COMMUNICATION_RANGE_ATTR.setNotDefaultAttribute(ne, n.getCommunicationRange(),
                tp.getCommunicationRange());
        SENSING_RANGE_ATTR.setNotDefaultAttribute(ne, n.getSensingRange(),
                tp.getSensingRange());
        DIRECTION_ATTR.setNotDefaultAttribute(ne, n.getDirection(), 0.0);
        LOCATION_X_ATTR.setAttribute(ne, n.getX());
        LOCATION_Y_ATTR.setAttribute(ne, n.getY());
        LOCATION_Z_ATTR.setNotDefaultAttribute(ne, n.getZ(), 0.0);
        CLASS_ATTR.setNotDefaultAttribute(ne, n.getClass().getName(), "default");
    }

    private String colorToXml(Color color) {
        if (color == null)
            return "None";
        else
            return Integer.toHexString(color.getRGB());
    }

    private void addLink(Document doc, Element graph, Link l) {
        Element ne = LINK.createElement(doc, graph);
        DIRECTED_ATTR.setAttribute(ne, l.isDirected());
        SOURCE_ATTR.setAttribute(ne, l.endpoint(0).getID());
        DESTINATION_ATTR.setAttribute(ne, l.endpoint(1).getID());
        WIDTH_ATTR.setNotDefaultAttribute(ne, l.getWidth(), Link.DEFAULT_WIDTH);
        COLOR_ATTR.setNotDefaultAttribute(ne, colorToXml(l.getColor()), colorToXml(Link.DEFAULT_COLOR));
    }

    private void addSimpleElement(Document doc, Element parent, XMLTopologyKeys key, BiConsumer<Document, Element> c) {
        Element e = key.createElement(doc);
        parent.appendChild(e);
        c.accept(doc, e);
    }

    private <T> void addSimpleElement(Document doc, Element parent, XMLTopologyKeys key, T value) {
        addSimpleElement(doc, parent, key, (d, e) -> e.appendChild(d.createTextNode(String.valueOf(value))));
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
