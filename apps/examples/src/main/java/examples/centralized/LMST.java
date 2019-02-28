package examples.centralized;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.Properties;
import io.jbotsim.core.event.ConnectivityListener;
import io.jbotsim.core.event.MovementListener;
import io.jbotsim.core.event.PropertyListener;
import io.jbotsim.ui.JViewer;
import io.jbotsim.contrib.algos.Algorithms;

import java.util.HashMap;
import java.util.List;


public class LMST implements ConnectivityListener, MovementListener, PropertyListener{
	Topology tp;
	public LMST(Topology tp){
		this.tp =tp;
		tp.addMovementListener(this);
        tp.addConnectivityListener(this);
	}
	@Override
	public void onMovement(Node n) {
		updateLMST();
	}
    @Override
    public void onLinkAdded(Link link) {
        updateLMST();
    }

    @Override
    public void onLinkRemoved(Link link) {
        updateLMST();
    }
	public void onPropertyChanged(Properties o, String key) {
		updateLMST();	
	}

	public void updateLMST(){
        HashMap<Node,List<Link>> allLocalMSTs=new HashMap<Node,List<Link>>();
		for (Node node : tp.getNodes()){
			List<Node> N=node.getNeighbors();
			N.add(node);
			allLocalMSTs.put(node, Algorithms.getMST(N));
		}
		for (Link l : tp.getLinks()){
			List<Link> mst1=allLocalMSTs.get(l.source);
			List<Link> mst2=allLocalMSTs.get(l.destination);
			if (mst1.contains(l) && mst2.contains(l)) {
                l.setWidth(3);
            }else
				l.setWidth(1);
		}		
	}
	
	public static void main(String args[]){
		Topology topo=new Topology();
		new JViewer(topo);
		new LMST(topo);
	}

}
