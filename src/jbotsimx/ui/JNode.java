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
package jbotsimx.ui;

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
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.*;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.painting.NodePainter;

@SuppressWarnings("serial")
public class JNode extends JButton implements MouseListener, MouseMotionListener, MouseWheelListener {
    protected Image icon;
    protected Image scaledIcon;
    protected Integer drawSize;
    protected double zcoord = -1;
    protected Node node;
    public static double camheight = 200;
    protected static Node destination = null;
    protected static Integer currentButton = 1;


    public JNode(Node node) {
        this.node = node;
        this.setToolTipText(Integer.toString(node.getID()));
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        setContentAreaFilled(false);
        setBorderPainted(false);
        updateIcon();
        update();
    }

    public void updateIcon() {
        String path = (String) node.getProperty("icon");
        try {
            URL iconUrl = getClass().getResource(path);
            if (iconUrl != null)
                icon = Toolkit.getDefaultToolkit().getImage(iconUrl);
            else
                icon = ImageIO.read(Paths.get(path).toUri().toURL());
        } catch (Exception e) {
            if (node.hasProperty("icon")) {
                System.err.println("Unable to set icon: " + path);
                System.err.println(e.getMessage());
            }
            setDefaultIcon();
            return;
        }
        updateIconSize();
    }

    private void setDefaultIcon() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        icon = tk.getImage(getClass().getResource("/jbotsimx/ui/circle.png"));
        updateIconSize();
    }

    public void updateIconSize() {
        drawSize = (int) (node.getSize() * camheight / (camheight - node.getZ()));
        scaledIcon = icon.getScaledInstance(drawSize * 2, drawSize * 2, Image.SCALE_DEFAULT);
        setIcon(new ImageIcon(scaledIcon));
        setBounds((int) node.getX() - drawSize, (int) node.getY() - drawSize, drawSize * 2, drawSize * 2);
    }

    public void update() {
        if (node.getZ() != zcoord) {
            zcoord = node.getZ();
            updateIconSize();
        }
        setBounds((int) node.getX() - drawSize, (int) node.getY() - drawSize, drawSize * 2, drawSize * 2);
    }

    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        double direction = this.node.getDirection();
        if (direction != Math.PI / 2) {
            AffineTransform newXform = g2d.getTransform();
            newXform.rotate(direction + Math.PI / 2, drawSize, drawSize);
            g2d.setTransform(newXform);
        }
        //g2d.drawImage(scaledIcon, 0, 0, null);
        JTopology jTopology = (JTopology) this.getParent();
        for (NodePainter painter : jTopology.nodePainters)
            painter.paintNode(g2d, node);
    }

    // EVENTS
    public void mousePressed(MouseEvent e) {
        Topology tp = node.getTopology();
        if (((JTopology) getParent()).handler.ctrlPressed) {
            if (e.getButton() == 1)
                tp.selectNode(node);
        } else {
            currentButton = e.getButton();
            tp.setProperty("refreshMode", tp.getRefreshMode());
            tp.setRefreshMode(Topology.RefreshMode.EVENTBASED);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (currentButton == 1)
            node.translate(e.getX() - drawSize, (int) e.getY() - drawSize);
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        if (currentButton == 3) {
            destination = node;
        }
    }

    public void mouseExited(MouseEvent e) {
        destination = null;
    }

    public void mouseReleased(MouseEvent e) {
        Topology tp = node.getTopology();
        if (tp.hasProperty("refreshMode"))
            tp.setRefreshMode((Topology.RefreshMode) tp.getProperty("refreshMode"));
        if (destination != null) {
            node.getTopology().addLink(new Link(node, destination));
            destination = null;
        } else {
            if (e.getButton() == MouseEvent.BUTTON3)
                node.getTopology().removeNode(node);
            else if (e.getButton() == MouseEvent.BUTTON2)
                node.getTopology().selectNode(node);
        }
        currentButton = 1;
    }

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        int notches = e.getWheelRotation();
        double z = node.getZ() - 2 * notches;
        if (z > .8 * camheight)
            z = .8 * camheight;
        if (z < 0)
            z = 0;
        node.setLocation(node.getX(), node.getY(), z);
    }

    @Override
    public JToolTip createToolTip() {
        setToolTipText(node.toString());
        return super.createToolTip();
    }
}
