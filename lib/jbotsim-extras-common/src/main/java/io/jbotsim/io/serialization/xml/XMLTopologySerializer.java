package io.jbotsim.io.serialization.xml;

import io.jbotsim.core.Topology;
import io.jbotsim.io.serialization.topology.string.TopologySerializer;

public class XMLTopologySerializer implements TopologySerializer {
    @Override
    public String exportTopology(Topology tp) {
        String result;

        try {
            XMLTopologyBuilder builder = new XMLTopologyBuilder(tp);
            result = builder.writeToString();
        } catch (XMLBuilder.BuilderException e) {
            result = null;
            e.printStackTrace();
        }

        return result;
    }

    @Override
    public void importTopology(Topology tp, String s) {
        try {
            XMLTopologyParser parser = new XMLTopologyParser(tp);
            parser.parseFromString(s);
        } catch (XMLParser.ParserException e) {
            e.printStackTrace();
        }
    }
}
