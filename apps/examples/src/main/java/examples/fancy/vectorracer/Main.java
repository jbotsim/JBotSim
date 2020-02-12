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

package examples.fancy.vectorracer;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.BackgroundPainter;

import io.jbotsim.core.Point;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;

public class Main implements ClockListener, BackgroundPainter{
    Topology tp;
    Integer score = null;
    Point startPoint = new Point(400, 300);
    Integer binome = 4;
    Integer set = 1;

    public Main() {
        tp = new Topology(800, 600);
        tp.addClockListener(this);
        JViewer jv = new JViewer(tp);
        jv.getJTopology().addBackgroundPainter(this);
        tp.setTimeUnit(10);
        CherrySets.distribute(tp);
        tp.addNode(500, 400, new Drone());
        tp.start();
    }

//    public void distributeNodesRandom(Topology tp){
//        for (Node node : tp.getNodes())
//            System.out.println("\t tp.addNode("+Math.round(node.getX())+".0, "+Math.round(node.getY())+".0, new Cherry());");
//    }


    public boolean hasReturned(VectorNode drone){
        if (drone.getLocation().equals(startPoint) && drone.vector.distance(new Point(0,0))<VectorNode.DEVIATION) {
            return true;
        }else
            return false;
    }

    @Override
    public void onClock() {
        if (tp.getNodes().size() == 1) {
            VectorNode drone = (VectorNode) tp.getNodes().get(0);
            if (hasReturned(drone)) {
                score = (int) Math.ceil(tp.getTime() / 10.0);
                String binome = drone.getClass().toString().substring(27);
                System.out.println("Score: " + score);
            }
        }
    }

    @Override
    public void paintBackground(UIComponent uiComponent, Topology tp) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        VectorNode drone = null;
        for (Node node : tp.getNodes())
            if (node instanceof VectorNode)
                drone = (VectorNode) node;
        if (drone == null)
            return;
        g2d.drawOval((int) (drone.getX()+ drone.vector.getX()), (int)(drone.getY()+ drone.vector.getY()), 5, 5);
        if (score != null) {
            String s = "Score: "+Integer.toString(score);
            g2d.drawString(s, (int) drone.getX()+30, (int) drone.getY()+30);
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
