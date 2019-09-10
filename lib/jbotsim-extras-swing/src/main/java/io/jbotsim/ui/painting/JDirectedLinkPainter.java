package io.jbotsim.ui.painting;


import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.List;

import java.awt.*;
import java.awt.geom.GeneralPath;

/**
 * <p>The {@link JDirectedLinkPainter} allows to draw directed links with arrow-tips at the destination end of
 * directed links.</p>
 * <p>The default length of the arrow-tip is {@link JDirectedLinkPainter#DEFAULT_HEAD_LENGTH} and its opening angle is
 * defined by {@link JDirectedLinkPainter#DEFAULT_HEAD_OPENING_ANGLE}.</p>
 * <p>When the link's length gets too small for the arrow to be properly displayed, the arrow-tip is gradually shrunken,
 * stopping at the {@link JDirectedLinkPainter#DEFAULT_SHRUNKEN_ARROW_RATIO} ratio.</p>
 * <p>Note: the process of drawing the arrow-tip consumes more resources than the default behavior proposed by the
 * {@link JLinkPainter}.</p>
 */
public class JDirectedLinkPainter extends JLinkPainter {

    /**
     * The default length of the arrow-tip: {@value JDirectedLinkPainter#DEFAULT_HEAD_LENGTH}.
     */
    public static final double DEFAULT_HEAD_LENGTH = Node.DEFAULT_ICON_SIZE;

    /**
     * The default opening angle of the arrow-tip: {@value JDirectedLinkPainter#DEFAULT_HEAD_OPENING_ANGLE}.
     */
    public static final double DEFAULT_HEAD_OPENING_ANGLE = Math.PI / 5;

    /**
     * The default shrunken ratio for the arrow-tip: {@value JDirectedLinkPainter#DEFAULT_SHRUNKEN_ARROW_RATIO}.
     */
    public static final double DEFAULT_SHRUNKEN_ARROW_RATIO = 0.3;

    private final ArrowTipPathGenerator arrowTipDrawer;

    /**
     * Creates a {@link JDirectedLinkPainter} with default values.
     */
    public JDirectedLinkPainter() {
        this(DEFAULT_HEAD_OPENING_ANGLE, DEFAULT_HEAD_LENGTH, DEFAULT_SHRUNKEN_ARROW_RATIO);
    }

    /**
     * Creates a {@link JDirectedLinkPainter}.
     * @param headOpeningAngle the opening angle of the arrow-tip (rad).
     * @param headLength the length of the arrow-tip, as a double.
     * @param shrunkenArrowRatio the ratio ot the normal length up to which the arrow-tip should be shrinkable, as a double.
     */
    public JDirectedLinkPainter(double headOpeningAngle, double headLength, double shrunkenArrowRatio) {
        arrowTipDrawer = new ArrowTipPathGenerator(headOpeningAngle, headLength, shrunkenArrowRatio);
    }

    @Override
    protected void drawDestinationPartIfNeeded(Graphics2D g2d, Link link) {
        if (isDirected(link)) {
            io.jbotsim.core.Point source = link.source.getLocation();
            io.jbotsim.core.Point destination = link.destination.getLocation();
            double destinationIconSize = link.destination.getIconSize() ;
            printDirectLinkEnd(g2d, source, destination, destinationIconSize);
        }
    }

    protected void printDirectLinkEnd(Graphics2D g2d, io.jbotsim.core.Point source, io.jbotsim.core.Point destination, double destinationIconSize) {
        List<Point> path = arrowTipDrawer.getPath(source, destination, destinationIconSize);

        new SwingUtil().drawAndFillWithPoints(g2d, path);
    }

    class SwingUtil {
        /**
         * Draw and fill a path described by an arbitrary long set of intermediate points.
         * The first point will be considered as the starting point and the last point of the path.
         *
         * @param g a {@link Graphics2D} object to be drawn on
         * @param pathPoints the set of intermediate points (x, y)*. Can be null or empty.
         */
        protected void drawAndFillWithPoints(Graphics2D g, List<Point> pathPoints) {
            if(pathPoints == null || pathPoints.isEmpty())
                return;

            GeneralPath path = new GeneralPath();

            Point originPoint = pathPoints.remove(0);
            path.moveTo(originPoint.getX(), originPoint.getY());

            for(Point p: pathPoints)
                path.lineTo(p.getX(), p.getY());

            path.closePath();
            g.fill(path);
            g.draw(path);
        }

    }


}
