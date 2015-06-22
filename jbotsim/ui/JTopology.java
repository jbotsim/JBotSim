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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim._Properties;
import jbotsim.event.*;


@SuppressWarnings("serial")
public class JTopology extends JPanel implements ActionListener{
    protected ArrayList<SurfacePainter> surfacePainters=new ArrayList<SurfacePainter>();
    protected ArrayList<ActionListener> actionListeners=new ArrayList<ActionListener>();
    protected ArrayList<CommandListener> commandListeners=new ArrayList<CommandListener>();
    protected ArrayList<String> commands = new ArrayList<String>();
    protected Topology topo;
    protected JTopology jtopo=this;
    protected EventHandler handler=new EventHandler();
	protected boolean showDrawings=true;
    protected boolean isInteractive=true;
    /**
     * Creates a new JTopology with default topology.
     */
    public JTopology(){
    	this(new Topology());
    }
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
        super.setPreferredSize(topo.getDimensions());
        for (Node n : topo.getNodes())
        	handler.onNodeAdded(n);
        for (Link l : topo.getLinks())
        	handler.onLinkAdded(l);
        topo.setProperty("popupRunning", false);
    }
    public void setInteractive(boolean interactive){
        if (interactive && !isInteractive)
            addMouseListener(handler);
        if (!interactive && isInteractive)
            removeMouseListener(handler);
    }
    public void addSurfacePainter(SurfacePainter painter){
        surfacePainters.add(painter);
    }
    public void removeSurfacePainter(SurfacePainter painter){
        surfacePainters.remove(painter);
    }
    /**
     * Registers the specified action listener to this JTopology.
     * @param al The listener to add.
     */
    public void addCommandListener(CommandListener al){
    	commandListeners.add(al);
    }
    /**
     * Unregisters the specified action listener to this JTopology.
     * @param al The listener to remove.
     */
    public void removeCommandListener(CommandListener al){
    	commandListeners.remove(al);
    }
    /**
     * Adds the specified action command to this JTopology.
     * @param command The command name to add.
     */
    public void addCommand(String command){
    	commands.add(command);
    }
    /**
     * Removes the specified action command from this JTopology.
     * @param command The command name to remove.
     */
    public void removeCommand(String command){
        commands.remove(command);
    }
    /**
     * Removes the specified action command from this JTopology.
     * @param command The command name to remove.
     */
    public void removeAllCommands(String command){
        commands.clear();
    }
    /**
     * Disables the drawing of links and sensing radius (if any).
     */
    public void disableDrawings(){
        showDrawings=false;
    }
    /**
     * Paints this JTopology on the specified graphics (not supposed to be
     * used explicitly).
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        for (SurfacePainter painter : surfacePainters)
            painter.onPaint(g);
        if (showDrawings){
            Graphics2D g2d=(Graphics2D)g;
            for(Link l : topo.getLinks(topo.hasDirectedLinks()))
                paintLink(g2d, l);
            g2d.setStroke(new BasicStroke(1));
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
        Integer width=l.getWidth();
    	if (width==0)
    		return;
   		g2d.setColor(l.getColor());
    	g2d.setStroke(new BasicStroke(width));
        int srcX=(int)l.source.getX(), srcY=(int)l.source.getY();
        int destX=(int)l.destination.getX(), destY=(int)l.destination.getY();
        g2d.drawLine(srcX, srcY, (int)(srcX+(destX-srcX)), (int)(srcY+(destY-srcY)));
		if (topo.hasDirectedLinks()) {
			int x=srcX+4*(destX-srcX)/5-2;
			int y=srcY+4*(destY-srcY)/5-2;
			g2d.drawOval(x,y,4,4);
		}
    }
	public void actionPerformed(ActionEvent arg0) {
		String cmd = ((JMenuItem) arg0.getSource()).getText();
	}

    class EventHandler implements TopologyListener, MovementListener, ConnectivityListener,
			PropertyListener, MouseListener, ActionListener{
	    public void onNodeAdded(Node n){
            JNode jv=new JNode(n);
	        n.setProperty("jnode", jv);
	        n.addPropertyListener(this);
	        add(jv);
	        updateUI();
	    }
	    public void onNodeRemoved(Node n){
	    	JNode jn=(JNode)n.getProperty("jnode");
	        remove(jn);
	        updateUI();
	    }
	    public void onLinkAdded(Link l){
	    	l.addPropertyListener(this);
	    	updateUI();
	    }
	    public void onLinkRemoved(Link l){
	    	updateUI();
	    }
	    public void onMove(Node n) {
			Toolkit.getDefaultToolkit().sync();
			updateUI();
            if (n.hasProperty("jnode"))
    	    	((JNode)n.getProperty("jnode")).update();
	    }
		public void propertyChanged(_Properties o, String property){
			if (o instanceof Node){ // Node
	    		JNode jn = (JNode)((Node)o).getProperty("jnode");
	    		if (property.equals("color")){
	    			jn.updateUI();
	    		}else if (property.equals("state")){
		    		jn.setToolTipText(o.toString());
	    		}else if (property.equals("icon")){
                    jn.updateIcon();
                }else if (property.equals("size")){
                    jn.updateIcon();
                }
	    	}else if (property.equals("width") || property.equals("color")){
				updateUI();
	    	}
		}
	    public void mousePressed(MouseEvent e) {
	    	if ((Boolean)topo.getProperty("popupRunning")==true){
	    		topo.setProperty("popupRunning", false);
	    		return;
	    	}
	    	if (e.getButton()==MouseEvent.BUTTON1){
	        	JPopupMenu popup=new JPopupMenu();
	        	for (String type : topo.getModelsNames()){
	        		JMenuItem jitem=new JMenuItem(type);
	        		jitem.setActionCommand("addNode "+type+" "+e.getX()+" "+e.getY());
	        		jitem.addActionListener(this);
	        		popup.add(jitem);
	        	}
	        	if (popup.getComponentCount()>1){
	            	topo.setProperty("popupRunning", true);
	        		popup.show(jtopo,e.getX(),e.getY());
	        	}else{
                    String modelName = "default";
                    if (topo.getModelsNames().size()>0)
                        modelName = topo.getModelsNames().iterator().next();
	        		topo.addNode(e.getX(),e.getY(),topo.newInstanceOfModel(modelName));
	        	}
	        }else if (e.getButton()==MouseEvent.BUTTON3){
	        	JPopupMenu popup=new JPopupMenu();
	        	for (String command : commands){
	        		JMenuItem jitem=new JMenuItem(command);
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
				topo.addNode(x, y, topo.newInstanceOfModel(args[1]));
			}else{
                for (CommandListener cl : commandListeners)
                    cl.onCommand(arg0.getActionCommand());
            }
		}
	    public void mouseClicked(MouseEvent e){}
	    public void mouseEntered(MouseEvent e){}
	    public void mouseExited(MouseEvent e){}
	    public void mouseReleased(MouseEvent e){}
	}
}
