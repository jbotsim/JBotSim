package io.jbotsim.serialization.xml;

import io.jbotsim.core.Topology;
import io.jbotsim.core.io.FileAsStream;
import io.jbotsim.core.io.FileManagerProvider;
import io.jbotsim.dynamicity.movement.trace.TraceEvent;
import io.jbotsim.dynamicity.movement.trace.TraceFileReader;
import io.jbotsim.dynamicity.movement.trace.TracePlayer;
import org.w3c.dom.Element;

/**
 * Interpreter for an {@link org.w3c.dom.Document XML document} that represents a trace of execution of a
 * {@link Topology}.
 *
 * This class is used through its parent class {@link XMLParser}. It overrides {@link #parseRootElement(Element)} to
 * interpret the document as a {@link Topology topology} and a set of {@link TraceEvent events} on that
 * topology.
 *
 * The class does not create an new topology. It populates a {@link TracePlayer} passed to the constructor.
 */
public class XMLTraceParser extends XMLParser implements TraceFileReader {
    private TracePlayer tracePlayer;
    private final FileManagerProvider fileManagerProvider;

    /**
     * Creates a parser for the given {@link TracePlayer}.
     *
     * @param fileManagerProvider the {@link TracePlayer} that is populated by the parser.
     */
    public XMLTraceParser(FileManagerProvider fileManagerProvider) {
        this.fileManagerProvider = fileManagerProvider;
    }

    /**
     * Loads and parses the document contained in (@code filename}.
     *
     * @param filename the name of the file to read.
     * @param tracePlayer the {@link TracePlayer} that is populated by the parser.
     * @throws ParserException raised if an IO error occurs or if the XML document is malformed.
     */
    @Override
    public void parse(String filename, TracePlayer tracePlayer) throws ParserException {
        this.tracePlayer = tracePlayer;
        try {
            parse(new XMLIO(getFileAccessor()).read(filename));
        } catch (XMLIO.XMLIOException e) {
            throw new ParserException(e);
        }
    }

    protected FileAsStream getFileAccessor() {
        return fileManagerProvider.getFileManager();
    }

    @Override
    public void parseRootElement(Element element) throws ParserException {
        parseTraceElement(element, tracePlayer);
    }

    /**
     * Populates the given {@link TracePlayer} {@code tracePlayer} with the topology and events describes by
     * {@link Element element}.
     *
     * @param element the root element of the XML tree describing the trace.
     * @param tp the {@link TracePlayer} popuylated by the parser
     * @throws ParserException is raised if the given {@link org.w3c.dom.Document document} is malformed.
     */
    public static void parseTraceElement(Element element, final TracePlayer tp) throws ParserException {
        if (!XMLKeys.TRACE.equals(element.getNodeName()))
            throw new ParserException("invalid node '" + element.getNodeName() + "' where '" +
                    XMLKeys.TRACE + "' was expected");
        mapElementChildrenOf(element, new ElementVisitor() {
            @Override
            public void accept(Element e) throws ParserException {
                if (XMLKeys.TOPOLOGY.labelsElement(e))
                    XMLTopologyParser.parseTopologyElement(e, tp.getTopology());
                else {
                    TraceEvent ev = null;
                    if (XMLKeys.ADD_NODE.labelsElement(e))
                        ev = parseAddNodeEvent(e);
                    else if (XMLKeys.DELETE_NODE.labelsElement(e))
                        ev = parseDeleteNodeEvent(e);
                    else if (XMLKeys.MOVE_NODE.labelsElement(e))
                        ev = parseMoveNodeEvent(e);
                    else if (XMLKeys.SELECT_NODE.labelsElement(e))
                        ev = parseSelectNodeEvent(e);
                    else if (XMLKeys.START_TOPOLOGY.labelsElement(e))
                        ev = parseStartTopologyEvent(e);
                    assert (ev != null);
                    tp.addTraceEvent(ev);
                }
            }
        });
    }

    private static TraceEvent parseStartTopologyEvent (Element e) {
        int time = XMLKeys.TIME_ATTR.getIntegerValueFor(e);
        return TraceEvent.newStartTopology(time);
    }

    private static TraceEvent parseAddNodeEvent (Element e) {
        int time = XMLKeys.TIME_ATTR.getIntegerValueFor(e);
        int id = XMLKeys.IDENTIFIER_ATTR.getIntegerValueFor(e);
        double x = XMLKeys.LOCATION_X_ATTR.getDoubleValueFor(e);
        double y = XMLKeys.LOCATION_Y_ATTR.getDoubleValueFor(e);
        String className = XMLKeys.NODECLASS_ATTR.getValueFor(e, "default");
        return TraceEvent.newAddNode(time, id, x, y, className);
    }

    private static TraceEvent parseDeleteNodeEvent (Element e) {
        int time = XMLKeys.TIME_ATTR.getIntegerValueFor(e);
        int id = XMLKeys.IDENTIFIER_ATTR.getIntegerValueFor(e);
        return TraceEvent.newDeleteNode(time, id);
    }

    private static TraceEvent parseMoveNodeEvent (Element e) {
        int time = XMLKeys.TIME_ATTR.getIntegerValueFor(e);
        int id = XMLKeys.IDENTIFIER_ATTR.getIntegerValueFor(e);
        double x = XMLKeys.LOCATION_X_ATTR.getDoubleValueFor(e);
        double y = XMLKeys.LOCATION_Y_ATTR.getDoubleValueFor(e);
        return TraceEvent.newMoveNode(time, id, x, y);
    }

    private static TraceEvent parseSelectNodeEvent (Element e) {
        int time = XMLKeys.TIME_ATTR.getIntegerValueFor(e);
        int id = XMLKeys.IDENTIFIER_ATTR.getIntegerValueFor(e);
        return TraceEvent.newSelectNode(time, id);
    }
}
