import jbotsim.Topology;
import jbotsim.format.common.Format;
import jbotsimx.ui.JViewer;

public class DotFormatterTest {
    // Test
    public static void main(String[] args) {
        Topology tp = new Topology();
        tp.disableWireless();
        String filename = "/home/acasteig/test.dot"; // to be updated
        Format.importFromFile(tp, filename, new jbotsim.format.dot.DotFormatter());
        new JViewer(tp);
    }
}
