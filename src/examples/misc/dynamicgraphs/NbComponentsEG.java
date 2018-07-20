package examples.misc.dynamicgraphs;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import jbotsimx.ui.JViewer;
import jbotsimx.Connectivity;
import jbotsimx.dygraph.EMEGPlayer;
import jbotsimx.dygraph.TVG;


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
