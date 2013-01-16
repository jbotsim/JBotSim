package _examples;

import java.awt.Point;
import java.util.Random;

import jbotsim.Node;

public class RandomWayPoint{
	private static Random r=new Random();
    public static void init(Node node){
    	node.setProperty("target", new Point(r.nextInt(400), r.nextInt(300)));
    }
	public static void move(Node node){
		Point target=(Point)node.getProperty("target");
        node.setDirection(target);
    	node.move(5);
        if(node.distance(target)<5)
        	node.setProperty("target", new Point(r.nextInt(400), r.nextInt(300)));
	}
}