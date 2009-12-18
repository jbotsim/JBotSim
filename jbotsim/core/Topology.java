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
package jbotsim;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Vector;

import jbotsim.Link.Mode;
import jbotsim.Link.Type;
import jbotsim.event.TopologyListener;

public class Topology{
    Vector<TopologyListener> undirectedListeners=new Vector<TopologyListener>();
    Vector<TopologyListener> directedListeners=new Vector<TopologyListener>();
    Message.MessageEngine messageEngine=new Message.MessageEngine(this);
    HashMap<String,Object> properties=new HashMap<String,Object>();
    Vector<Node> nodes=new Vector<Node>();
    Vector<Link> arcs=new Vector<Link>();
    Vector<Link> edges=new Vector<Link>();
    
    /**
     * Removes all the nodes (and links) of this topology.
     */
    public void clear(){
        for (Node n : new Vector<Node>(nodes))
            removeNode(n);
    }
    /**
     * Adds the specified node to this topology. The location of the node
     * in the topology will be its current inherent location (or <tt>(0,0)</tt>
     * if no location was prealably given to it).
     * @param n The node to be added.
     */
    public void addNode(Node n){
        this.addNode(n.getX(), n.getY(), n);
    }
    /**
     * Adds a new node to this topology at the specified location. The node
     * will be created using default settings FIXME
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     */
    public void addNode(double x, double y){
        this.addNode(x, y, Node.newInstanceOfModel("default"));
    }
    /**
     * Adds the specified node to this topology at the specified location.
     * @param x The abscissa of the location.
     * @param y The ordinate of the location.
     * @param n The node to be added.
     */
    public void addNode(double x, double y, Node n){
        n.setLocation(x, y);
        this.nodes.add(n);
        n.topo=this;
        this.updateWirelessLinksFor(n);
        this.notify("nodeAdded",n);
    }
    /**
     * Removes the specified node from this topology. All adjacent links will
     * be automatically removed.
     * @param n The node to be removed.
     */
    public void removeNode(Node n){
    	for (Link l : n.getLinks(true))
            this.removeLink(l);
        this.nodes.remove(n);
        n.topo=null;
        this.notify("nodeRemoved",n);
    }
    /**
     * Adds the specified link to this topology. Calling this method makes
     * sense only for wired links, since wireless links are automatically
     * managed as per the nodes' communication ranges.
     * @param l The link to be added.
     */
    public void addLink(Link l){
        if (l.type==Type.DIRECTED){
            arcs.add(l);
            if (arcs.contains(new Link(l.destination,l.source,Link.Type.DIRECTED))){
                Link edge=new Link(l.source,l.destination,Link.Type.UNDIRECTED,l.mode);
                edges.add(edge);
                notify("linkAdded", edge);
            }    		
        }else{
            Link arc1=new Link(l.source,l.destination,Link.Type.DIRECTED);
            Link arc2=new Link(l.destination,l.source,Link.Type.DIRECTED);
            if (!arcs.contains(arc1)){
                arcs.add(arc1);
                notify("linkAdded", arc1);
            }
            if (!arcs.contains(arc2)){
                arcs.add(arc2);
                notify("linkAdded", arc2);
            }
            edges.add(l);
        }
        notify("linkAdded",l);
    }
    /**
     * Removes the specified link from this topology. Calling this method makes
     * sense only for wired links, since wireless links are automatically
     * managed as per the nodes' communication ranges.
     * @param l The link to be removed.
     */
    public void removeLink(Link l){
        if (l.type==Type.DIRECTED){
            arcs.remove(l);
            Link edge=getLink(l.source, l.destination, false);
            if (edge!=null){
                edges.remove(edge);
                notify("linkRemoved",edge);
            }
        }else{
            Link arc1=getLink(l.source, l.destination, true);
            Link arc2=getLink(l.destination, l.source, true);
            arcs.remove(arc1);
            notify("linkRemoved", arc1);
            arcs.remove(arc2);
            notify("linkRemoved", arc2);
            edges.remove(l);
        }
        notify("linkRemoved",l);
    }
    /**
     * Returns a vector containing all the nodes in this topology. The returned
     * vector can be subsequently modified without effect on the topology.
     */
    public Vector<Node> getNodes(){
        return new Vector<Node>(nodes);
    }
    /** 
     * Returns a vector containing all undirected links in this topology. The 
     * returned vector can be subsequently modified without effect on the
     * topology.
     */
    public Vector<Link> getLinks(){
        return getLinks(false);
    }
    /** 
     * Returns a vector containing all links of the specified type in this
     * topology. The returned vector can be subsequently modified without
     * effect on the topology.
     * @param directed <tt>true</tt> for directed links, <tt>false</tt> for
     * undirected links.
     */
    public Vector<Link> getLinks(boolean directed){
        return (directed)?new Vector<Link>(arcs):new Vector<Link>(edges);
    }
    Vector<Link> getLinks(boolean directed, Node n, int pos){
        Vector<Link> result=new Vector<Link>();
        Vector<Link> allLinks=(directed)?arcs:edges;
        for(Link l : allLinks)
            switch(pos){
                case 0:	if(l.source==n || l.destination==n) 
                    result.add(l); break;
                case 1:	if(l.source==n)
                    result.add(l); break;
                case 2:	if(l.destination==n)
                    result.add(l); break;
            }
        return result;
    }
    /**
     * Returns the undirected link shared the specified nodes, if any.
     * @return The requested link, if such a link exists, <tt>null</tt> 
     * otherwise.
     */
    public Link getLink(Node n1, Node n2){
        return getLink(n1, n2, false);
    }
    /**
     * Returns the link of the specified type between the specified nodes, if
     * any.
     * @return The requested link, if such a link exists, <tt>null</tt> 
     * otherwise.
     */
    public Link getLink(Node from, Node to, boolean directed){
        if (directed){
            Link l=new Link(from, to,Link.Type.DIRECTED);
            return (arcs.contains(l))?arcs.elementAt(arcs.indexOf(l)):null;
        }else{
            Link l=new Link(from, to, Link.Type.UNDIRECTED);
            return (edges.contains(l))?edges.elementAt(edges.indexOf(l)):null;    		
        }
    }
    /**
     * Returns the property stored under the specified name.
     * @param key The property name.
     */
    public Object getProperty(String key){
    	return properties.get(key);
    }
    /**
     * Stores the specified property (<tt>value</tt>) under the specified name
     * (<tt>key</tt>). 
     * @param key The property name.
     * @param value The property value.
     */
    public void setProperty(String key, Object value){
    	properties.put(key, value);
    }    
    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever the a node or an undirected link is added or 
     * removed, as well as when a property is changed.
     * @param listener The listener to add.
     */
    public void addTopologyListener(TopologyListener listener){
        undirectedListeners.add(listener);
    }
    /**
     * Registers the specified topology listener to this topology. The listener
     * will be notified whenever the a node or a link of the specified type is
     * added or removed, as well as when a property is changed.
     * @param listener The listener to add.
     * @param directed The type of links to be listened (<tt>true</tt> for 
     * directed, <tt>false</tt> for undirected).
     */
    public void addTopologyListener(TopologyListener listener, boolean directed){
        if (directed) 
        	directedListeners.add(listener); 
        else 
        	undirectedListeners.add(listener);
    }
    /**
     * Unregisters the specified topology listener from the 'undirected' 
     * listeners of this topology.
     * @param listener The listener to remove.
     */
    public void removeTopologyListener(TopologyListener listener){
    	undirectedListeners.remove(listener);
    }
    /**
     * Unregisters the specified topology listener from the listeners of this 
     * topology of the specified type.
     * @param listener The listener to remove.
     * @param directed The type of links that this listener was listening 
     * (<tt>true</tt> for directed, <tt>false</tt> for undirected).
     */
    public void removeTopologyListener(TopologyListener listener, boolean directed){
        if (directed) 
        	directedListeners.remove(listener); 
        else 
        	undirectedListeners.remove(listener);
    }
    protected void notify(String method, Object param){
    	if(param.getClass()==Link.class){
            Link l=(Link)param;
            boolean directed=(l.type==Type.DIRECTED)?true:false;
            LinkedHashSet<Object> union=new LinkedHashSet<Object>(directed?directedListeners:undirectedListeners);
            union.addAll(directed?l.source.directedListeners:l.source.undirectedListeners);
            union.addAll(directed?l.destination.directedListeners:l.destination.undirectedListeners);
            try{
            	for (Object o : new Vector<Object>(union)){
            		java.lang.reflect.Method m;
            		if ((Arrays.asList(o.getClass().getInterfaces()).contains(TopologyListener.class)))
            			m=TopologyListener.class.getMethod(method, Link.class);
            		else
            			m=jbotsim.event.NodeListener.class.getMethod(method, Link.class);
                    m.invoke(o, param);
            	}
            }catch(Exception e){e.printStackTrace();
            }
        }else{
            LinkedHashSet<TopologyListener> union=new LinkedHashSet<TopologyListener>(directedListeners);
            union.addAll(undirectedListeners);
            Vector<TopologyListener> listeners=new Vector<TopologyListener>(union);
            try{
                java.lang.reflect.Method m=TopologyListener.class.getMethod(method, Node.class);
                for (TopologyListener tl : listeners){
                    m.invoke(tl,param);
                }
            }catch(Exception e){e.printStackTrace();}
        }
    }
    void updateWirelessLinksFor(Node n){
        for (Node n2 : nodes)
        	if (n2!=n){
        		this.updateWirelessLink(n, n2);
        		this.updateWirelessLink(n2, n);
        	}
    }    
    void updateWirelessLink(Node n1, Node n2){
    	Link l=n1.getOutLinkTo(n2);
    	if (l==null){
    		if(n1.distance(n2)<n1.communicationRange)
    			this.addLink(new Link(n1,n2,Type.DIRECTED,Mode.WIRELESS));
    	}else
    		if (l.isWireless() && n1.distance(n2)>n1.communicationRange)
    			this.removeLink(l);
    }
    /**
     * Returns a string representation of this topology. The output of this
     * method can be subsequently used to reconstruct a topology with the 
     * <tt>fromString</tt> method. Only the nodes and wired links are exported
     * here (not the topology's properties).
     */
    public String toString(){
		StringBuffer res = new StringBuffer();
		for (Node n : nodes)
			res.append(n.toString() + " " + n.coords.toString().substring(14) + "\n");
		for (Link l : getLinks())
			if (!l.isWireless())
				res.append(l.toString()+ "\n");
		return res.toString();
	}
    /**
     * Imports nodes and wired links from the specified string representation of a 
     * topology.
     * @param s The string representation.
     */
    public void fromString(String s){
		HashMap<String,Node> nodeTable=new HashMap<String,Node>();
    	while(s.indexOf("[")>0){	
    		Node n=new Node();
    		String id=s.substring(0, s.indexOf(" "));
    		n.setProperty("id", id);
    		nodeTable.put(id, n);
    		addNode(new Double(s.substring(s.indexOf("[")+1,s.indexOf(","))),new Double(s.substring(s.indexOf(",")+2,s.indexOf("]"))),n);
    		s=s.substring(s.indexOf("\n")+1);
    	}
    	while(s.indexOf("--")>0){
    		Node n1=nodeTable.get(s.substring(0,s.indexOf(" ")));
    		Node n2=nodeTable.get(s.substring(s.indexOf(">")+2,s.indexOf("\n")));
    		Type type=(s.indexOf("<")>0 && s.indexOf("<")<s.indexOf("\n"))?Type.UNDIRECTED:Type.DIRECTED;
    		addLink(new Link(n1,n2,type,Link.Mode.WIRED));
    		s=s.substring(s.indexOf("\n")+1);
    	}
    }
}
