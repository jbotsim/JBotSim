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
package io.jbotsim.io.format.dot;

import io.jbotsim.core.Topology;
import io.jbotsim.io.format.xml.XMLTopologySerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static io.jbotsim.io.format.dot.DotImportExportCoherencyTest.reorderLines;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class DotTopologySerializerTest {
    private static final boolean UPDATE_RESULTS = false;
    private static final String TEST_RC_ROOT = "/dotinputs/";

    @Parameterized.Parameter
    public String dotFileName;

    @Parameterized.Parameters(name="{index}: {0}")
    public static Collection<String> makers() {
        return Arrays.asList(
                "hm-01.gv",
                "arboricity-100-2.gv",
                "barbell-6-5-4.gv",
                "cactus-20.gv",
                "kstar-20-2.gv",
                "gabriel-50.gv",
                "paley-10.gv",
                "paley-10.xdot",
                "sunlet-10-directed.gv");
    }

    private void updateExpectedResult(String result) throws IOException {
        File resFile = new File(dotFileName + "-res.xml");
        FileOutputStream out = new FileOutputStream(resFile);
        out.write(result.getBytes());
        out.flush();
        out.close();
    }

    @Test
    public void dotParserTest() throws IOException {
        URL url = getClass().getResource(TEST_RC_ROOT + dotFileName);

        Topology tp = new Topology();

        String fileContent = tp.getFileManager().read(url.getPath());
        new DotTopologySerializer().importFromString(tp, fileContent);
        assertNotNull(tp);

        String xmlTp = new XMLTopologySerializer(true).exportToString(tp);
        assertNotNull(xmlTp);
        if (UPDATE_RESULTS)
            updateExpectedResult(xmlTp);
        String expectedXml = new String(Files.readAllBytes(Paths.get(url.getPath()+"-res.xml")));

        assertEquals(reorderLines(expectedXml), reorderLines(xmlTp));
    }
}
