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
package jbotsim.core;

import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.Vector;

import jbotsim.core.event.TopologyListener;
import jbotsim.engine.AlgorithmEngine;
import jbotsim.engine.MessageEngine;
import jbotsim.engine.event.Clock;

@SuppressWarnings("unchecked")
public class Topology extends _Properties{
    protected Clock clock=new Clock();
    protected Vector<TopologyListener> directedListeners=new Vector<TopologyListener>();
    protected Vector<TopologyListener> undirectedListeners=new Vector<TopologyListener>();
    protected Hashtable<String,Node> nodeTypes=new Hashtable<String,Node>();
    protected Vector<Node> nodes=new Vector<Node>();
    protected Vector<Link> arcs=new Vector<Link>();
    protected Vector<Link> edges=new Vector<Link>();
    protected AlgorithmEngine ae=new AlgorithmEngine(this,2);
    protected MessageEngine me=new MessageEngine(this,2);
    
    public Topology(){
        this((Topology)null);
    }
    public Topology(int refreshRate){
        this((Topology)null, refreshRate);
    }
    public Topology(String s){
        this((Topology)null, 0);
        this.fromString(s);
    }
    public Topology(String s, int refreshRate){
        this((Topology)null, refreshRate);
        this.fromString(s);
    }
    public Topology(Topology model){
    	this(model, 0);
    }
    public Topology(Topology model, int refreshRate){
        super(model);
        new TopologyMaintainer(this,refreshRate);
        if (model!=null){
            for (Node n : model.getNodes())
                addNode(n.getX(), n.getY(), n);
            for (Link l : model.arcs)
                if (!l.isWireless()) addLink(l);
            for (Link l : model.edges)
                if (!l.isWireless()) addLink(l);
            for (String s : model.nodeTypes.keySet())
                nodeTypes.put(s,new Node(model.nodeTypes.get(s)));
        }
    }
    /**
     * Removes all nodes and links in this topology, but keeps other settings
     * such as node models and inner properties.
     */
    public void clear(){
        Vector<Node> tmp=new Vector<Node>(nodes);
        for (Node n : tmp)
            removeNode(n);
    }
    /**
     * Returns the virtual node corresponding to the model whose name is given in argument,
     * all properties assigned to this virtual node will be given to further nodes created 
     * using this model name. If the requested model does not exist, it is created.
     */
    public Node getNodeModel(String modelName){
        if (!nodeTypes.containsKey(modelName))
            nodeTypes.put(modelName,new Node());
        return nodeTypes.get(modelName);
    }
    public void setNodeModel(String id, Node n){
        nodeTypes.put(id,n);
    }
	
