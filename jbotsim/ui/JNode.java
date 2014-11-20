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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import jbotsim.Node;

@SuppressWarnings("serial")
public class JNode extends JButton implements MouseListener, MouseMotionListener, MouseWheelListener{
	protected Toolkit tk = Toolkit.getDefaultToolkit();
	protected Image baseIcon = tk.getImage(JNode.class.getResource("circle.png")); 
	protected Image icon;
    protected Integer userSize;
    protected Integer drawSize;
    protected double zcoord = -1;
    protected Node node;
    public static double camheight=200;

    public JNode(Node node){
        this.node=node;
        this.setToolTipText(node.toString());
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setContentAreaFilled(false);
        setBorderPainted(false);
        userSize=node.hasProperty("size")?(Integer)node.getProperty("size"):8;
        drawSize=userSize;
        if (node.hasProperty("icon"))
        	baseIcon=tk.getImage(JNode.class.getResource((String)node.getProperty("icon")));
        update();
    }
    public void update(){
    	if (node.getZ() != zcoord){
    		zcoord = node.getZ();
    		drawSize = (int)(userSize * camheight/(camheight-zcoord));
            icon=baseIcon.getScaledInstance(drawSize*2, drawSize*2, Image.SCALE_DEFAULT);
            setIcon(new ImageIcon(icon));
    	}
    	setBounds((int)node.getX()-drawSize, (int)node.getY()-drawSize, drawSize*2, drawSize*2);
    }
    public void paint(Graphics g){
    	Graphics2D g2d = (Graphics2D) g;
    	double direction=this.node.getDirection();
    	if(direction!=Math.PI/2){
    		AffineTransform newXform = g2d.getTransform();
    		newXform.rotate(direction+Math.PI/2, drawSize, drawSize);
    		g2d.setTransform(newXform);
    	}
        g2d.drawImage(this.icon, 0, 0, null);
        String sc=node.getColor();
    	if (sc != "none") try{
    		g2d.setColor((Color)Color.class.getField(sc).get(sc));
    		g2d.fillOval(drawSize/2, drawSize/2, drawSize, drawSize);
    	}catch(Exception e){System.err.println("Color "+sc+" is not supported.");}
    }
    // EVENTS
    public void mousePressed(MouseEvent e) {
        if (e.getButton()==MouseEvent.BUTTON3)
        	node.getTopology().removeNode(node);
        else if (e.getButton()==MouseEvent.BUTTON2)
        	node.getTopology().selectNode(node);
    }
    public void mouseDragged(MouseEvent e){
        node.translate((int)e.getX()-drawSize,(int)e.getY()-drawSize);
    }
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}
	public void mouseWheelMoved(MouseWheelEvent e) {
		int notches = e.getWheelRotation();
		double z = node.getZ()-2*notches;
		if (z > .8*camheight)
			z = .8*camheight;
		if (z < 0)
			z = 0;
		System.out.println(z);
		node.setLocation(node.getX(), node.getY(), z);
	}
}
