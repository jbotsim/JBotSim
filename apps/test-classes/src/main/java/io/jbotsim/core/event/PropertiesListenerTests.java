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

package io.jbotsim.core.event;

import io.jbotsim.core.Node;
import io.jbotsim.core.Properties;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class PropertiesListenerTests {
    public static void main(String[] args) {
        Topology topology = new Topology();
        topology.setTimeUnit(1);


        Node node = new Node();
        topology.addNode(-1, -1, node);

        for (int i=0; i < 2; i++) {
            MyListener listener = new MyListener();
            node.addPropertyListener(listener);
            topology.addSelectionListener(listener);
        }

        new JViewer(topology);
        topology.start();

        System.out.println("Please select the node to change its property");
    }

    private static class MyListener implements PropertyListener, SelectionListener {
        protected static final String THE_PROPERTY = "the property";
        private int count = 1;

        private void switchListener(Properties o) {
            o.removePropertyListener(this);
            o.addPropertyListener(new MyListener());
        }

        @Override
        public void onPropertyChanged(Properties o, String key) {

            if (!key.equals(THE_PROPERTY))
                return;

            count--;
            Integer property = (Integer) o.getProperty(THE_PROPERTY);
            if (property == null)
                property = new Integer(0);

            System.out.println("[" + toString() + "]: Property: " + property + "");
            if (count <= 0)
                switchListener(o);

        }

        @Override
        public void onSelection(Node node) {
            Integer property = (Integer) node.getProperty(THE_PROPERTY);
            if(property == null)
                property = new Integer(0);

            node.setProperty(THE_PROPERTY, property+1);
        }
    }
}
