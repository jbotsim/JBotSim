import jbotsim.Topology;
import jbotsim.format.common.Format;
import jbotsim.format.plain.PlainFormatter;
import jbotsimx.ui.JViewer;

public class PlainFormatterTest {
    // Test
    public static void main(String[] args) {
        String filename = "/home/acasteig/test.plain"; // to be updated
        Topology tp = Format.importFromFile(filename, new PlainFormatter());
        new JViewer(tp);
    }
}
