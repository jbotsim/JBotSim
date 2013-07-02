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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

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
	protected JSlider slideBar = new JSlider(0,800);
	protected enum BarType {COMMUNICATION, SENSING, SPEED};
	protected BarType slideBarType = null;
	protected JFrame window = null;
	protected EventHandler handler=new EventHandler();
	/**
	 * Creates a windowed viewer for a default topology. 
	 */
    public JViewer(){
    	this(new Topology(), true);
    }
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
    public JViewer(JTopology jtopo, boolean windowed){
    	jtp=jtopo;
   		jtp.addActionCommand("Set communication range");
   		jtp.addActionCommand("Set sensing range");
   		jtp.addActionCommand("Set clock speed");
   		jtp.addActionCommand("Pause / Resume");
   		jtp.addActionListener(handler);
    	if (windowed){ // This JViewer creates its own window
	   		window=new JFrame();
	   		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   		window.add(jtp);
    		window.pack();
	   		window.setVisible(true);
	        window.addComponentListener(new ComponentAdapter() {
	            public void componentResized(ComponentEvent e) {
	            	jtp.topo.setDimensions(jtp.getWidth(), jtp.getHeight());
	            }
	        });        
    	}
    	slideBar.addChangeListener(handler);
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
    	jtp.topo.setDimensions(width, height);
		jtp.setPreferredSize(jtp.topo.getDimensions());
    	if (window!=null)
    		window.pack();
    }
    /**
     * Adds a slide bar at the top of the topology.
     */
    public void addSlideBar(BarType type, int value){
    	removeSlideBar();
    	slideBarType = type;
    	slideBar.setValue(value);
		jtp.getParent().add(slideBar,BorderLayout.NORTH);
    }
    /**
     * Removes the slide bar, if any.
     */
    public void removeSlideBar(){
 		if (slideBarType!=null){
 			jtp.getParent().remove(slideBar);
 			slideBarType=null;
 		}
    }
	class EventHandler implements ChangeListener, ActionListener{		
		public void stateChanged(ChangeEvent arg0) {
			if (slideBarType==BarType.COMMUNICATION){
				for (Node n : jtp.topo.getNodes())
					n.setCommunicationRange(slideBar.getValue());
				Node.getModel("default").setCommunicationRange(slideBar.getValue());			
			}else if (slideBarType==BarType.SENSING){
				for (Node n : jtp.topo.getNodes())
					n.setSensingRange(slideBar.getValue());
				Node.getModel("default").setSensingRange(slideBar.getValue());			
			}else if (slideBarType==BarType.SPEED){
				Clock.setTimeUnit((800-slideBar.getValue())/40+1);
			}
			jtp.updateUI();
		}
		public void actionPerformed(ActionEvent arg0) {
			String cmd=((JMenuItem)arg0.getSource()).getText();
			if (cmd.equals("Set communication range")){
				if (slideBarType != BarType.COMMUNICATION) 
					addSlideBar(BarType.COMMUNICATION, 
							(int)Node.getModel("default").getCommunicationRange());
				else 
					removeSlideBar();
				jtp.updateUI();
			}else if (cmd.equals("Set sensing range")){
				if (slideBarType != BarType.SENSING) 
					addSlideBar(BarType.SENSING, 
							(int)Node.getModel("default").getSensingRange());
				else
					removeSlideBar();
				jtp.updateUI();
			}else if (cmd.equals("Set clock speed")){
				if (slideBarType != BarType.SPEED) 
					addSlideBar(BarType.SPEED, (800-Clock.getTimeUnit()*40));
				else
					removeSlideBar();
				jtp.updateUI();
			}else if (cmd.equals("Pause / Resume")){
				if (Clock.isRunning())
					Clock.pause();
				else
					Clock.resume();
			}
		}
	}
}
