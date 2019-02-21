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
package io.jbotsim.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    public static final int DEFAULT_ICON_SIZE = 10;

    private Node createDefaultNode() {
        return new Node();
    }

    // region iconSize
    @Test
    void iconSize_defaultValue_ok() {
        Node node = createDefaultNode();
        assertEquals(DEFAULT_ICON_SIZE, node.getIconSize());
    }

    @Test
    void iconSize_setZero_ok() {
        testIconSizeGetterSetter(0);
    }

    @Test
    void iconSize_setNegative_ok() {
        testIconSizeGetterSetter(-15);
    }

    @Test
    void iconSize_setPositive_ok() {
        testIconSizeGetterSetter(15);
    }

    private void testIconSizeGetterSetter(int testedSize) {
        Node node = createDefaultNode();
        node.setIconSize(testedSize);
        assertEquals(testedSize, node.getIconSize());
    }

    // endregion
}