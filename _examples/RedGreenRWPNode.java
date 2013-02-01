package _examples;

import jbotsim.event.ClockListener;

public class RedGreenRWPNode extends RWPNode implements ClockListener{
	public RedGreenRWPNode(){
		super();
	}
	public void onClock(){
		super.onClock();
		if (this.getNeighbors().size()>0)
			this.setProperty("color", "green");
		else
			this.setProperty("color", "red");
	}
}
