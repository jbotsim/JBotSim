package io.jbotsim.geometry.toroidal.ui;

import io.jbotsim.core.Link;
import io.jbotsim.core.Point;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.painting.DefaultLinkPainter;
import io.jbotsim.ui.painting.UIComponent;

import java.awt.*;

/**
 * A link painter that draws toroidal link in a toroidal fashion.
 * Each toroidal link is drawn using two lines (one for each node).
 * The tricky part is to decide when it is toroidal and over which coordinate.
 */
public class ToroidalLinkPainter extends DefaultLinkPainter {

    protected void drawLine(Graphics2D g2d, Point p1, Point p2) {
        int srcX=(int)p1.getX(), srcY=(int)p1.getY();
        int destX=(int)p2.getX(), destY=(int)p2.getY();
        g2d.drawLine(srcX, srcY, (srcX+(destX-srcX)), (srcY+(destY-srcY)));
    }

    protected void toroidalPaint(Graphics2D g2d, Topology tp, Point p1, Point p2,
                         boolean wrapX, boolean wrapY){
        Point p1b = (Point) p1.clone();
        Point p2b = (Point) p2.clone();
        if (wrapX){
            if (p1.getX() < p2.getX()) {
                p1b.setLocation(p1b.getX() + tp.getWidth(), p1b.getY());
                p2b.setLocation(p2b.getX() - tp.getWidth(), p2b.getY());
            }else{
                p1b.setLocation(p1b.getX() - tp.getWidth(), p1b.getY());
                p2b.setLocation(p2b.getX() + tp.getWidth(), p2b.getY());
            }
        }
        if (wrapY){
            if (p1.getY() < p2.getY()) {
                p1b.setLocation(p1b.getX(), p1b.getY() + tp.getHeight());
                p2b.setLocation(p2b.getX(), p2b.getY() - tp.getHeight());
            }else{
                p1b.setLocation(p1b.getX(), p1b.getY() - tp.getHeight());
                p2b.setLocation(p2b.getX(), p2b.getY() + tp.getHeight());
            }
        }
        drawLine(g2d, p1, p2b);
        drawLine(g2d, p1b, p2);
    }

    @Override
    public void paintLink(UIComponent uiComponent, Link link) {
        Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
        Point p1 = link.endpoint(0).getLocation();
        Point p2 = link.endpoint(1).getLocation();
        Topology tp = link.getTopology();
        boolean wrapX = (Math.abs((p1.getX() - p2.getX())) > tp.getWidth()/2);
        boolean wrapY = (Math.abs((p1.getY() - p2.getY())) > tp.getHeight()/2);
        if ( ! wrapX && ! wrapY )
            // The link is normal, use default painting
            super.paintLink(uiComponent, link);
        else
            // The link is toroidal, use special painting
            toroidalPaint(g2d, tp, p1, p2, wrapX, wrapY);
    }

}
