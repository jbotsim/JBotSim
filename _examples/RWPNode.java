package _examples;

import java.awt.geom.Point2D;
import java.util.Random;

import jbotsim.Clock;
import jbotsim.Node;
import jbotsim.event.ClockListener;

public class RWPNode extends Node implements ClockListener{
	Random r=new Random();
	Point2D.Double target;
	final int step = 4;
	
    public RWPNode(){
        Clock.addClockListener(this, 5);
        target = new Point2D.Double(r.nextInt(400), r.nextInt(300));
    }
    public void onClock() {
        this.setDirection(target);
    	this.move(step);
        if(this.distance(target)<step){
            target = new Point2D.Double(r.nextInt(400), r.nextInt(300));
        }
    }
}
