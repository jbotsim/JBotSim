package jbotsimx.format.xml;

import jbotsim.Topology;
import jbotsimx.replay.TraceEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static jbotsimx.format.xml.XMLKeys.*;

public class XMLTraceBuilder extends XMLBuilder {
    private Element traceElement;

    public XMLTraceBuilder(Topology tp) throws BuilderException {
        super();
        Document document = getDocument();
        traceElement = TRACE.createElement(document);
        Element topo = XMLTopologyBuilder.buildTopologyElement(document, tp);
        traceElement.appendChild(topo);

        document.getDocumentElement().appendChild(traceElement);
    }

    public void addTraceEvent(TraceEvent e){
        Document doc = getDocument();
        Element event = null;
        switch(e.getKind()) {
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
