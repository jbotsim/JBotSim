/*
 * This file is part of JBotSim.
 * 
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsim.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jbotsim.Clock;
import jbotsim.Node;
import jbotsim.Topology;

/**
 * The viewer includes a central jtopology which will draw the attached 
 * topology and offer interaction, as well as contextual commands to add or
 * remove a communication range or sensing range tuners (slider bars), or to 
 * pause/resume the system clock.
 */
public class JViewer{
	protected JTopology jtp;
	protected JSlider cRTuner, sRTuner;
	protected JFrame window=null;
	protected EventHandler handler=new EventHandler();
	/**
	 * Creates a windowed viewer for the specified topology. 
	 * @param topo The topology to be drawn and/or manipulated.
	 */
    public JViewer(Topology topo){
    	this(topo, true);
    }
	/**
	 * Creates a viewer for the specified topology. If <tt>selfContained</tt>
	 * is <tt>true</tt>, a new window will be created to contain the viewer
	 * (similarly to <tt>JViewer(Topology)</tt>). If it is <tt>false</tt>, 
	 * no window will be created and the viewer can be subsequently
	 * integrated to another swing container (e.g. another <tt>JFrame</tt> 
	 * or a <tt>JApplet</tt>).
	 * @param topo The topology to be drawn and/or manipulated.
	 */
    public JViewer(Topology topo, boolean selfContained){
    	this(new JTopology(topo), selfContained);
    }
	/**
	 * Creates a windowed viewer encapsulating the specified jtopology. 
	 * @param jtopo The jtopology to be encapsulated.
	 */
    public JViewer(JTopology jtopo){
    	this(jtopo, true);
    }
	/**
	 * Creates a viewer encapsulating the specified jtopology. If 
	 * <tt>selfContained</tt> is <tt>true</tt>, a new window will be created 
	 * to contain the viewer (similarly to <tt>JViewer(Topology)</tt>). If it 
	 * is <tt>false</tt>, no window will be created and the viewer can be 
	 * subsequently integrated to another swing container (e.g. another 
	 * <tt>JFrame</tt> or a <tt>JApplet</tt>).
	 * @param jtopo The JTopology to be encapsulated.
	 */
    public JViewer(JTopology jtopo, boolean selfContained){
    	this.jtp=jtopo;
   		jtp.addActionCommand("communication bar");
   		jtp.addActionCommand("sensing bar");
   		jtp.addActionCommand("clock on/off");
   		jtp.addActionCommand("toString()");
   		jtp.addActionListener(handler);
    	if (selfContained){
	   		window=new JFrame();
	   		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   		window.add(jtp);
	   		Dimension dim = (Dimension)jtp.topo.getProperty("dimension");
	   		Dimension pos = (Dimension)jtp.topo.getProperty("position");
	   		if (dim==null) dim = new Dimension(800,600);
	   		if (pos==null) pos = new Dimension(0,0);
	   		window.setBounds(pos.width, pos.height, dim.width, dim.height);
	   		window.setVisible(true);
    	}
    }
    /**
     * Returns the jtopology attached to this viewer. Obtaining the reference 
     * can be useful for example to add or remove action commands or action 
     * listeners to the jtopology.
     * @return The jtopology reference.
     */
    public JTopology getJTopology(){
    	return jtp;
    }
    /**
     * Sets the size of the inner jtopology to the specified dimension.
     * @param width The desired width, in pixels.
     * @param height The desired height, in pixels.
     */
    public void setSize(int width, int height){
		jtp.setPreferredSize(new Dimension(width,height));
    	if (window!=null)
    		window.pack();
    }
    /**
     * Adds a communication range tuner (slider bar) to regulate the
     * communication range of the topology's nodes. The tuner is anchored at
     * the north of the jtopology.
     */
    public void addCommunicationRangeTuner(){
    	cRTuner=new JSlider(0,800,(int)Node.getModel("default").getCommunicationRange());
		cRTuner.addChangeListener(handler);
		jtp.getParent().add(cRTuner,BorderLayout.NORTH);
	}
    /**
     * Removes the communication range tuner (slider bar), if any.
     */
    public void removeCommunicationRangeTuner(){
 		jtp.getParent().remove(cRTuner);
 		cRTuner=null;
    }
    /**
     * Adds a sensing range tuner (slider bar) to regulate the sensing range of
     * the topology's nodes. The tuner is anchored at the south of the 
     * jtopology.
     */
    public void addSensingRangeTuner(){
    	sRTuner=new JSlider(0,800,(int)Node.getModel("default").getSensingRange());
		sRTuner.addChangeListener(handler);
		jtp.getParent().add(sRTuner,BorderLayout.SOUTH);
	}
    /**
     * Removes the sensing range tuner (slider bar), if any.
     */
    public void removeSensingRangeTuner(){
 		jtp.getParent().remove(sRTuner);
 		sRTuner=null;
    }
	class EventHandler implements ChangeListener, ActionListener{		
		public void stateChanged(ChangeEvent arg0) {
			JSlider src=((JSlider)arg0.getSource());
			if (src==cRTuner){
				for (Node n : jtp.topo.getNodes())
					n.setCommunicationRange(src.getValue());
				Node.getModel("default").setCommunicationRange(src.getValue());			
			}else{
				for (Node n : jtp.topo.getNodes())
					n.setSensingRange(src.getValue());
				Node.getModel("default").setSensingRange(src.getValue());			
			}
			jtp.updateUI();
		}
		public void actionPerformed(ActionEvent arg0) {
			String cmd=((JMenuItem)arg0.getSource()).getText();
			if (cmd.equals("communication bar")){
				if (cRTuner==null) 
					addCommunicationRangeTuner();
				else 
					removeCommunicationRangeTuner();
				jtp.updateUI();
			}else if (cmd.equals("sensing bar")){
				if (sRTuner==null)
					addSensingRangeTuner();
				else
					removeSensingRangeTuner();
				jtp.updateUI();
			}else if (cmd.equals("switch clock")){
				if (Clock.isRunning())
					Clock.pause();
				else
					Clock.resume();
			}else if (cmd.equals("toString()")){
				System.out.println(jtp.topo);
			}
		}
	}
}
