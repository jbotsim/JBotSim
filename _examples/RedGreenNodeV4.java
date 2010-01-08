import jbotsim.Clock;
import jbotsim.Node;
import jbotsim.event.ClockListener;

public class RedGreenNodeV4 extends Node implements ClockListener{
	public RedGreenNodeV4(){
		RandomWayPoint.init(this);
		Clock.addClockListener(this, 10);
	}
	public void onClock() {
		RandomWayPoint.move(this);
		if (super.getNeighbors().size()>0)
			super.setProperty("color", "green");
		else
			super.setProperty("color", "red");
	}
}
