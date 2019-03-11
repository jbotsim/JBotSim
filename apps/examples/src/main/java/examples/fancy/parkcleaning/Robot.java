package examples.fancy.parkcleaning;

import io.jbotsim.core.Node;
import io.jbotsim.core.event.ClockListener;

import io.jbotsim.core.Point;
import java.util.List;

public class Robot extends Node implements ClockListener{
	protected Point target;
	double step = 1;
	
	public Robot(){
		setIcon("/examples/fancy/parkcleaning/gmrobot.png");
		setIconSize(10);
		setSensingRange(30);
		disableWireless();
	}		

	public void onClock() {
		if (target != null){
			if (distance(target) > step){
				setDirection(target);
				move(step);
			}else target=null;
		}
		List<Node> sensedObjects = this.getSensedNodes();
		for (Node thing : sensedObjects)
			if (thing instanceof Garbage && thing.getColor()==getColor())
				getTopology().removeNode(thing);
	}
}
