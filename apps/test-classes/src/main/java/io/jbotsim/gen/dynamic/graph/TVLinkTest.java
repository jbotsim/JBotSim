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

package io.jbotsim.gen.dynamic.graph;

import io.jbotsim.core.Node;
import io.jbotsim.gen.dynamic.graph.TVLink;

public class TVLinkTest {
    public static void main(String args[]){
        TVLink l=new TVLink(new Node(), new Node());
        l.addAppearanceDate(2);
        l.addDisappearanceDate(4);
        l.addAppearanceDate(5);
        l.addDisappearanceDate(7);
        System.out.println(l);
    }
}
