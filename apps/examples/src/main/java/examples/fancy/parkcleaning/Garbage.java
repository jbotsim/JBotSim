package examples.fancy.parkcleaning;

import io.jbotsim.core.Node;

public class Garbage extends Node{
	public Garbage(){
		disableWireless();
		setIcon("/examples/fancy/parkcleaning/gmgarbage.png");
		setIconSize(12);
	}
}
