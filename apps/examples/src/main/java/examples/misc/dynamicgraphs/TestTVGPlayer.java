package examples.misc.dynamicgraphs;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.gen.dynamic.graph.TVG;
import io.jbotsim.gen.dynamic.graph.TVGPlayer;

public class TestTVGPlayer {
	public static void main(String args[]){
		Topology tp=new Topology(400, 400);
        new JViewer(tp);

		TVG tvg=new TVG();
		tvg.buildFromFile("./apps/examples/src/main/resources/examples/misc/dynamicgraphs/triangle.tvg");

        (new TVGPlayer(tvg, tp, 100)).start();
        tp.start();
	}
}
