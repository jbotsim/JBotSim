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
package io.jbotsim.gen.dynamic.trace;

public class TraceEvent {
    public enum EventKind {
        ADD_NODE, DEL_NODE, SELECT_NODE, MOVE_NODE, START_TOPOLOGY
    }

    private EventKind kind;
    private int time;
    private int id;
    private double x;
    private double y;
    private String nodeClass;

    protected TraceEvent(int time, EventKind kind) {
        this(time, -1, kind);
    }

    protected TraceEvent(int time, int id, EventKind kind) {
        this.id = id;
        this.time = time;
        this.kind = kind;
    }

    public static TraceEvent newStartTopology(int time) {
        return new TraceEvent(time, EventKind.START_TOPOLOGY);
    }

    public static TraceEvent newAddNode(int time, int id, double x, double y, String className) {
        TraceEvent result = new TraceEvent(time, id, EventKind.ADD_NODE);
        result.x = x;
        result.y = y;
        result.nodeClass = className;

        return result;
    }

    public static TraceEvent newDeleteNode(int time, int id) {
        return new TraceEvent(time, id, EventKind.DEL_NODE);
    }

    public static TraceEvent newSelectNode(int time, int id) {
        return new TraceEvent(time, id, EventKind.SELECT_NODE);
    }

    public static TraceEvent newMoveNode(int time, int id, double x, double y) {
        TraceEvent result = new TraceEvent(time, id, EventKind.MOVE_NODE);
        result.x = x;
        result.y = y;

        return result;
    }

    public EventKind getKind() {
        return kind;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getNodeClass() {
        return nodeClass;
    }

    public int getTime() {

        return time;
    }

    public int getNodeID() {
        return id;
    }
}
