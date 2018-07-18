package jbotsimx.format.xml;

import jbotsimx.replay.TraceEvent;
import jbotsimx.replay.TracePlayer;
import org.w3c.dom.Element;

import static jbotsimx.format.xml.XMLKeys.*;

/**
 * Interpreter for an {@link org.w3c.dom.Document XML document} that represents a trace of execution of a
 * {@link jbotsim.Topology}.
 *
 * This class is used through its parent class {@link XMLParser}. It overrides {@link #parseRootElement(Element)} to
 * interpret the document as a {@link jbotsim.Topology topology} and a set of {@link TraceEvent events} on that
 * topology.
 *
 * The class does not create an new topology. It populates a {@link TracePlayer} passed to the constructor.
 */
public class XMLTraceParser extends XMLParser {
    private final TracePlayer tp;

    /**
     * Creates a parser for the given {@link TracePlayer}.
     *
     * @param tp the {@link TracePlayer} that is populated by the parser.
     */
    public XMLTraceParser(TracePlayer tp) {
        this.tp = tp;
    }

    @Override
    public void parseRootElement(Element element) throws ParserException {
        parseTraceElement(element, tp);
    }

    /**
     * Populates the given {@link TracePlayer} {@code tp} with the topology and events describes by
     * {@link Element element}.
     *
     * @param element the root element of the XML tree describing the trace.
     * @param tp the {@link TracePlayer} popuylated by the parser
     * @throws ParserException is raised if the given {@link org.w3c.dom.Document document} is malformed.
     */
    public static void parseTraceElement(Element element, TracePlayer tp) throws ParserException {
        if (!TRACE.equals(element.getNodeName()))
            throw new ParserException("invalid node '" + element.getNodeName() + "' where '" +
                    TRACE + "' was expected");
        mapElementChildrenOf(element, e -> {
            if (TOPOLOGY.labelsElement(e))
                XMLTopologyParser.parseTopologyElement(e, tp.getTopology());
            else {
                TraceEvent ev = null;
                if (ADD_NODE.labelsElement(e))
                    ev = parseAddNodeEvent(e);
                else if (DELETE_NODE.labelsElement(e))
                    ev = parseDeleteNodeEvent(e);
                else if (MOVE_NODE.labelsElement(e))
                    ev = parseMoveNodeEvent(e);
                else if (SELECT_NODE.labelsElement(e))
                    ev = parseSelectNodeEvent(e);
                else if (START_TOPOLOGY.labelsElement(e))
                    ev = parseStartTopologyEvent(e);
                assert (ev != null);
                tp.addTraceEvent(ev);
            }
        });
    }

    private static TraceEvent parseStartTopologyEvent (Element e) {
        int time = TIME_ATTR.getIntegerValueFor(e);
        return TraceEvent.newStartTopology(time);
    }

    private static TraceEvent parseAddNodeEvent (Element e) {
        int time = TIME_ATTR.getIntegerValueFor(e);
        int id = IDENTIFIER_ATTR.getIntegerValueFor(e);
        double x = LOCATION_X_ATTR.getDoubleValueFor(e);
        double y = LOCATION_Y_ATTR.getDoubleValueFor(e);
        String className = NODECLASS_ATTR.getValueFor(e, "default");
        return TraceEvent.newAddNode(time, id, x, y, className);
    }

    private static TraceEvent parseDeleteNodeEvent (Element e) {
        int time = TIME_ATTR.getIntegerValueFor(e);
        int id = IDENTIFIER_ATTR.getIntegerValueFor(e);
        return TraceEvent.newDeleteNode(time, id);
    }

    private static TraceEvent parseMoveNodeEvent (Element e) {
        int time = TIME_ATTR.getIntegerValueFor(e);
        int id = IDENTIFIER_ATTR.getIntegerValueFor(e);
        double x = LOCATION_X_ATTR.getDoubleValueFor(e);
        double y = LOCATION_Y_ATTR.getDoubleValueFor(e);
        return TraceEvent.newMoveNode(time, id, x, y);
    }

    private static TraceEvent parseSelectNodeEvent (Element e) {
        int time = TIME_ATTR.getIntegerValueFor(e);
        int id = IDENTIFIER_ATTR.getIntegerValueFor(e);
        return TraceEvent.newSelectNode(time, id);
    }
}
