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

package io.jbotsim.io.format.dot;

import java.io.PrintWriter;

/**
 * Simple data structure to represent nodes of DOT graphs.
 */
class DotNode extends AttributeTable {
    private String id;

    DotNode(String id) {
        super();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    void prettyPrint(PrintWriter out) {
        out.print(getId());
        if (hasAttributes())
            out.print(super.toString());
        out.print(";");
        out.flush();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof DotNode && id.equals(((DotNode)obj).id));
    }
}
