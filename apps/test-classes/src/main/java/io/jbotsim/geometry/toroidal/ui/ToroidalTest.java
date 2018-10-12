package io.jbotsim.geometry.toroidal.ui;

import io.jbotsim.Topology;
import io.jbotsim.geometry.toroidal.ToroidalLinkResolver;
import io.jbotsim.geometry.toroidal.ui.ToroidalLinkPainter;
import io.jbotsim.ui.JViewer;

/**
 * Created by acasteig on 30/08/16.
 */
public class ToroidalTest {
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.setLinkResolver(new ToroidalLinkResolver());
        JViewer jv = new JViewer(tp);
        jv.getJTopology().setLinkPainter(new ToroidalLinkPainter());
        for (int i = 0; i < 10; i++)
            tp.addNode(-1, -1);
    }
}
