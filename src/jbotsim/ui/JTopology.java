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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim._Properties;
import jbotsim.event.*;
import jbotsim.ui.painting.*;


@SuppressWarnings("serial")
public class JTopology extends JPanel implements ActionListener{
    protected ArrayList<BackgroundPainter> backgroundPainters = new ArrayList<>();
    protected LinkPainter linkPainter;
    protected ArrayList<NodePainter> nodePainters = new ArrayList<>();
    protected ArrayList<CommandListener> commandListeners = new ArrayList<>();
    protected ArrayList<String> commands = new ArrayList<String>();
    protected Topology topo;
    protected JTopology jtopo=this;
    protected EventHandler handler=new EventHandler();
    protected boolean popupRunning = false;
    protected boolean showDrawings=true;
    protected boolean isInteractive=true;
    /**
     * Creates a new JTopology for the specified topology.
     * @param topo The topology to encapsulate.
     */
    public JTopology(Topology topo){
        setTopology(topo);
        super.setLayout(null);
        super.setBackground(new Color(180,180,180));
        super.addMouseListener(handler);
        super.setPreferredSize(topo.getDimensions());
        ToolTipManager.sharedInstance().setInitialDelay(0);
        linkPainter = new LinkPainter();
        nodePainters.add(new DefaultNodePainter());
        backgroundPainters.add(new DefaultBackgroundPainter());
    }

    public Topology getTopology() {
        return topo;
    }

    public void setTopology(Topology topology) {
        unsetTopology();
        this.topo = topology;
        topo.setProperty("jtopology", this);
        topo.setClockModel(JClock.class);
        topo.addConnectivityListener(handler);
        topo.addTopologyListener(handler);
        topo.addMovementListener(handler);
        topo.addClockListener(handler);
        for (Node n : topo.getNodes())
            handler.onNodeAdded(n);
        for (Link l : topo.getLinks())
            handler.onLinkAdded(l);
    }

    public void unsetTopology(){
        if (topo != null) {
            topo.removeProperty("jtopology");
            topo.removeConnectivityListener(handler);
            topo.removeTopologyListener(handler);
            topo.removeMovementListener(handler);
            topo.removeClockListener(handler);
            for (Link l : topo.getLinks())
                handler.onLinkRemoved(l);
            for (Node n : topo.getNodes())
                handler.onNodeRemoved(n);
        }
    }

    public void setInteractive(boolean interactive){
        if (interactive && !isInteractive)
            addMouseListener(handler);
        if (!interactive && isInteractive)
            removeMouseListener(handler);
        isInteractive = interactive;
    }
    public void addBackgroundPainter(BackgroundPainter painter){
        backgroundPainters.add(0, painter);
    }
    public void setDefaultBackgroundPainter(BackgroundPainter painter){
        backgroundPainters.clear();
        addBackgroundPainter(painter);
    }
    public void removeBackgroundPainter(BackgroundPainter painter){
        backgroundPainters.remove(painter);
    }
    public void setLinkPainter(LinkPainter painter){
        linkPainter = painter;
    }
    public void addNodePainter(NodePainter painter){
        nodePainters.add(painter);
    }
    public void setDefaultNodePainter(NodePainter painter){
        nodePainters.clear();
        addNodePainter(painter);
    }
    public void removeNodePainter(NodePainter painter){
        nodePainters.remove(painter);
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
     * Removes all commands from this JTopology.
     */
    public void removeAllCommands(){
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
        Graphics2D g2d=(Graphics2D)g;
        for (BackgroundPainter painter : backgroundPainters)
            painter.paintBackground(g2d, topo);
        if (showDrawings){
            for(Link l : topo.getLinks(topo.hasDirectedLinks()))
                linkPainter.paintLink(g2d, l);
        }
        //if ( ! topo.getNodes().isEmpty() && ! backgroundPainters.isEmpty() ) // FIXME
        //    updateUI();
    }
    public void actionPerformed(ActionEvent arg0) {
        String cmd = ((JMenuItem) arg0.getSource()).getText();
    }

    class EventHandler implements TopologyListener, MovementListener, ConnectivityListener,
            PropertyListener, ClockListener, MouseListener, ActionListener{
        public void onNodeAdded(Node n){
            JNode jv=new JNode(n);
            n.setProperty("jnode", jv);
            n.addPropertyListener(this);
            add(jv);
            updateUI();
        }
        public void onNodeRemoved(Node n){
            JNode jn=(JNode)n.getProperty("jnode");
            if (jn != null)
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
                }else if (property.equals("icon")){
                    jn.updateIcon();
                }else if (property.equals("size")){
                    jn.updateIcon();
                }
            }else if (property.equals("width") || property.equals("color")){
                updateUI();
            }
        }
        @Override
        public void onClock() {
        }
        public void mousePressed(MouseEvent e) {
            if (popupRunning){
                popupRunning = false;
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
                    popupRunning = true;
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
                popupRunning = true;
                popup.show(jtopo,e.getX(),e.getY());
            }
        }

        public void actionPerformed(ActionEvent arg0) {
            popupRunning = false;
            String[] args=arg0.getActionCommand().split(" ");
            if (args[0].equals("addNode")){
                int x=Integer.parseInt(args[2]);
                int y=Integer.parseInt(args[3]);
                topo.addNode(x, y, topo.newInstanceOfModel(args[1]));
            }else{
                for (CommandListener cl : commandListeners)
                    cl.onCommand(arg0.getActionCommand());
                updateUI();
            }
        }
        public void mouseClicked(MouseEvent e){}
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        public void mouseReleased(MouseEvent e){}
    }
}
