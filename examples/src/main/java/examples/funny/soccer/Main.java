package examples.funny.soccer;

import jbotsim.Topology;
import jbotsimx.ui.JTopology;
import jbotsimx.ui.JViewer;
import jbotsimx.ui.painting.BackgroundPainter;

import java.awt.*;


/**
 * Created by Arnaud Casteigts on 06/04/17.
 */
public class Main {
    public static void main(String[] args) {
        int unit = 6;
        Topology tp = new Topology(105*unit+200, 68*unit+200);
        tp.setClockSpeed(50);
        tp.addNode(100, 100, new Ball());
        tp.setDefaultNodeModel(Robot.class);
        JTopology jtp = new JTopology(tp);
        jtp.addBackgroundPainter(new BackgroundPainter() {
            @Override
            public void paintBackground(Graphics2D g2d, Topology tp) {
                g2d.setColor(new Color(0, 156, 0));
                g2d.fillRect(100,100,tp.getWidth()-200,tp.getHeight()-200);
                g2d.setColor(Color.white);
                g2d.drawOval(tp.getWidth()/2-60, tp.getHeight()/2-60, 120, 120);
            }
        });
        new JViewer(jtp);
        tp.start();
    }
}
