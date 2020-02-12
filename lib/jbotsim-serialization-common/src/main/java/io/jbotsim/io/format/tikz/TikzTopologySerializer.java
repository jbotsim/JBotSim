/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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
 *  <p>The {@link TikzTopologySerializer} exports a JBotSim {@link Topology} into TikZ format.</p>
 *
 *  <p>Note: {@link #importFromString(Topology, String)} is not supported.</p>
 */
public class TikzTopologySerializer implements TopologySerializer {

    /**
     * The default End-Of-Line delimiter.
     */
    public static final String DEFAULT_EOL = "\n";

    /**
     * The default scale factor that must be applied to the distance between nodes.
     */
    public static final double DEFAULT_SCALE_FACTOR = 1/50.;

    protected String eol = DEFAULT_EOL;
    protected double scaleFactor = DEFAULT_SCALE_FACTOR;

    /**
     * Creates {@link TikzTopologySerializer} with the default values.
     *
     * @see #DEFAULT_EOL
     * @see #DEFAULT_SCALE_FACTOR
     */
    public TikzTopologySerializer() {
        this(DEFAULT_EOL, DEFAULT_SCALE_FACTOR);
    }

    /**
     * Creates A {@link TikzTopologySerializer} according to the provided parameters.
     *
     * @param eol The String to append at the end of each line.
     * @param scaleFactor The scale factor to apply to each {@link Node} location, as a double.
     *
     * @see #DEFAULT_EOL
     * @see #DEFAULT_SCALE_FACTOR
     */
    public TikzTopologySerializer(String eol, double scaleFactor) {
        this.eol = eol;
        this.scaleFactor = scaleFactor;
    }

    /**
     * <p>Converts a JBotSim {@link Color} into a color string displayable with TikZ.</p>
     *
     * @param color a {@link Color} to be converter
     * @return a TikZ-displayable equivalent of the provided color.
     */
    public static String getTikzStringColor(Color color){
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
        return exportTopology(topology, scaleFactor);
    }

    protected String exportTopology(Topology topology, double scaleFactor){

        StringBuilder sb = new StringBuilder();

        sb.append("\\begin{tikzpicture}[scale=1]" + eol);

        exportSensingRanges(sb, topology, scaleFactor);
        exportNodes(sb, topology, scaleFactor);
        exportLinks(sb, topology);

        sb.append("\\end{tikzpicture}"+ eol);
        return sb.toString();
    }

    private void exportSensingRanges(StringBuilder sb, Topology tp, double scale) {
        Integer sr = (int) tp.getSensingRange();
        if (sr == 0)
            return;

        double innerSep = sr / 5.0;
        sb.append("  \\tikzstyle{every node}=[draw,circle,inner sep=" + innerSep + ", fill opacity=0.5,gray,fill=gray!40]" + eol);

        for (Node n : tp.getNodes())
            sb.append(exportSensingRange(n, scale));

    }

    private String exportSensingRange(Node n, double scale) {
        return exportNode(n, scale);
    }

    private void exportNodes(StringBuilder sb, Topology tp, double scale) {
        String header = "  \\tikzstyle{every node}=[draw,circle,fill=gray,inner sep=1.5]";
        sb.append(header + eol);

        for (Node n : tp.getNodes())
            sb.append(exportNode(n, scale));

    }

    private String exportNode(Node n, double scale) {
        String id = "v"+ n;
        Point coords = getDisplayableCoords(n, scale);
        String color = getTikzStringColor(n.getColor());
        return "  \\path (" + coords.x + "," + coords.y + ") node [" + color + "] (" + id + ") {};" + eol;
    }

    private Point getDisplayableCoords(Node n, double scale) {
        double x = Math.round(n.getX()* 100 * scale) / 100.0;
        double invertedY = n.getTopology().getHeight() - n.getY();
        double y = Math.round(invertedY * 100 * scale) / 100.0;
        return new Point(x, y);
    }

    private void exportLinks(StringBuilder sb, Topology tp) {
        String header = "  \\tikzstyle{every path}=[];";
        sb.append(header + eol);

        for (Link l : tp.getLinks())
            sb.append(exportLink(l));
    }

    private String exportLink(Link l) {
        String options = "";
        options = addOption(options, getTikzStringColor(l.getColor()));
        if (l.getWidth()>1)
            options = addOption(options, "ultra thick");
        if (l.isDirected())
            options = addOption(options, "->");
        String id1 = "v" + l.source;
        String id2 = "v" + l.destination;
        return "  \\draw [" + options + "] (" + id1 + ")--(" + id2 + ");" + eol;
    }

    private String addOption(String options, String option) {
        if (options == null || options.length() == 0)
            return option;

        return options + ", " + option;
    }
}
