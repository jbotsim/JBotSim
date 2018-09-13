package examples.fancy.parkcleaning;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class Main {
	public static void main(String[] args) {
		Topology tp = new Topology();
		tp.setNodeModel("UAV", UAV.class);
		tp.setNodeModel("Robot_blue", RobotBlue.class);
		tp.setNodeModel("Robot_red", RobotRed.class);
		tp.setNodeModel("Garbage_blue", GarbageBlue.class);
		tp.setNodeModel("Garbage_red", GarbageRed.class);

		new JViewer(tp);
		new RWP(tp, true);
	}
}
