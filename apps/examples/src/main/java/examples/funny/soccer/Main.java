package examples.funny.soccer;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JTopology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;


/**
 * Created by Arnaud Casteigts on 06/04/17.
 */
public class Main {
    public static void main(String[] args) {
        int unit = 6;
        Topology tp = new Topology(105*unit+200, 68*unit+200);
        tp.setTimeUnit(50);
        tp.addNode(100, 100, new Ball());
        tp.setDefaultNodeModel(Robot.class);
        JTopology jtp = new JTopology(tp);
        jtp.addBackgroundPainter(new BackgroundPainter() {
            @Override
            public void paintBackground(UIComponent uiComponent, Topology tp) {
                Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
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
