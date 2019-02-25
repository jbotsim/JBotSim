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
package io.jbotsim.io.format;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * A {@link TopologySerializerFilenameMatcher} is able to provide a set of {@link TopologySerializer} linked to filenames via regular expressions.
 */
public class TopologySerializerFilenameMatcher {

    private List<SupportedSerializer> supportedSerializers = new ArrayList<>();

    /**
     * Retrieves a {@link TopologySerializer} applicable for the provided filename.
     *
     * @param filename a filename
     * @return a {@link TopologySerializer} applicable to the provided filename
     */
    public TopologySerializer getTopologySerializerFor(String filename) {
        for(SupportedSerializer fmt : supportedSerializers) {
            if (Pattern.matches(fmt.regex, filename)) {
                return fmt.topologySerializer;
            }
        }
        return null;
    }

    /**
     * Records and links the provided {@link TopologySerializer} to a specific regular expression.
     *
     * @param regexp a regular expression matching the file names
     * @param topologySerializer the {@link TopologySerializer} to be recorded
     */
    public void addTopologySerializer(String regexp, TopologySerializer topologySerializer) {
        supportedSerializers.add(new SupportedSerializer(regexp, topologySerializer));
    }

    private static class SupportedSerializer {
        String regex;
        TopologySerializer topologySerializer;

        public SupportedSerializer(String regex, TopologySerializer topologySerializer) {
            this.regex = regex;
            this.topologySerializer = topologySerializer;
        }
    }
}
