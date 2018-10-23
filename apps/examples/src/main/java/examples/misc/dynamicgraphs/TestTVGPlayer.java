package examples.misc.dynamicgraphs;

import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;
import io.jbotsim.dynamicity.graph.TVG;
import io.jbotsim.dynamicity.graph.TVGPlayer;

public class TestTVGPlayer {
	public static void main(String args[]){
		Topology tp=new Topology(400, 400);
        new JViewer(tp);

		TVG tvg=new TVG();
		tvg.buildFromFile("triangle.tvg");

        (new TVGPlayer(tvg, tp, 100)).start();
	}
}
