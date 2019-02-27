package examples.misc.dynamicgraphs;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;
import io.jbotsim.core.event.SelectionListener;

import io.jbotsim.core.Color;
import io.jbotsim.ui.JViewer;

import java.util.Random;

public class BoundedRecurrent implements ClockListener, SelectionListener {
	Topology tp;
	Random r=new Random();

	public BoundedRecurrent(Topology tp){
		this.tp = tp;
		tp.setTimeUnit(100);
		tp.addClockListener(this, 1);
		tp.addSelectionListener(this);
	}
	public void onClock() {
		for (Link l : tp.getLinks()){
			if (l.getWidth()>0){
				l.setWidth(0);
				l.setProperty("nextAppearance", tp.getTime()+r.nextInt(10)+1);
			}else{
				int nextAppearance = (Integer)l.getProperty("nextAppearance");
				if (tp.getTime() == nextAppearance){
					l.setWidth(2);
					doSomething(l);
				}
			}
		}
	}
	public void doSomething(Link l){
		if (l.endpoint(0).getColor()==Color.red && l.endpoint(1).getColor()!=Color.red)
			l.endpoint(1).setColor(Color.red);
		if (l.endpoint(1).getColor()==Color.red && l.endpoint(0).getColor()!=Color.red)
			l.endpoint(0).setColor(Color.red);
	}
	public static void main(String args[]){
		Topology tp=new Topology();
		new BoundedRecurrent(tp);
		new JViewer(tp);
	}

	@Override
	public void onSelection(Node node) {
		node.setColor(Color.red);
	}
}