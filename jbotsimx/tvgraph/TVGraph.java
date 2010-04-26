package jbotsimx.tvgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import jbotsim.Node;

public class TVGraph{
	Vector<Node> nodes=new Vector<Node>();
	Vector<TVLink> tvlinks=new Vector<TVLink>();
	public TVGraph(){
	}
	public TVGraph(String filename){
		this(filename, Node.class);
	}
	@SuppressWarnings("unchecked")
	public TVGraph(String filename, Class nodeClass){
        try{
            BufferedReader in=new BufferedReader(new FileReader(filename));
            StringTokenizer st;
            String sin="";
            do sin=in.readLine(); while (sin.compareTo("vertices")!=0);
            st = new StringTokenizer(sin," ");
            while ((sin=in.readLine()).compareTo("")!=0){
                st = new StringTokenizer(sin," ");
                String id=st.nextToken();
                Node n=(Node)nodeClass.newInstance(); 
                n.setProperty("id", id);
                if (st.hasMoreTokens()){
                    int x=Integer.parseInt(st.nextToken());
                    int y=Integer.parseInt(st.nextToken());
            		n.setLocation(x, y);
                }
                nodes.add(n);
            }

            do sin=in.readLine(); while (sin.compareTo("edges")!=0);
            while ((sin=in.readLine())!=null && !sin.equals("")){
            	sin=sin.replaceAll("-", " ");
                st = new StringTokenizer(sin," ");
                Node n1=getNodeById(st.nextToken());
                Node n2=getNodeById(st.nextToken());
                
                TVLink tvl = new TVLink(n1, n2);
                
                while (st.hasMoreTokens()){
                	tvl.addAppearanceDate(new Integer(st.nextToken()));
                	tvl.addDisappearanceDate(new Integer(st.nextToken()));
                }
                tvlinks.add(tvl);
            }
        }catch(Exception e){e.printStackTrace();}		
	}
	public Node getNodeById(String id){
		for (Node n : nodes)
			if (n.getProperty("id").equals(id))
				return n;
		return null;
	}
	public void addNode(Node n){
		nodes.add(n);
	}
	public void addTVLink(TVLink l){
		tvlinks.add(l);
	}
	public Vector<Node> getNodes(){
		return new Vector<Node>(nodes);
	}
	public Vector<TVLink> getTVLinks(){
		return new Vector<TVLink>(tvlinks);
	}
	public int nbDates(){
		TreeSet<Integer> allDates=new TreeSet<Integer>();
		for (TVLink l : tvlinks){
			allDates.addAll(l.appearanceDates);
			allDates.addAll(l.disappearanceDates);
		}
		return allDates.size();
	}
	public String toString(){
		String s="";
		for (TVLink l : tvlinks)
			s += l.toString()+"\n";
		return s;
	}
}
