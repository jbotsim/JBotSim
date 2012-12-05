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

import javax.swing.JPanel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ConnectivityListener;
import jbotsim.event.MovementListener;
import jbotsim.event.PropertyListener;
import jbotsim.event.TopologyListener;


@SuppressWarnings("serial")
public class JTopology extends JPanel{
    protected Vector<ActionListener> actionListeners=new Vector<ActionListener>();
    protected Vector<String> actionCommands=new Vector<String>();
    protected Topology topo;
    protected JTopology jtopo=this;
    protected EventHandler handler=new EventHandler();
	protected boolean showDrawings=true;
    
    /**
     * Creates a new JTopology for the specified topology.
     * @param topo The topology to encapsulate.
     */
    public JTopology(Topology topo){
    	this.topo=topo;
    	topo.addConnectivityListener(handler);
    	topo.addTopologyListener(handler);
    	topo.addMovementListener(handler);
        super.setLayout(null);
        super.setBackground(new Color(180,180,180));
        super.addMouseListener(handler);
        for (Node n : topo.getNodes())
        	handler.nodeAdded(n);
        topo.setProperty("popupRunning",false);
    }
    /**
     * Registers the specified action listener to this JTopology.
     * @param al The listener to add.
     */
    public void addActionListener(ActionListener al){
    	actionListeners.add(al);
    }
    /**
     * Unregisters the specified action listener to this JTopology.
     * @param al The listener to remove.
     */
    public void removeActionListener(ActionListener al){
    	actionListeners.remove(al);
    }
    /**
     * Adds the specified action command to this JTopology.
     * @param command The command name to add.
     */
    public void addActionCommand(String command){
    	actionCommands.add(command);
    }
    /**
     * Removes the specified action command from this JTopology.
     * @param command The command name to remove.
     */
    public void removeActionCommand(String command){
    	actionCommands.remove(command);
    }
    /**
     * Disables the drawing of links and sensing radius (if any).
     */
    public void disableDrawings(){
    	this.showDrawings=false;
    }
    /**
     * Paints this JTopology on the specified graphics (not supposed to be 
     * used explicitly).
     */
    public void paint(Graphics g){
        super.paint(g);
        if (showDrawings){
            Graphics2D g2d=(Graphics2D)g;
            for(Link l : topo.getLinks())
            	paintLink(g2d, l);
            for(Node n : topo.getNodes()){
            	double sR=n.getSensingRange();
            	if(sR>0){
            		g2d.setColor(Color.gray);
            		g2d.drawOval((int)n.getX()-(int)sR, (int)n.getY()-(int)sR, 2*(int)sR, 2*(int)sR);
            	}
            }
        }
    }
    protected void paintLink(Graphics2D g2d, Link l){
        Integer lineWidth=((Integer)l.getProperty("lineWidth"));
        if (lineWidth==null)
        	lineWidth=1;
        g2d.setColor(Color.darkGray);
        g2d.setStroke(new BasicStroke(lineWidth));
        int srcX=(int)l.source.getX(), srcY=(int)l.source.getY();
        int destX=(int)l.destination.getX(), destY=(int)l.destination.getY();
        g2d.drawLine(srcX, srcY, (int)(srcX+(destX-srcX)), (int)(srcY+(destY-srcY)));
    }
	class EventHandler implements TopologyListener, MovementListener, ConnectivityListener,
			PropertyListener, MouseListener, ActionListener{
	    public void nodeAdded(Node n){
	    	JNode jv=new JNode(n);
	        n.setProperty("jnode", jv);
	        n.addPropertyListener(this);
	        add(jv);
	        updateUI();
	    }
	    public void nodeRemoved(Node n){
	    	JNode jn=(JNode)n.getProperty("jnode");
	        remove(jn);
	        updateUI();
	    }
	    public void linkAdded(Link l){
	    	l.addPropertyListener(this);
	    	updateUI();
	    }
	    public void linkRemoved(Link l){
	    	updateUI();
	    }	    
	    public void nodeMoved(Node n) {
	    	updateUI();
	    	((JNode)n.getProperty("jnode")).update();
	    }
		public void propertyChanged(Object o, String property){
	    	if (property.equals("color"))
	    		((JNode)((Node)o).getProperty("jnode")).updateUI();
	    	if (property.equals("id"))
	    		((JNode)((Node)o).getProperty("jnode")).setToolTipText(o.toString());
			if (property.equals("lineWidth"))
				updateUI();
		}		
	    public void mousePressed(MouseEvent e) {
	    	if ((Boolean)topo.getProperty("popupRunning")==true){
	    		topo.setProperty("popupRunning", false);
	    		return;
	    	}
	    	if (e.getButton()==MouseEvent.BUTTON1){
	        	JPopupMenu popup=new JPopupMenu();
	        	for (String type : Node.getModelsNames()){
	        		JMenuItem jitem=new JMenuItem(type);
	        		jitem.setActionCommand("addNode "+type+" "+e.getX()+" "+e.getY());
	        		jitem.addActionListener(this);
	        		popup.add(jitem);
	        	}
	        	if (popup.getComponentCount()>1){
	            	topo.setProperty("popupRunning", true);
	        		popup.show(jtopo,e.getX(),e.getY());
	        	}else{
	        		topo.addNode(e.getX(),e.getY(),Node.newInstanceOfModel("default"));
	        	}
	        }else if (e.getButton()==MouseEvent.BUTTON3){
	        	JPopupMenu popup=new JPopupMenu();
	        	for (String command : actionCommands){
	        		JMenuItem jitem=new JMenuItem(command);
	        		for (ActionListener al : actionListeners)
	        			jitem.addActionListener(al);
	        		jitem.addActionListener(this);
	        		popup.add(jitem);
	        	}
	        	topo.setProperty("popupRunning", true);
	    		popup.show(jtopo,e.getX(),e.getY());
	        }
	    }
	    
		public void actionPerformed(ActionEvent arg0) {
			topo.setProperty("popupRunning", false);
			String[] args=arg0.getActionCommand().split(" ");
			if (args[0].equals("addNode")){
				int x=Integer.parseInt(args[2]);
				int y=Integer.parseInt(args[3]);
				topo.addNode(x, y, Node.newInstanceOfModel(args[1]));
			}
		}
	    public void mouseClicked(MouseEvent e){}
	    public void mouseEntered(MouseEvent e){}
	    public void mouseExited(MouseEvent e){}
	    public void mouseReleased(MouseEvent e){}
	}
}
