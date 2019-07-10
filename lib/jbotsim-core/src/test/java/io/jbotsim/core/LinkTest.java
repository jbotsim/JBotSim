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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkTest {

    private Node n1;
    private Node n2;
    private Node n3;

    @BeforeEach
    void setUp() {
        n1 = new Node();
        n2 = new Node();
        n3 = new Node();
    }

    // region equals

    @Test
    void equals_null_notEqual() {
        Link link1 = new Link(n1, n2);

        assertNotEquals(link1, null);
    }

    @Test
    void equals_sameObject_equal() {
        Link link1 = new Link(n1, n2);

        assertEquals(link1, link1);
    }
    @Test
    void equals_sameValues_equal() {
        Link link1 = new Link(n1, n2);
        Link link2 = new Link(n1, n2);

        assertEquals(link1, link2);
    }
    @Test
    void equals_differentNodes_notEqual() {
        Link link1 = new Link(n1, n2);
        Link link2 = new Link(n1, n3);

        assertNotEquals(link1, link2);
    }
    // endregion

}