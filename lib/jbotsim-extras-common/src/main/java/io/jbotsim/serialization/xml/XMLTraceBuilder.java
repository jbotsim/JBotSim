/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package io.jbotsim.serialization.xml;

import io.jbotsim.core.Topology;
import io.jbotsim.core.io.FileAsStream;
import io.jbotsim.dynamicity.movement.trace.TraceEvent;
import io.jbotsim.dynamicity.movement.trace.TraceFileWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Builder used to store recorded <i>executions</i> of a {@link Topology}.
 *
 * <p>This class is used, mainly, through methods inherited from {@link XMLBuilder}. It calls
 * {@link XMLTopologyBuilder#buildTopologyElement} to build the element that stores the recorded {@link Topology}.</p>
 *
 * <p>Event of the {@link Topology} are recorded using {@link #addTraceEvent(TraceEvent)} method.</p>
 */
public class XMLTraceBuilder extends XMLBuilder implements TraceFileWriter {
    private Element traceElement;
    private Topology tp;

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
        this.tp = tp;
        Document document = getDocument();
        traceElement = XMLKeys.TRACE.createElement(document);
        Element topo = XMLTopologyBuilder.buildTopologyElement(document, tp);
        traceElement.appendChild(topo);

        document.getDocumentElement().appendChild(traceElement);
    }

    protected FileAsStream getFileAccessor() {
        return tp.getFileManager();
    }

    /**
     * Outputs the current XML document into the specified file.
     *
     * @param filename the targeted output file
     * @throws BuilderException raised either when an XML error occurs while the document is created or if an IO error
     *         occurs.
     */
    @Override
    public void write(String filename) throws BuilderException {
        try {
            new XMLIO(getFileAccessor()).write(filename, getDocument());
        } catch (XMLIO.XMLIOException e) {
            throw new BuilderException(e);
        }
    }

    /**
     * Adds an event to the {@link Document document}
     *
     * @param e the {@link TraceEvent event} added to the stored story
     */
    @Override
    public void addTraceEvent(TraceEvent e){
        Document doc = getDocument();
        Element event = null;
        switch(e.getKind()) {
            case START_TOPOLOGY:
                event = XMLKeys.START_TOPOLOGY.createElement(doc);
                XMLKeys.TIME_ATTR.setAttribute(event, e.getTime());
                break;
            case ADD_NODE:
                event = XMLKeys.ADD_NODE.createElement(doc);
                XMLKeys.TIME_ATTR.setAttribute(event, e.getTime());
                XMLKeys.IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                XMLKeys.LOCATION_X_ATTR.setAttribute(event, e.getX());
                XMLKeys.LOCATION_Y_ATTR.setAttribute(event, e.getY());
                XMLKeys.NODECLASS.setAttribute(event, e.getNodeClass());
                break;
            case DEL_NODE:
                event = XMLKeys.DELETE_NODE.createElement(doc);
                XMLKeys.TIME_ATTR.setAttribute(event, e.getTime());
                XMLKeys.IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                break;
            case MOVE_NODE:
                event = XMLKeys.MOVE_NODE.createElement(doc);
                XMLKeys.TIME_ATTR.setAttribute(event, e.getTime());
                XMLKeys.IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                XMLKeys.LOCATION_X_ATTR.setAttribute(event, e.getX());
                XMLKeys.LOCATION_Y_ATTR.setAttribute(event, e.getY());
                break;
            case SELECT_NODE:
                event = XMLKeys.SELECT_NODE.createElement(doc);
                XMLKeys.TIME_ATTR.setAttribute(event, e.getTime());
                XMLKeys.IDENTIFIER_ATTR.setAttribute(event, e.getNodeID());
                break;
        }
        assert(event != null);
        traceElement.appendChild(event);
    }
}
