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
package io.jbotsim.io.format.xml;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.TopologySerializer;

public class XMLTopologySerializer implements TopologySerializer {
    private boolean validateDocument;

    public XMLTopologySerializer(boolean validateDocument) {
        this.validateDocument = validateDocument;
    }

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
            XMLTopologyParser parser = new XMLTopologyParser(tp, validateDocument);
            parser.parseFromString(s);
        } catch (XMLParser.ParserException e) {
            e.printStackTrace();
        }
    }
}
