package examples.centralized;
import java.util.List;

import jbotsim.Link;
import jbotsim.Topology;
import jbotsim.event.*;
import jbotsimx.ui.JViewer;


public class MST implements ClockListener{
	Topology tp;
	public MST(Topology tp){
		this.tp = tp;
		tp.addClockListener(this, 10);
	}
	protected void updateMST(){
		if (jbotsimx.Connectivity.isConnected(tp)) {
            List<Link> mstLinks = jbotsimx.Algorithms.getMST(tp);
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
