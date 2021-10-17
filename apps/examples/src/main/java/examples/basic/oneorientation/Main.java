/*
 * Copyright 2008 - 2021, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

package examples.basic.oneorientation;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.gen.basic.TopologyGenerators;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.JParentLinkPainter;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setDefaultNodeModel(ColoringNode.class);

        tp.addCommand("Build orientation");
        tp.addCommandListener(command -> {
            if (command.equals("Build orientation")) {
                List<Node> notVisited = tp.getNodes();
                while (notVisited.size() > 0)
                    DFS(notVisited);
            }
        });
        tp.addCommand("-");

        JTopology jtp = new JTopology(tp);
        jtp.addLinkPainter(new JParentLinkPainter());
        new JViewer(jtp);
    }

    /*
     * Realizes one DFS from the first non-visited node.
     * During the process, all visited nodes take as parent
     * their predecessor in the DFS.
     */
    public static void DFS(List<Node> notVisited) {
        Stack<Node> stack = new Stack<>();
        Node startNode = notVisited.get(0);
        stack.push(startNode);
        notVisited.remove(startNode);
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            for (Node ng : current.getNeighbors()) {
                if (notVisited.contains(ng)) {
                    ((ColoringNode) ng).parent = current;
                    ng.getCommonLinkWith(current).setWidth(3);
                    stack.push(ng);
                    notVisited.remove(ng);
                }
            }
        }
    }

    /*
     * Same with a BFS (queue instead of a stack).
     */
    public static void BFS(List<Node> notVisited) {
        Queue<Node> queue = new LinkedList<>();
        Node startNode = notVisited.get(0);
        queue.add(startNode);
        notVisited.remove(startNode);
        while (!queue.isEmpty()) {
            Node current = queue.remove();
            for (Node ng : current.getNeighbors()) {
                if (notVisited.contains(ng)) {
                    ((ColoringNode) ng).parent = current;
                    ng.getCommonLinkWith(current).setWidth(3);
                    queue.add(ng);
                    notVisited.remove(ng);
                }
            }
        }
    }
}
