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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import jbotsim.Node;

@SuppressWarnings("serial")
public class JNode extends JButton implements MouseListener, MouseMotionListener{
	protected Image icon=Toolkit.getDefaultToolkit().getImage(JNode.class.getResource("circle.png"));
    protected Integer size;
    protected Node node;

    public JNode(Node node){
        this.node=node;
        this.setToolTipText(node.toString());
        addMouseListener(this);
        addMouseMotionListener(this);
        setContentAreaFilled(false);
        setBorderPainted(false);
        size=(Integer)node.getProperty("size"); if (size==null) size = 8;
        String desiredIconPath=(String)node.getProperty("icon");
        if (desiredIconPath!=null)
        	icon=Toolkit.getDefaultToolkit().getImage(desiredIconPath);
        icon=icon.getScaledInstance(size*2, size*2, Image.SCALE_FAST);
        setIcon(new ImageIcon(icon));      
        
        update();
    }
    public void update(){
    	setBounds((int)node.getX()-size, (int)node.getY()-size, size*2, size*2);
    }
    public void paint(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
    	double direction=this.node.getDirection();
    	if(direction!=Math.PI/2){
    		AffineTransform newXform = g2d.getTransform();
    		newXform.rotate(direction+Math.PI/2, size, size);
    		g2d.setTransform(newXform);
    	}
        g2d.drawImage(this.icon, 0, 0, null);
        String sc=(String)node.getProperty("color");
    	if (sc != null && sc != "none") try{
    		g2d.setColor((Color)Color.class.getField(sc).get(sc));
    		g2d.fillOval(size/2+1, size/2+1, size-2, size-2);
    	}catch(Exception e){System.err.println("Color "+sc+" is not supported.");}
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
