/*
 * Copyright 2008 - 2020, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
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

/**
 * <p>The {@link UIComponent} contains a platform-dependent object to be used for displaying graphical elements.</p>
 *
 * <p>Its sole member is a simple {@link Object}. The typical usage is:</p>
 * <ol>
 *     <li>wrap the system UI component (say <code>View</code>) in the {@link UIComponent} when a (re)draw is prompted
 *     by the system;</li>
 *     <li>pass this {@link UIComponent} to the object able draw on it;</li>
 *     <li>in this object, cast the result of {@link #getComponent()} back to the system UI component type
 *     (<code>View</code>) and use it normally, see <code>DefaultBackgroundPainter</code> example below.</li>
 * </ol>
 *
 * <pre>
 *  {@code
 *
 * public class DefaultBackgroundPainter implements BackgroundPainter {
 *     
 *     public void paintBackground(UIComponent uiComponent, Topology tp) {
 *         Graphics2D g2d = (Graphics2D) uiComponent.getComponent();
 *         g2d.setStroke(new BasicStroke(1));
 *         for (Node n : tp.getNodes()) {
 *             double sR = n.getSensingRange();
 *             if (sR > 0) {
 *                 g2d.setColor(Color.gray);
 *                 g2d.drawOval((int) n.getX() - (int) sR, (int) n.getY() - (int) sR, 2 * (int) sR, 2 * (int) sR);
 *             }
 *         }
 *     }
 *  }
 * }
 * </pre>
 */
public class UIComponent {
    private Object component;

    /**
     * <p>Creates a {@link UIComponent} by wrapping the provided system {@link Object}</p>
     * @param component the {@link Object} to be wrapped
     */
    public UIComponent(Object component) {
        this.component = component;
    }

    /**
     * Gets the {@link Object} that has been provided upon creation.
     *
     * @return the component, as an {@link Object} to be cast
     */
    public Object getComponent()
    {
        return component;
    }
}
