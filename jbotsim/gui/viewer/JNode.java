/*******************************************************************************
 * This file is part of JBotSim.
 * 
 *     JBotSim is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     JBotSim is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 * 
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with JBotSim.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *     contributors:
 *     Arnaud Casteigts
 *******************************************************************************/
package jbotsim.gui.viewer;

import javax.swing.*;


import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

import jbotsim.core.*;
import jbotsim.core.event.*;


@SuppressWarnings("serial")
public class JNode extends JButton
    implements NodeListener, MouseListener, MouseMotionListener{
    protected Node node;
    protected Topology topo;
    protected Integer size;
    protected Image icon;

    public JNode(Topology topo, Node node){
        this.node=node;
        this.topo=topo;
        this.setToolTipText(node.toString());
        addMouseListener(this);
        addMouseMotionListener(this);
        setContentAreaFilled(false);
        setBorderPainted(false);
        size=(Integer)node.getProperty("size"); size=(size==null) ? 8 : size;
        String tmpicon=(String)node.getProperty("icon");
        if (tmpicon!="none"){
        	if (tmpicon==null)
        		icon=Toolkit.getDefaultToolkit().getImage(getClass().getResource("circle.png"));
        	else
        		icon=Toolkit.getDefaultToolkit().getImage(tmpicon).getScaledInstance(size*2, size*2, Image.SCALE_FAST);
        	setIcon(new ImageIcon(icon));
        }
        node.addNodeListener(this);
        sync();
    }
    public void sync(){
    	setBounds((int)node.getX()-size, (int)node.getY()-size, size*2, size*2);
    }
    public void paint(Graphics g){
        paintNode((Graphics2D)g,this);
    }
    // EVENTS
    // from MouseListener
    public void mouseClicked(MouseEvent e){
        if (e.getButton()==MouseEvent.BUTTON2)
        	topo.setProperty("selectedNode", this.node);
    }
    public void mouseEntered(MouseEvent e){
    }
    public void mouseExited(MouseEvent e){
    }
    public void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON3)
        	topo.removeNode(node);
    }
    public void mouseReleased(MouseEvent e) {
    }
    public void mouseMoved(MouseEvent e){
    }
    public void mouseDragged(MouseEvent e){
        node.translate((int)e.getX()-size,(int)e.getY()-size);
    }
    public void nodeMoved(Node node){
    	sync();
    }
    public void nodeChanged(Node node, String prop){
    	if (prop.equals("color"))
    		updateUI();
    	if (prop.equals("id"))
    		this.setToolTipText((String)node.getProperty("id"));
    }
	public void linkAdded(Link l) {}
	public void linkRemoved(Link l) {}
    public void paintNode(Graphics2D g2d, JNode jn){
    	if(Viewer.antialiasing)
    		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
    	Node n=jn.node;
    	int size=jn.size;
    	double direction=n.getDirection();
    	if(direction!=Math.PI/2){
    		AffineTransform newXform = g2d.getTransform();
    		newXform.rotate(direction-Math.PI/2, size, size);
    		g2d.setTransform(newXform);
    	}
        g2d.drawImage(jn.icon, 0, 0, null);
        String color=n.getColor();
        if(color!=null){
            g2d.setColor(getColor(color));
            g2d.fillOval(size/2, size/2, size, size);
        }
    }
    protected Color getColor(String color){
    	try{
    		return (Color)Color.class.getField(color).get(color);
    	}catch(Exception e){return Color.lightGray;}
    }
}
