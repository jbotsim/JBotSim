package io.jbotsim.serialization.dot;

import io.jbotsim.core.Topology;
import io.jbotsim.serialization.xml.XMLTopologySerializer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class DotTopologySerializerTest {
    private static final String TEST_RC_ROOT = "/dotinputs/";

    @Parameterized.Parameter
    public String dotFileName;

    @Parameterized.Parameters(name="{index}: {0}")
    public static Collection<String> makers() {
        return Arrays.asList(
                "arboricity-100-2.dot",
                "barbell-6-5-4.dot",
                "cactus-20.dot",
                "kstar-20-2.dot",
                "paley-10.dot",
                "paley-10.xdot",
                "sunlet-10-directed.dot");
    }

    @Test
    public void dotParserTest() throws IOException {
        URL url = getClass().getResource(TEST_RC_ROOT + dotFileName);

        Topology tp = new Topology();

        String fileContent = tp.getFileManager().read(url.getPath());
        new DotTopologySerializer().importTopology(tp, fileContent);
        assertNotNull(tp);

        String xmlTp = new XMLTopologySerializer(true).exportTopology(tp);
        assertNotNull(xmlTp);
//        String bkupFileName = url.getPath() + "-res.xml";
//        System.out.println(bkupFileName);
//        Format.exportToFile(tp, bkupFileName);

        String expectedXml = new String(Files.readAllBytes(Paths.get(url.getPath()+"-res.xml")));

        assertEquals(expectedXml, xmlTp);
    }
}
