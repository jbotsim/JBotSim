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

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import jbotsim.*;

@SuppressWarnings("serial")
class JNode extends JButton implements MouseListener, MouseMotionListener{
	final static Image icon=Toolkit.getDefaultToolkit().getImage(JNode.class.getResource("circle.png"));
    final static int size=8;
    protected Node node;

    public JNode(Node node){
        this.node=node;
        this.setToolTipText(node.toString());
        addMouseListener(this);
        addMouseMotionListener(this);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setIcon(new ImageIcon(icon));
        update();
    }
    public void update(){
    	setBounds((int)node.getX()-size, (int)node.getY()-size, size*2, size*2);
    }
    public void paint(Graphics g){
    	Graphics2D g2d=(Graphics2D)g;
        g2d.drawImage(icon, 0, 0, null);
        String sc=(String)node.getProperty("color");
    	try{
    		g2d.setColor((Color)Color.class.getField(sc).get(sc));
    		g2d.fillOval(size/2, size/2, size, size);
    	}catch(Exception e){}
    }
    // EVENTS
    public void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON3)
        	node.getTopology().removeNode(node);
        else
        	node.getTopology().setProperty("selectedNode", this.node);
    }
    public void mouseDragged(MouseEvent e){
        node.translate((int)e.getX()-size,(int)e.getY()-size);
    }
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}
}
