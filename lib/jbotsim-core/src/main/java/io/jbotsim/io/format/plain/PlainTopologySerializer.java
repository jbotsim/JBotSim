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
package io.jbotsim.io.format.plain;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.io.TopologySerializer;

import java.util.HashMap;

public class PlainTopologySerializer implements TopologySerializer {
    public void importFromString(Topology topology, String data){
        topology.clear();
        topology.setCommunicationRange(Double.parseDouble(data.substring(data.indexOf(" ") + 1, data.indexOf("\n"))));
        data = data.substring(data.indexOf("\n") + 1);
        topology.setSensingRange(Double.parseDouble(data.substring(data.indexOf(" ") + 1, data.indexOf("\n"))));
        data = data.substring(data.indexOf("\n") + 1);
        HashMap<String, Node> nodeTable = new HashMap<>();
        while (data.indexOf("[") > 0) {
            double x = new Double(data.substring(data.indexOf("x") + 3, data.indexOf(", y")));
            double y = 0;
            double z = 0;
            if (data.contains("z")) {
                y = new Double(data.substring(data.indexOf("y") + 3, data.indexOf(", z")));
                z = new Double(data.substring(data.indexOf("z") + 3, data.indexOf("]")));
            }else{
                y = new Double(data.substring(data.indexOf("y") + 3, data.indexOf("]")));
            }
            try {
                Node node = topology.newInstanceOfModel("default");
                node.setLocation(x, y, z);
                topology.addNode(node);
                String id = data.substring(0, data.indexOf(" "));
                node.setProperty("id", id);
                nodeTable.put(id, node);
                data = data.substring(data.indexOf("\n") + 1);
            } catch (Exception e) {}
        }
        while (data.indexOf("--") > 0) {
            Node n1 = nodeTable.get(data.substring(0, data.indexOf(" ")));
            Node n2 = nodeTable.get(data.substring(data.indexOf(">") + 2, data.indexOf("\n")));
            Link.Orientation orientation = (data.indexOf("<") > 0 && data.indexOf("<") < data.indexOf("\n")) ? Link.Orientation.UNDIRECTED : Link.Orientation.DIRECTED;
            topology.addLink(new Link(n1, n2, orientation, Link.Mode.WIRED));
            data = data.substring(data.indexOf("\n") + 1);
        }
    }
    public String exportToString(Topology topology){
        StringBuffer res = new StringBuffer();
        res.append("cR " + topology.getCommunicationRange() + "\n");
        res.append("sR " + topology.getSensingRange() + "\n");
        for (Node n : topology.getNodes()) {
            Point p2d = new Point();
            p2d.setLocation(n.getLocation().getX(), n.getLocation().getY());
            res.append(n.toString() + " " + p2d.toString().substring(p2d.toString().indexOf("[") -1) + "\n");
        }
        for (Link l : topology.getLinks())
            if (!l.isWireless())
                res.append(l.toString() + "\n");
        return res.toString();
    }

}
