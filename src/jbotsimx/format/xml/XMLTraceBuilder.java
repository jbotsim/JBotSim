package jbotsimx.format.xml;

import jbotsim.Topology;
import jbotsimx.replay.TraceEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static jbotsimx.format.xml.XMLKeys.*;

/**
 * Builder used to store recorded <i>executions</i> of a {@link Topology}.
 *
 * <p>This class is used, mainly, through methods inherited from {@link XMLBuilder}. It calls
 * {@link XMLTopologyBuilder#buildTopologyElement} to build the element that stores the recorded {@link Topology}.</p>
 *
 * <p>Event of the {@link Topology} are recorded using {@link #addTraceEvent(TraceEvent)} method.</p>
 */
public class XMLTraceBuilder extends XMLBuilder {
    private Element traceElement;

    /**
     * This constructor add to the root element of the {@link Document document} the {@code trace} element that
     * stores the {@link Topology topology} {@code tp} and the recorded events.
     *
     * Since the trace may not exist before the creation of the document, events are stored one by one using
     * {@link #addTraceEvent(TraceEvent)} after the document has been built.
     *
     * @param tp the {@link Topology topology} that is traced.
     * @throws BuilderException is raised if an error occurs while the document is built.
     */
    public XMLTraceBuilder(Topology tp) throws BuilderException {
        super();
        Document document = getDocument();
        traceElement = TRACE.createElement(document);
        Element topo = XMLTopologyBuilder.buildTopologyElement(document, tp);
        traceElement.appendChild(topo);

        document.getDocumentElement().appendChild(traceElement);
    }

    /**
     * Adds an event to the {@link Document document}
     *
     * @param e the {@link TraceEvent event} added to the stored story
     */
    public void addTraceEvent(TraceEvent e){
        Document doc = getDocument();
        Element event = null;
        switch(e.getKind()) {
            case START_TOPOLOGY:
                event = START_TOPOLOGY.createElement(doc);
                TIME_ATTR.setAttribute(event, e.getTime());
                break;
            case ADD_NODE:
                event = ADD_NODE.createElement(doc);
                TIME_ATTR.setAttribute(event, e.getTime());
                IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                LOCATION_X_ATTR.setAttribute(event, e.getX());
                LOCATION_Y_ATTR.setAttribute(event, e.getY());
                NODECLASS.setAttribute(event, e.getNodeClass());
                break;
            case DEL_NODE:
                event = DELETE_NODE.createElement(doc);
                TIME_ATTR.setAttribute(event, e.getTime());
                IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                break;
            case MOVE_NODE:
                event = MOVE_NODE.createElement(doc);
                TIME_ATTR.setAttribute(event, e.getTime());
                IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                LOCATION_X_ATTR.setAttribute(event, e.getX());
                LOCATION_Y_ATTR.setAttribute(event, e.getY());
                break;
            case SELECT_NODE:
                event = SELECT_NODE.createElement(doc);
                TIME_ATTR.setAttribute(event, e.getTime());
                IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                break;
        }
        assert(event != null);
        traceElement.appendChild(event);
    }
}