    /**
     * Returns the list of all available model names.
     */
    public Vector<String> getNodeModelNames(){
        return new Vector<String>(nodeTypes.keySet());
    }
    public Clock getClock(){
        return clock;
    }
    public void addNode(double x, double y){
        this.addNode(x, y,"default");
    }
    public void addNode(double x, double y, String modelName){
        this.addNode(x, y, new Node(nodeTypes.get(modelName)));
    }
    public void addNode(double x, double y, Node n){
        n.setLocation(x, y);
        nodes.add(n);
        n.topology=this;
        notify("nodeAdded",n);
    }
    public void removeNode(Node n){
        for (NodeAlgorithm na : n.algorithms)
            na.exit(n);
        for (Link l : n.getLinks(true))
            removeLink(l);
        nodes.remove(n);
        notify("nodeRemoved",n);
    }
    public void addLink(Link l){
        if (l.directed==true){
            arcs.add(l);
            if (arcs.contains(new Link(l.to,l.from,Link.Type.DIRECTED))){
                Link edge=new Link(l.from,l.to,Link.Type.UNDIRECTED);
                edges.add(edge);
                notify("linkAdded", edge);
            }    		
        }else{
            Link arc1=new Link(l.from,l.to,Link.Type.DIRECTED);
            Link arc2=new Link(l.to,l.from,Link.Type.DIRECTED);
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
    public void removeLink(Link l){
        if (l.directed==true){
            arcs.remove(l);
            Link edge=getLink(l.from, l.to, false);
            if (edge!=null){
                edges.remove(edge);
                notify("linkRemoved",edge);
            }
        }else{
            Link arc1=getLink(l.from, l.to, true);
            Link arc2=getLink(l.to, l.from, true);
            arcs.remove(arc1);
            notify("linkRemoved", arc1);
            arcs.remove(arc2);
            notify("linkRemoved", arc2);
            edges.remove(l);
        }
        notify("linkRemoved",l);
    }
    public Vector<Node> getNodes(){
        return new Vector<Node>(nodes);
    }
    public Vector<Link> getLinks(){
        return getLinks(false);
    }
    public Vector<Link> getLinks(boolean directed){
        return (directed)?new Vector<Link>(arcs):new Vector<Link>(edges);
    }
    protected Vector<Link> getElements(boolean directed, Node n, int pos){
        Vector<Link> result=new Vector<Link>();
        Vector<Link> allLinks=(directed)?arcs:edges;
        for(Link l : allLinks){
            switch(pos){
                case 0:	if(l.from==n || l.to==n) 
                    result.add(l); break;
                case 1:	if(l.from==n)
                    result.add(l); break;
                case 2:	if(l.to==n)
                    result.add(l); break;
            }
        }
        return result;
    }
    public Link getLink(Node from, Node to){
        return getLink(from, to, false);
    }
    public Link getLink(Node from, Node to, boolean directed){
        if (directed){
            Link l=new Link(from,to,Link.Type.DIRECTED);
            return (arcs.contains(l))?arcs.elementAt(arcs.indexOf(l)):null;
        }else{
            Link l=new Link(from, to, Link.Type.UNDIRECTED);
            return (edges.contains(l))?edges.elementAt(edges.indexOf(l)):null;    		
        }
    }
    public AlgorithmEngine getAlgorithmEngine(){
        return ae;
    }
    public MessageEngine getMessageEngine(){
        return me;
    }
    public void addTopologyListener(TopologyListener listener){
        undirectedListeners.add(listener);
    }
    public void addTopologyListener(TopologyListener listener, boolean directed){
        if (directed)
            directedListeners.add(listener);
        else
            undirectedListeners.add(listener);
    }
    public void removeTopologyListener(TopologyListener listener){
        undirectedListeners.remove(listener);
    }
    public void removeTopologyListener(TopologyListener listener, boolean directed){
        if (directed)
            directedListeners.remove(listener);
        else
            undirectedListeners.remove(listener);
    }
    protected void notify(String method, Object param){
        if (param.getClass()==Node.class){
            LinkedHashSet<TopologyListener> union=new LinkedHashSet<TopologyListener>(directedListeners);
            union.addAll(undirectedListeners);
            Vector<TopologyListener> listeners=new Vector<TopologyListener>(union);
            try{
                java.lang.reflect.Method m=TopologyListener.class.getMethod(method, param.getClass());
                for (TopologyListener tl : listeners)
                    m.invoke(tl,param);
            }catch(Exception e){}
        }else if(param.getClass()==Link.class){
            Link l=(Link)param;
            LinkedHashSet<Object> union=new LinkedHashSet<Object>((l.directed)?directedListeners:undirectedListeners);
            union.addAll(l.directed?l.from.directedListeners:l.from.undirectedListeners);
            union.addAll(l.directed?l.to.directedListeners:l.to.undirectedListeners);
            try{
                for (Object o : new Vector<Object>(union)){
                    java.lang.reflect.Method m=o.getClass().getMethod(method, param.getClass());
                    m.invoke(o, param);
                }
            }catch(Exception e){}
        }
    }
    public boolean contains(Node n){
        return nodes.contains(n);
    }
    public boolean contains(Link e){
        if (e.directed)
            return arcs.contains(e);
        else
            return edges.contains(e);
    }
    public int size(){
        return nodes.size();
    }
	public String toString(){
		StringBuffer res = new StringBuffer();
		res.append("communicationRange " + getNodeModel("default").getCommunicationRange() + "\n");
		res.append("sensingRange " + getNodeModel("default").getSensingRange() + "\n");
		for (Node n : nodes)
			res.append(n.toString() + " " + n.coords.toString().substring(14) + "\n");
		// FIXME
//		for (Link l : getLinks()){
//			if (!l.isWireless());
//				res.append(l.toString()+ "\n");
//		}
		return res.toString();
	}
    private void fromString(String s){
    	getNodeModel("default").setCommunicationRange(new Double(s.substring(s.indexOf(" ")+1,s.indexOf("\n"))));
    	s=s.substring(s.indexOf("\n")+1);
    	
    	getNodeModel("default").setSensingRange(new Double(s.substring(s.indexOf(" ")+1,s.indexOf("\n"))));
		s=s.substring(s.indexOf("\n")+1);

		Hashtable<String,Node> nodeTable=new Hashtable<String,Node>();
    	while(s.indexOf("[")>0){	
    		Node n=new Node();
    		nodeTable.put(s.substring(0, s.indexOf(" ")), n); 
    		addNode(new Double(s.substring(s.indexOf("[")+1,s.indexOf(","))),new Double(s.substring(s.indexOf(",")+2,s.indexOf("]"))));
    		s=s.substring(s.indexOf("\n")+1);
    	}
    	while(s.indexOf("--")>0){
    		Node n1=nodeTable.get(s.substring(0,s.indexOf(" ")));
    		Node n2=nodeTable.get(s.substring(s.indexOf(">")+2,s.indexOf("\n")));
    		boolean directed=(s.indexOf("<")>0 && s.indexOf("<")<s.indexOf("\n"))?false:true;
    		addLink(new Link(n1,n2,directed?Link.Type.DIRECTED:Link.Type.UNDIRECTED,Link.Mode.WIRED));
    		s=s.substring(s.indexOf("\n")+1);
    	}
    }
}
