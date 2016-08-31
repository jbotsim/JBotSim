package jbotsim.ui.painting;

import jbotsim.Link;
import jbotsim.Topology;

import java.awt.*;

/**
 * Created by acasteig on 10/19/15.
 */
public class LinkPainter {
    /**
     * Paints the Links.
     * @param g2d The graphics object
     * @param link The link to be drawn
     */
    public void paintLink(Graphics2D g2d, Link link) {
        Integer width=link.getWidth();
        if (width==0)
            return;
        g2d.setColor(link.getColor());
        g2d.setStroke(new BasicStroke(width));
        int srcX=(int)link.source.getX(), srcY=(int)link.source.getY();
        int destX=(int)link.destination.getX(), destY=(int)link.destination.getY();
        g2d.drawLine(srcX, srcY, (srcX+(destX-srcX)), (srcY+(destY-srcY)));
        Topology topology = link.source.getTopology();
        if (topology.hasDirectedLinks()) {
            int x=srcX+4*(destX-srcX)/5-2;
            int y=srcY+4*(destY-srcY)/5-2;
            g2d.drawOval(x,y,4,4);
        }
    }
}
