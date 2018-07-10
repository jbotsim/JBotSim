package jbotsimx.format.xml;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GeneratorsTest {
    private String TEST_RC_ROOT = "generators/";

    @Parameterized.Parameter
    public String testCaseName;

    @Test
    public void generatorTest() throws XMLTopologyParser.ParserException, XMLTopologyBuilder.BuilderException {
        BuilderParserTest.runBuilderParserTest(()-> {
            return ParserTest.loadXMLFile(TEST_RC_ROOT + testCaseName);
        });
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<String> makers() {
        return Arrays.asList(
                "gen-grid-1.xml",
                "gen-grid-2.xml",
                "gen-grid-3.xml",
                "gen-grid-4.xml",
                "gen-grid-5.xml",
                "gen-kn-1.xml",
                "gen-kn-2.xml",
                "gen-kn-3.xml",
                "gen-line-1.xml",
                "gen-line-2.xml",
                "gen-line-3.xml",
                "gen-ring-1.xml",
                "gen-ring-2.xml",
                "gen-ring-3.xml",
                "gen-torus-1.xml",
                "gen-torus-2.xml"
                );
    }
}
