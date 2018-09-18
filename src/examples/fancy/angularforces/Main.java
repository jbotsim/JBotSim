package examples.fancy.angularforces;

import jbotsim.Topology;
import jbotsimx.ui.JViewer;

/**
 * Created by Arnaud Casteigts on 15/03/17.
 */
public class Main {
    public static void main(String args[]) throws Exception{
        Topology tp=new Topology(400,300);
        tp.setCommunicationRange(70);
        Forces.Dth=tp.getCommunicationRange()*0.851;
        tp.setSensingRange(Forces.Dth / 2);
        tp.setDefaultNodeModel(Robot.class);
        tp.setClockSpeed(6);
        new JViewer(tp);
        tp.start();
    }
}
