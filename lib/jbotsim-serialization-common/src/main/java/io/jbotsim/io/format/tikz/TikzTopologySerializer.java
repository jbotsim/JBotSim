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
package io.jbotsim.io.format.tikz;

import io.jbotsim.core.*;
import io.jbotsim.io.TopologySerializer;

/**
 *  Tikz export for objects of type Topology in JBotSim.
 */
public class TikzTopologySerializer implements TopologySerializer {
    public static String getStringColor(Color color){
        if (color == null)
            return "";
        if (color.equals(Color.BLACK))
            return "black";
        if (color.equals(Color.RED))
            return "red";
        if (color.equals(Color.BLUE))
            return "blue";
        if (color.equals(Color.GREEN))
            return "green";
        if (color.equals(Color.WHITE))
            return "white";
        if (color.equals(Color.GRAY))
            return "gray";
        if (color.equals(Color.CYAN))
            return "cyan";
        if (color.equals(Color.MAGENTA))
            return "magenta";
        if (color.equals(Color.ORANGE))
            return "orange";
        if (color.equals(Color.DARK_GRAY))
            return "darkgray";
        if (color.equals(Color.LIGHT_GRAY))
            return "lightGray";
        if (color.equals(Color.PINK))
            return "pink";
        if (color.equals(Color.YELLOW))
            return "yellow";

        return customRGBTikzColor(color);
    }

    private static String customRGBTikzColor(Color color) {
        return "color={rgb:red,"+color.getRed()+";green,"+color.getGreen()+";blue,"+color.getBlue()+"}";
    }

    @Override
    public void importFromString(Topology topology, String data) {
        return;
    }

    @Override
    public String exportToString(Topology topology){
        return exportTopology(topology, 50);
    }

    public String exportTopology(Topology tp, double scale){
        final String delim="\n";

        String s = "\\begin{tikzpicture}[scale=1]" + delim;

        s += exportSensingRanges(tp, scale, delim);
        s += exportNodes(tp, scale, delim);
        s += exportLinks(tp, delim);

        s+="\\end{tikzpicture}"+delim;
        return s;
    }

    private String exportSensingRanges(Topology tp, double scale, final String delim) {
        String result = "";
        Integer sr = (int) tp.getSensingRange();
        if (sr != 0) {
            result = result + "  \\tikzstyle{every node}=[draw,circle,inner sep=" + sr / 5.0 + ", fill opacity=0.5,gray,fill=gray!40]" + delim;
            for (Node n : tp.getNodes())
                result = exportSensingRange(n, scale, delim);

        }
        return result;
    }

    private String exportSensingRange(Node n, double scale, final String delim) {
        return exportNode(n, scale, delim);
    }

    private String exportNodes(Topology tp, double scale, final String delim) {
        String result = "";
        String header = "  \\tikzstyle{every node}=[draw,circle,fill=gray,inner sep=1.5]";
        result += header + delim;

        for (Node n : tp.getNodes())
            result += exportNode(n, scale, delim);

        return result;
    }

    private String exportNode(Node n, double scale, final String delim) {
        String id = "v"+ n;
        Point coords = getDisplayableCoords(n, scale);
        String color = getStringColor(n.getColor());
        return "  \\path (" + coords.x + "," + coords.y + ") node [" + color + "] (" + id + ") {};" + delim;
    }

    private Point getDisplayableCoords(Node n, double scale) {
        double x = Math.round(n.getX()*100/scale) / 100.0;
        double y = Math.round((600.0 - n.getY()) * 100/scale) / 100.0;
        return new Point(x, y);
    }

    private String exportLinks(Topology tp, final String delim) {
        String result = "";
        String header = "  \\tikzstyle{every path}=[];";
        result += header+delim;

        for (Link l : tp.getLinks())
            result += exportLink(l, delim);
        return result;
    }

    private String exportLink(Link l, final String delim) {
        String options = "";
        options = addOption(options, getStringColor(l.getColor()));
        if (l.getWidth()>1)
            options = addOption(options, "ultra thick");
        if (l.isDirected())
            options = addOption(options, "->");
        String id1 = "v" + l.source;
        String id2 = "v" + l.destination;
        return "  \\draw [" + options + "] (" + id1 + ")--(" + id2 + ");" + delim;
    }

    private String addOption(String options, String option) {
        if (options == null || options.length() == 0)
            return option;

        return options + ", " + option;
    }
}
