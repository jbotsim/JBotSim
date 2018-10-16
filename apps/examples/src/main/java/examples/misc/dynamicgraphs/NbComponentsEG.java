package examples.misc.dynamicgraphs;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.Connectivity;
import io.jbotsim.dygraph.EMEGPlayer;
import io.jbotsim.dygraph.TVG;


public class NbComponentsEG implements ClockListener{

	static Topology tp = new Topology();
	
	@Override
	public void onClock() {
		// prints the number of components in the graph
		System.out.println(Connectivity.splitIntoConnectedSets(tp.getNodes()).size());
	}

	public static void main(String[] args) {
		new JViewer(tp);
		TVG tvg=new TVG(Node.class); tvg.buildCompleteGraph(6);
		(new EMEGPlayer(tvg, tp, .05, .6)).start();
		tp.addClockListener(new NbComponentsEG());
	}
}
