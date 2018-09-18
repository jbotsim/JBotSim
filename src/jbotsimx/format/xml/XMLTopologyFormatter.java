package jbotsimx.format.xml;

import jbotsim.Topology;
import jbotsimx.format.common.Formatter;

public class XMLTopologyFormatter implements Formatter {
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
