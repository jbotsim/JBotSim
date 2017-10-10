/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim.ui;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jbotsim.Topology;
import jbotsimx.format.tikz.Tikz;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The viewer includes a central jtopology which will draw the attached
 * topology and offer interaction, as well as contextual commands to add or
 * remove a communication range or sensing range tuners (slider bars), or to
 * pause/resume the system clock.getTopology()
 */
public class JViewer implements CommandListener, ChangeListener{
    protected JTopology jtp;
    protected int width=600;
    protected JSlider slideBar = new JSlider(0, width);

    protected enum BarType {COMMUNICATION, SENSING, SPEED};
    protected BarType slideBarType = null;
    protected JFrame window = null;

    protected int nbPauses = 0;
    protected boolean isStarted = false;

    protected Timer timer = new Timer(10, new ActionHandler());


    private class ActionHandler implements ActionListener{
        public void actionPerformed(ActionEvent evt) {
            jtp.topo.step();
        }
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
     * @param selfContained Set this to false to avoid creating a JFrame
     *                      (e.g. for embedding the JViewer in your own frame).
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
     * @param windowed Set this to false to avoid creating a JFrame
     *                 (e.g. for embedding the JViewer in your own frame).
     */
    public JViewer(JTopology jtopo, boolean windowed){
        jtp=jtopo;
        jtp.addCommand("Set communication range");
        jtp.addCommand("Set sensing range");
        jtp.addCommand("Set clock speed");
        jtp.addCommand("Pause or resume execution");
        jtp.addCommand("Execute a single step");
        jtp.addCommand("Restart nodes");
        jtp.addCommand("Export topology");
           jtp.addCommandListener(this);
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
        slideBar.addChangeListener(this);
        start();
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
     * Sets the title of the corresponding window
     */
    public void setTitle(String title){
        if (window != null)
            window.setTitle(title);
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

    @Override
    public void onCommand(String command) {
        if (command.equals("Set communication range")){
            if (slideBarType != BarType.COMMUNICATION)
                addSlideBar(BarType.COMMUNICATION,
                        (int)jtp.topo.getCommunicationRange());
            else
                removeSlideBar();
            jtp.updateUI();
        }else if (command.equals("Set sensing range")){
            if (slideBarType != BarType.SENSING)
                addSlideBar(BarType.SENSING,
                        (int)jtp.topo.getSensingRange());
            else
                removeSlideBar();
            jtp.updateUI();
        }else if (command.equals("Set clock speed")){
            if (slideBarType != BarType.SPEED)
                addSlideBar(BarType.SPEED, (width-getTimeUnit()*40));
            else
                removeSlideBar();
            jtp.updateUI();
        }else if (command.equals("Pause or resume execution")){
            if (isRunning())
                pause();
            else
                resume();
        }else if (command.equals("Restart nodes")){
            jtp.topo.restart();
        }else if (command.equals("Execute a single step")){
            jtp.topo.step();
        }else if (command.equals("Export topology")){
            System.out.println(Tikz.exportTopology(jtp.topo));
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        if (slideBarType==BarType.COMMUNICATION){
            jtp.topo.setCommunicationRange(slideBar.getValue());
        }else if (slideBarType==BarType.SENSING){
            jtp.topo.setSensingRange(slideBar.getValue());
        }else if (slideBarType==BarType.SPEED){
            setTimeUnit((width-slideBar.getValue())/40+1);
        }
        jtp.updateUI();
    }


    /**
     * Reset the color and width of nodes and links, then calls the
     * onStart() method on each node.
     */
    public void start(){
        if (! isStarted) {
            isStarted = true;
            restart();
        }
    }
    /**
     * Causes the onStart() method to be called again on each node (and each StartListener)
     */
    public void restart(){
        pause();
        jtp.topo.restart();
        resume();
    }

    public boolean isStarted() {
        return isStarted;
    }

    /**
     * Pauses the clock (or increments the pause counter).
     */
    public void pause(){
        if (isStarted) {
            if (nbPauses == 0)
                timer.stop();
            nbPauses++;
        }
    }

    /**
     * Resumes the clock (or decrements the pause counter).
     */
    public void resume(){
        if (isStarted) {
            assert (nbPauses > 0);
            nbPauses--;
            if (nbPauses == 0)
                timer.start();
        }
    }
    /**
     * Returns the time unit of the clock, in milliseconds.
     */
    public int getTimeUnit(){
        return timer.getDelay();
    }

    /**
     * Sets the time unit of the clock to the specified value in millisecond.
     * @param delay The desired time unit (1 corresponds to the fastest rate)
     */
    public void setTimeUnit(int delay){
        timer.setDelay(delay);
    }

    /**
     * Indicates whether the clock is currently running or paused.
     * @return <tt>true</tt> if running, <tt>false</tt> if paused.
     */
    public boolean isRunning(){
        return timer.isRunning();
    }
}
