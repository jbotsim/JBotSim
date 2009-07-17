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

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;

import jbotsim.core.Link;
import jbotsim.core.Node;
import jbotsim.core.Topology;
import jbotsim.core._Properties;
import jbotsim.core.event.NodeListener;
import jbotsim.core.event.PropertiesListener;
import jbotsim.core.event.TopologyListener;
import jbotsim.engine.event.ClockListener;


@SuppressWarnings("serial")
public class Viewer extends JPanel implements MouseListener, ActionListener, ChangeListener,
												TopologyListener, ClockListener,
												NodeListener, PropertiesListener{
    protected Topology topology;
    protected int refreshDelay=0;
    protected boolean topologyChanged=false;
    protected static boolean antialiasing=false;
    
    public Viewer(Topology topo){
    	this(topo, true);
    }
    public Viewer(Topology topo, boolean selfcontained){
        topo.addTopologyListener(this);
        super.setLayout(null);
        super.setBackground(new Color(180,180,180));
        super.addMouseListener(this);
        this.setTopology(topo);
        if (selfcontained){
    		JFrame win=new JFrame();
    		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    		win.add(this);
    		win.setSize(800, 600);
    		win.setVisible(true);
        }
        topo.setProperty("popupRunning",false);
    }
    public void setTopology(Topology topo){
    	if (this.topology!=null)
    		for (Node n : this.topology.getNodes())
    			((TopologyListener)this).nodeRemoved(n);
    	this.topology=topo;
    	if (topo!=null){
    		for (Node n : topo.getNodes())
    			((TopologyListener)this).nodeAdded(n);
    	}
    }
    public void setAntialiasing(boolean antialiasing){
    	Viewer.antialiasing=antialiasing;
    }
    public void setRefreshDelay(int millisec){
    	if (refreshDelay>0)
        	topology.getClock().removeClockListener(this);    		
    	if (millisec>0)
        	topology.getClock().addClockListener(this, millisec);
    	refreshDelay=millisec;
    }
    public void addCommunicationRangeTuner(){
    	JSlider js=(JSlider)topology.getProperty("communicationRangeTuner");
		if (js==null){
			js=new JSlider(0,800,(int)topology.getNodeModel("default").getCommunicationRange());
			js.setName("communicationRangeTuner");
			js.addChangeListener(this);
			this.getParent().add(js,BorderLayout.NORTH);
			topology.setProperty("communicationRangeTuner", js);
			this.updateUI();
		}
	}
    public void removeCommunicationRangeTuner(){
    	JSlider js=(JSlider)topology.getProperty("communicationRangeTuner");
		if (js!=null){
			this.getParent().remove(js);
			topology.setProperty("communicationRangeTuner", null);
		}
    }
    public void addSensingRangeTuner(){
    	JSlider js=(JSlider)topology.getProperty("sensingRangeTuner");
		if (js==null){
			js=new JSlider(0,400,(int)topology.getNodeModel("default").getSensingRange());
			js.setName("sensingRangeTuner");
			js.addChangeListener(this);
			this.getParent().add(js,BorderLayout.SOUTH);
			topology.setProperty("sensingRangeTuner", js);
			this.updateUI();
		}
	}
    public void removeSensingRangeTuner(){
    	JSlider js=(JSlider)topology.getProperty("sensingRangeTuner");
		if (js!=null){
			this.getParent().remove(js);
			topology.setProperty("sensingRangeTuner", null);
		}
    }
    public void onClock(){
    	if (topologyChanged){
    		updateUI();
    		topologyChanged=false;
    	}
    }
    protected void touch(){
		if (refreshDelay==0)
			updateUI();
		else
			topologyChanged=true;
	}
    public void paint(Graphics g){
        super.paint(g);
        if (this.topology!=null){
	        for(Link l : topology.getLinks())
	        	paintLink((Graphics2D)g, l);
	        for(Node n : topology.getNodes()){
	        	Integer r=new Double(n.getSensingRange()).intValue();
	        	if(r!=null){
	        		Graphics2D g2d=(Graphics2D)g;
	        		g2d.setColor(Color.gray);
	        		g2d.drawOval((int)n.getX()-r, (int)n.getY()-r, 2*r, 2*r);
	        	}
	        }
        }
    }
    public void paintLink(Graphics2D g2d, Link l){
    	if(Viewer.antialiasing)
    		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        Integer lineWidth;
        g2d.setColor(Color.darkGray);
        lineWidth=(Integer)l.getProperty("lineWidth");
        if (lineWidth==null)
        	lineWidth=1;
        g2d.setStroke(new BasicStroke(lineWidth));
        Vector<Node> endpoints=l.getEndpoints();
        Node n0=endpoints.get(0);
        Node n1=endpoints.get(1);
        g2d.drawLine((int)n0.getX(), (int)n0.getY(), (int)n1.getX(), (int)n1.getY());
    }
	// EVENT MANAGEMENT ////////////////////
    // FROM MouseListener
    public void mouseClicked(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
    public void mousePressed(MouseEvent e) {
    	if ((Boolean)topology.getProperty("popupRunning")==true){
    		topology.setProperty("popupRunning", false);
    		return;
    	}
    	if (e.getButton()==MouseEvent.BUTTON1){
        	JPopupMenu popup=new JPopupMenu();
        	for (String type : topology.getNodeModelNames()){
        		JMenuItem jitem=new JMenuItem(type);
        		jitem.setActionCommand(type+" "+e.getX()+" "+e.getY());
        		jitem.addActionListener(this);
        		popup.add(jitem);
        	}
        	if (popup.getComponentCount()>1){
            	topology.setProperty("popupRunning", true);
        		popup.show(this,e.getX(),e.getY());
        	}else{
        		topology.addNode(e.getX(),e.getY());
        	}
        }else if (e.getButton()==MouseEvent.BUTTON3){
        	JPopupMenu popup=new JPopupMenu();
        	String[] menus={"communication bar","sensing bar","algorithm engine"};
        	for (int i=0; i<menus.length; i++){
        		JMenuItem jitem=new JMenuItem(menus[i]);
        		jitem.addActionListener(this);
        		popup.add(jitem);
        	}
        	topology.setProperty("popupRunning", true);
    		popup.show(this,e.getX(),e.getY());
        }
    }
    public void mouseReleased(MouseEvent e) {}
    
    // FROM ActionListener
	public void actionPerformed(ActionEvent arg0) {
		topology.setProperty("popupRunning", false);
		String cmd=((JMenuItem)arg0.getSource()).getText();
		if (cmd.equals("communication bar")){
			JSlider js=(JSlider)topology.getProperty("communicationRangeTuner");
			if (js==null) 
				this.addCommunicationRangeTuner();
			else 
				this.removeCommunicationRangeTuner();
		}else if (cmd.equals("sensing bar")){
			JSlider js=(JSlider)topology.getProperty("sensingRangeTuner");
			if (js==null) 
				this.addSensingRangeTuner();
			else
				this.removeSensingRangeTuner();
		}else if (cmd.equals("algorithm engine")){
			topology.getAlgorithmEngine().setRunning(!topology.getAlgorithmEngine().isRunning());
		}else{
			String[] args=arg0.getActionCommand().split(" ");
			int x=Integer.parseInt(args[1]);
			int y=Integer.parseInt(args[2]);
			topology.addNode(x, y, args[0]);
		}
	}

	// FROM TopologyListener
    public void nodeAdded(Node n){
        JNode jv=(new JNode(topology,n));
        n.setProperty("jnode", jv);
        super.add(jv);
        n.addNodeListener(this);
        touch();
    }
    public void nodeRemoved(Node n){
    	JNode jn=(JNode)n.getProperty("jnode");
    	n.removeNodeListener(jn);
        n.removeNodeListener(this);
        super.remove(jn);
        touch();
    }
    public void linkAdded(Link l){
    	l.addPropertiesListener(this);
    	touch();
    }
    public void linkRemoved(Link l){
    	l.removePropertiesListener(this);
    	touch();
    }
    
    // FROM NodeListener
    public void nodeMoved(Node n) {
        touch();
	}
	public void nodeChanged(Node n, String property) {}
	
	// FROM ChangeListener
	public void stateChanged(ChangeEvent arg0) {
		JSlider src=((JSlider)arg0.getSource());
		if (src.getName().equals("communicationRangeTuner")){
			for (Node n : topology.getNodes())
				n.setCommunicationRange(src.getValue());
			topology.getNodeModel("default").setCommunicationRange(src.getValue());			
		}else{
			for (Node n : topology.getNodes())
				n.setSensingRange(src.getValue());
			topology.getNodeModel("default").setSensingRange(src.getValue());			
		}
		touch();
	}
	
	// FROM PropertiesListener
	public void propertyChanged(_Properties target, String property) {
		if (property=="lineWidth")
			touch();
	}
}
