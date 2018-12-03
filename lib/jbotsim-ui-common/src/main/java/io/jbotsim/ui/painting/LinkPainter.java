package io.jbotsim.ui.painting;

import io.jbotsim.core.Link;

/**
 * <p>The {@link LinkPainter} is an element which is used to be called while trying to draw a {@link Link}.</p>
 */
public interface LinkPainter {
    /**
     * Paints the Links.
     *
     * @param uiComponent the graphical object to be drawn on.
     * @param link the {@link Link} to be drawn.
     */
    void paintLink(UIComponent uiComponent, Link link);
}
