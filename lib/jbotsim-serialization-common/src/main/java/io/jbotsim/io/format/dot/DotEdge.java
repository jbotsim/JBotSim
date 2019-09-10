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

package io.jbotsim.io.format.dot;

import java.io.PrintWriter;

/**
 * Simple data structure to represent edges of DOT graphs.
 */
class DotEdge extends AttributeTable {
    private boolean directed;
    private DotNode src;
    private DotNode dst;

    DotEdge(DotNode src, DotNode dst) {
        super();
        assert src != null;
        this.src = src;
        assert dst != null;
        this.dst = dst;
    }

    public DotNode getSrc() {
        return src;
    }

    public DotNode getDst() {
        return dst;
    }

    public DotNode getNode1() {
        return getSrc();
    }

    public DotNode getNode2() {
        return getDst();
    }

    public boolean isDirected() {
        return directed;
    }

    public void setDirected(boolean directed) {
        this.directed = directed;
    }

    @Override
    void prettyPrint(PrintWriter out) {
        out.print(src.getId());
        out.print(isDirected() ? " -> " : "--");
        out.print(dst.getId());
        out.print(" ");
        if (hasAttributes())
            out.print(super.toString());
        out.print(";");
        out.flush();
    }
}
