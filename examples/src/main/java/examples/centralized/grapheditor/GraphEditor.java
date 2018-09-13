package examples.centralized.grapheditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jbotsim.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.ui.JTopology;

public class GraphEditor implements ActionListener{
	protected Topology topo;
	protected JTopology viewer;
	protected JTextArea textArea;
	protected JPanel mainPanel;
	protected int scale=50;

	public GraphEditor(){
		this(new Topology());
	}
	public GraphEditor(Topology t){
		topo=t;
		viewer=new JTopology(topo);
		textArea=new JTextArea();
		textArea.setRows(1000);
		textArea.setPreferredSize(new Dimension(450,600));
		JButton cmdImport=new JButton("import !");
		cmdImport.setActionCommand("import");
		cmdImport.addActionListener(this);
		JButton cmdExport=new JButton("export !");
		cmdExport.setActionCommand("export");
		cmdExport.addActionListener(this);

		JFrame win=new JFrame();
		win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		win.setLayout(new BorderLayout());
		win.add(new JScrollPane(textArea), BorderLayout.EAST);
		win.add(viewer, BorderLayout.CENTER);
		mainPanel=new JPanel(new BorderLayout());
		mainPanel.add(cmdImport, BorderLayout.EAST);
		mainPanel.add(cmdExport, BorderLayout.WEST);
		mainPanel.add(new jbotsimx.JConsole(topo), BorderLayout.NORTH);
		win.add(mainPanel, BorderLayout.SOUTH);
		win.setSize(800, 600);
		win.setVisible(true);
		
//		double rayon=100;
//		double nb=11;
//		for (double i=0; i<nb; i++){
//			double angle=i*((2.0*Math.PI)/nb);
//			System.out.println(angle);
//			double dx=Math.cos(angle)*rayon;
//			double dy=Math.sin(angle)*rayon;
//			topo.addNode(120-dx, 120-dy);
//		}
		//new RandomWayPoint(topo);
	}
	public static void main(String[] args) {
		Topology tp=new Topology();
		tp.setCommunicationRange(60);
		new GraphEditor(tp);
	}
	public void actionPerformed(ActionEvent action) {
		if (action.getActionCommand().equals("export"))
			doExport();
		else if (action.getActionCommand().equals("import"))
			doImport();
	}
	public void doImport(){
		topo.clear();
		String[] input=textArea.getText().split("\\n");
		List<Point> points=new ArrayList<Point>();
		double ymax=0;
		for (int i=0; i<input.length;i++){
			if (input[i].indexOf("node")>0 && input[i].indexOf("{}")>0){
				String sx=input[i].substring(input[i].indexOf("(")+1, input[i].indexOf(","));
				String sy=input[i].substring(input[i].indexOf(",")+1, input[i].indexOf(")"));
				double xtmp=(new Double(sx).doubleValue()*scale);
				double ytmp=(new Double(sy).doubleValue()*scale);
				points.add(new Point(xtmp,ytmp));
				ymax=(ytmp>ymax)?ytmp:ymax;
			}
		}
		for (Point p : points)
			topo.addNode((int)p.getX(),(int)(ymax-p.getY())+50);
	}
	public void doExport(){
		updateIDs();
		String delim="\n";

		String s="\\begin{tikzpicture}[xscale=1,yscale=1]"+delim;
		Integer sr=(int)topo.getSensingRange();
		if (sr!=null){
			s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep="+sr/5.0+", fill opacity=0.5,gray,fill=gray!40]"+delim;
			for (Node n : topo.getNodes()){
				double x=Math.round(n.getX()*100/scale)/100.0;
				double y=Math.round((viewer.getHeight()-n.getY())*100/scale)/100.0;
				s=s+"  \\path ("+x+","+y+") node ("+n+") {};"+delim;
			}
		}
		s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep=2]"+delim;
		List<String> labels=new ArrayList<String>();
		for (Node n : topo.getNodes()){
			double x=Math.round(n.getX()*100/scale)/100.0;
			double y=Math.round((viewer.getHeight()-n.getY())*100/scale)/100.0;
			s=s+"  \\path ("+x+","+y+") node ("+n+") {};"+delim;
			if (!isDefault(n.toString()))
				labels.add(n.toString());
		}
		if (labels.size()>0){
			s=s+"  \\tikzstyle{every node}=[]"+delim;
			for (String label : labels)
				s=s+"  \\path[] ("+label+")+(.3,.3) node (label"+label+") {\\small "+label+"};"+delim;
		}
		s+="  \\tikzstyle{every path}=[]"+delim;
		String specialEdges="";
		for (Link e : topo.getLinks()){
			Node from=e.source;
			Node to=e.destination;
			if (e.getWidth()>1){
				if (from==(Node)to.getProperty("father"))
					specialEdges+="  \\draw ("+to+")--("+from+");"+delim;
				else
					specialEdges+="  \\draw ("+from+")--("+to+");"+delim;
			}else{			
				s+="  \\draw ("+from+")--("+to+");"+delim;
			}
		}
		if (specialEdges.length()>0){
			s+="  \\tikzstyle{every path}=[very thick];"+delim+specialEdges;
		}
		s+="\\end{tikzpicture}"+delim;
		textArea.setText(s);
	}
	protected void updateIDs(){
		List<Node> nodes=topo.getNodes();
		Collections.sort(nodes, new NodeComparator());
		int nextID=0;
		for (Node n : nodes){
			if(isDefault(n.toString())){
				n.setProperty("id", "v"+nextID++);
				n.setState("v"+nextID);
			}
		}
	}
	protected boolean isDefault(String id){
		if(id.indexOf("@")>0 ||	(id.charAt(0)=='v' && id.substring(1).matches("\\d+")) || id.length()>4)
			return true;
		return false;
	}
}
