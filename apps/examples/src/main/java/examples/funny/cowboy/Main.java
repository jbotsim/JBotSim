package examples.funny.cowboy;

import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.ui.painting.BackgroundPainter;
import io.jbotsim.ui.painting.UIComponent;

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
    public void paintBackground(UIComponent uiComponent, Topology tp) {
        Graphics2D g = (Graphics2D) uiComponent.getComponent();
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
