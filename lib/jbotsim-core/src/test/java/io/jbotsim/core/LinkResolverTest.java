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

class LinkResolverTest {

    public static final Point POINT_ORIGIN = new Point(0, 0);
    private LinkResolver resolver;
    private Node n2;
    private Node n1;

    @BeforeEach
    void setUp() {
        resolver = new LinkResolver();
        n1 = createNode(POINT_ORIGIN, 20);
        n2 = createNode(new Point(10, 10), 5);
    }

    private Node createNode(Point location, double commRange) {
        Node node = new Node();
        node.setCommunicationRange(commRange);
        node.setLocation(location);
        return node;
    }

    //region isHeardBy

    private void testHeardBy(Node n1, Node n2) {
        boolean heard = resolver.isHeardBy(n1, n2);
        assertTrue(heard);
    }

    private void testNotHeardBy(Node n1, Node n2) {
        boolean heard = resolver.isHeardBy(n1, n2);
        assertFalse(heard);
    }

    //region Basic behavior
    @Test
    void isHeardBy_properNodesInRange_heard() {
        testHeardBy(n1, n2);
    }

    @Test
    void isHeardBy_properNodesNotInRange_notHeard() {
        testNotHeardBy(n2, n1);
    }
    //endregion

    //region Incorrect parameters
    @Test
    void isHeardBy_n1Null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            resolver.isHeardBy(null, n2);
        });
    }
    @Test
    void isHeardBy_n2Null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            resolver.isHeardBy(n1, null);
        });
    }
    @Test
    void isHeardBy_n1n2Null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> {
            resolver.isHeardBy(null, null);
        });
    }
    //endregion


    //region Wireless needed
    @Test
    void isHeardBy_n1NoWireless_notHeard() {
        n1.setWirelessStatus(false);

        testNotHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_n2NoWireless_notHeard() {
        n2.setWirelessStatus(false);

        testNotHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_n1n2NoWireless_notHeard() {
        n1.setWirelessStatus(false);
        n2.setWirelessStatus(false);

        testNotHeardBy(n1, n2);
    }
    //endregion


    //region Boundaries
    @Test
    void isHeardBy_n2AwayOnXaxis_notHeard() {
        n2.setLocation(n1.communicationRange+1, 0, 0);

        testNotHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_atLimintOnXaxis_heard() {
        n2.setLocation(n1.communicationRange, 0, 0);
        testHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_n2AwayOnYaxis_notHeard() {
        n2.setLocation(0, n1.communicationRange+1, 0);

        testNotHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_atLimitOnYaxis_heard() {
        n2.setLocation(0, n1.communicationRange, 0);

        testHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_n2AwayOnZaxis_notHeard() {
        n2.setLocation(0, 0, n1.communicationRange+1);

        testNotHeardBy(n1, n2);
    }
    @Test
    void isHeardBy_atLimintOnZaxis_heard() {
        n2.setLocation(0, 0, n1.communicationRange);

        testHeardBy(n1, n2);
    }

    //endregion
    //endregion
}