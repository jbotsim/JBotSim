/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

package io.jbotsim.ui.painting;

import io.jbotsim.core.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>The {@link ArrowTipPathGenerator} creates a path corresponding to an arrow tip.</p>
 *
 * <p>The path provided by {@link #getPath(Point, Point, double)} is properly oriented relative to the underlying arrow.
 * When the length of the underlying arrow gets shorter then the desired one, a shrinking ratio is gradually applied
 * so that the arrow tip remains visible.</p>
 */
public class ArrowTipPathGenerator {

    private final double expectedTipOpeningAngle;
    private final double expectedTipLength;
    private final double tipShrinkingRatio;
    private final double shrunkenLength;

    /**
     * Creates an {@link ArrowTipPathGenerator}.
     *
     * @param tipOpeningAngle the angle by which the arrow tip should be open, in radians.
     * @param tipLength the expected length of the arrow tip, when not constrained.
     * @param tipShrinkingRatio the ratio of the arrow length that the tip should take when the available arrow
     *                           length is shorter than the expected tipLength. A double between <code>0</code> and
     *                           <code>1</code>.
     */
    public ArrowTipPathGenerator(double tipOpeningAngle, double tipLength, double tipShrinkingRatio) {
        this.expectedTipOpeningAngle = tipOpeningAngle;
        this.expectedTipLength = tipLength;
        this.tipShrinkingRatio = tipShrinkingRatio;
        this.shrunkenLength = expectedTipLength * tipShrinkingRatio;

    }

    /**
     * <p>Computes a path for the tip of an arrow going from provided the source {@link Point} to the destination
     * {@link Point}.</p>
     *
     * @param source the {@link Point} from which the arrow starts. Nothing to be displayed here.
     * @param destination the {@link Point} to which the arrow points. The arrow tip is to be displayed on this side.
     *
     * @return a {@link List} of {@link Point}s representing the arrow tip.
     */
    public List<Point> getPath(Point source, Point destination, double destinationIconSize) {
        // Note: variable names consider a link with :
        // - the destination point being on the top right and
        // - the source point being on the bottom left

        double linkAngle = computeLinkAngle(source, destination);
        double usableLinkLength = computeUsableLinkLength(source, destination, destinationIconSize);

        double sidedArrowTipOpening = expectedTipOpeningAngle / 2;

        double originX = destination.x + destinationIconSize * Math.cos(linkAngle);
        double originY = destination.y + destinationIconSize * Math.sin(linkAngle);

        double tipLength = computeTipLength(usableLinkLength);
        double topCornerAngleToOrigin = linkAngle + sidedArrowTipOpening;
        double topCornerX = originX + tipLength * Math.cos(topCornerAngleToOrigin);
        double topCornerY = originY + tipLength * Math.sin(topCornerAngleToOrigin);

        double centerX = originX + 0.8 * tipLength * Math.cos(linkAngle);
        double centerY = originY + 0.8 * tipLength * Math.sin(linkAngle);

        double bottomCornerAngleToOrigin = linkAngle - sidedArrowTipOpening;
        double bottomCornerX = originX + tipLength * Math.cos(bottomCornerAngleToOrigin);
        double bottomCornerY = originY + tipLength * Math.sin(bottomCornerAngleToOrigin);

        List<Point> points = new ArrayList<>();
        points.add(new Point(originX, originY));
        points.add(new Point(topCornerX, topCornerY));
        points.add(new Point(centerX, centerY));
        points.add(new Point(bottomCornerX, bottomCornerY));

        return points;
    }

    /**
     * Compute the angle "under" the link at the arrow tip.
     *
     * @param source the source {@link Point}
     * @param destination the destination {@link Point}
     * @return the link angle
     */
    private double computeLinkAngle(Point source, Point destination) {
        double diffX = source.x - destination.x;
        double diffY = source.y - destination.y;

        // compute base angle
        double angle = Math.atan(diffY / diffX);

        // rotate the angle if the source is on the left
        if(source.x < destination.x)
            angle += Math.PI;

        return angle;
    }

    /**
     * Compute the euclidean distance between the two points
     * @param p1 the first {@link Point}
     * @param p2 the second {@link Point}
     * @param iconSize the size of the icons
     * @return the euclidean distance between the provided points, as a double
     */
    private double computeUsableLinkLength(Point p1, Point p2, double iconSize) {
        return p1.distance(p2) - iconSize * 2; // we suppose that both icons have the same size
    }

    /**
     * <p>Compute the appropriate tip length with respect with the current linkLength.</p>
     *
     * @param linkLength the length of the current link.
     * @return the appropriate tip length, as a double.
     */
    private double computeTipLength(double linkLength) {
        double adaptationPhaseStart = expectedTipLength * 1.8;
        double adaptationPhaseStop = expectedTipLength * tipShrinkingRatio;

        // "normal" phase
        if(linkLength > adaptationPhaseStart)
            return expectedTipLength;

        // adapt length from "normal" to "shrinking" phase
        if(linkLength > adaptationPhaseStop)
            return computeAdaptedTipLength(linkLength, expectedTipLength, shrunkenLength, adaptationPhaseStart, adaptationPhaseStop);

        // "shrinking" phase
        return shrunkenLength;
    }

    /**
     * <p>Compute the tip length during the adaptation phase</p>
     * <p>The </p>
     * @param linkLength the available link length
     * @param displayedLengthBefore the length of the tip when it has not started to shrink (just before the
     *                              adaptation phase).
     * @param displayedLengthAfter the length of the tip when it enters the shrinking phase (just after the
     *                             adaptation phase).
     * @param startThreshold the distance to the destination at which the adaptation phase starts
     * @param stopThreshold the distance to the destination at which the adaptation phase stops
     * @return the tip length
     */
    private double computeAdaptedTipLength(double linkLength, double displayedLengthBefore, double displayedLengthAfter, double startThreshold, double stopThreshold) {
        double adaptationLength = startThreshold - stopThreshold;
        double adaptationRatio = Math.abs(linkLength - startThreshold) / adaptationLength;

        double actualTipLengthRun = displayedLengthBefore - displayedLengthAfter;
        double toBeRemoved = adaptationRatio * actualTipLengthRun;
        return displayedLengthBefore - toBeRemoved;
    }
}
