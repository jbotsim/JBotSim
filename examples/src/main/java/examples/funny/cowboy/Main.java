package examples.funny.cowboy;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsimx.ui.JViewer;
import jbotsimx.ui.painting.BackgroundPainter;

import java.awt.*;

/**
 * Created by acasteig on 17/06/15.
 */
public class Main implements ClockListener, BackgroundPainter {
    Topology tp;
    boolean finished = false;
    static Node center;

    public Main(Topology tp) {
        this.tp = tp;
        tp.addClockListener(this);
    }

    private boolean isFinished(){
        for (Node node : tp.getNodes())
            if (node instanceof Cow)
                if (node.distance(125,125) > 75)
                    return false;
        return true;
    }
    @Override
    public void onClock() {
        finished = isFinished();
    }

    @Override
    public void paintBackground(Graphics2D g, Topology tp) {
        g.drawOval(50,50,150,150);
        if (finished) {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 20));
            g.drawString("YEAH !", 300, 150);
        }
    }

    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.addNode(100, 100, Cow.farmer = new Farmer());
        tp.addNode(-1, -1, new Cow());
        tp.addNode(-1,-1, new Cow());
        tp.addNode(-1,-1, new Cow());
        JViewer jv = new JViewer(tp);
        jv.getJTopology().addBackgroundPainter(new Main(tp));
    }
}
