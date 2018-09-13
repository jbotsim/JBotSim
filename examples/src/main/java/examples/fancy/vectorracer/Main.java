package examples.fancy.vectorracer;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsimx.ui.JViewer;
import jbotsimx.ui.painting.BackgroundPainter;

import jbotsim.Point;

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
        tp.setClockSpeed(10);
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
    public void paintBackground(Graphics2D g2d, Topology tp) {
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
