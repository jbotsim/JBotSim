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
import java.io.StringWriter;
import java.util.HashMap;

/**
 * This class stores attributes of DOT elements: graph, nodes and edges.
 */
class AttributeTable {
    private HashMap<String, String> table;

    AttributeTable() {
        table = new HashMap<>();
    }

    boolean hasAttributes() {
        return ! table.isEmpty();
    }

    String getAttribute(String attr) {
        if (table.containsKey(attr))
            return table.get(attr);
        return null;
    }

    void setAttribute(String attr, String value) {
        table.put(attr, value);
    }

    void putAll(AttributeTable attributeTable) {
        if (attributeTable == null)
            return;
        table.putAll(attributeTable.table);
    }

    void prettyPrint(PrintWriter out) {
        out.print("[");
        boolean first = true;
        for (String key : table.keySet()) {
            String value = getAttribute(key);
            if (first) first = false;
            else out.print (", ");
            out.print(key + "=" + value);
        }
        out.print("]");
    }

    public String toString() {
        StringWriter str = new StringWriter();
        PrintWriter out = new PrintWriter(str);
        prettyPrint(out);
        out.flush();

        return str.getBuffer().toString();
    }
}
