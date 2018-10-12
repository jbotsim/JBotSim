package examples.fancy.parkcleaning;

import io.jbotsim.Node;

public class Garbage extends Node{
	public Garbage(){
		disableWireless();
		setIcon("/examples/fancy/parkcleaning/gmgarbage.png");
		setSize(12);
	}
}
