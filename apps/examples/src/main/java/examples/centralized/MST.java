package examples.centralized;

import io.jbotsim.Algorithms;
import io.jbotsim.Connectivity;
import io.jbotsim.core.Link;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.ui.JViewer;

import java.util.List;


public class MST implements ClockListener {
	Topology tp;
	public MST(Topology tp){
		this.tp = tp;
		tp.addClockListener(this, 10);
	}
	protected void updateMST(){
		if (Connectivity.isConnected(tp)) {
            List<Link> mstLinks = Algorithms.getMST(tp);
            for (Link l : tp.getLinks())
                l.setWidth(mstLinks.contains(l) ? 5 : 0);
        }else{
            for (Link l : tp.getLinks())
                l.setWidth(1);
        }
	}
	public static void main(String[] args) {
		Topology tp = new Topology();
		new MST(tp);
		new JViewer(tp);
	}

    @Override
    public void onClock() {
        updateMST();
    }
}
