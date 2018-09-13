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
package jbotsimx;

import java.awt.*;
import java.awt.event.*;

import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.Link.Mode;
import jbotsim.Link.Type;
import jbotsimx.format.plain.PlainFormatter;
import jbotsimx.format.tikz.TikzFormatter;

@SuppressWarnings("serial")
public class JConsole extends TextArea implements TextListener{
    protected Topology topo;
    public JConsole(Topology topo){
        super("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.topo=topo;
        setBackground(Color.black);
        setForeground(Color.white);
        addTextListener(this);
    }
    public void textValueChanged(TextEvent event){
        String s=getText();
        int to=s.lastIndexOf("\n");
        if(to == s.length()-1 && to!=-1){
            int from=s.lastIndexOf("\n",to-1)+1;
            try{
                executeCommand(s.substring(from,to).split("\\s"));
            }catch(Exception e){e.printStackTrace();}
        }
    }
    protected void executeCommand(String[] args) throws Exception{
           if (args[0].equals("clear")){
               topo.clear();
           }else if (args[0].equals("print")){
               this.setText(topo.toString());
           }else if (args[0].equals("add")){
               if (args[1].equals("link")){
                   String id1=args[2]; String id2=args[3];
                   Node n1=null, n2=null;
                   for (Node n:topo.getNodes()){
                       if (n.toString().equals(id1))
                           n1=n;
                       if (n.toString().equals(id2))
                           n2=n;
                   }
                   if (n1!=null && n2!=null)
                       topo.addLink(new Link(n1,n2,Type.UNDIRECTED,Mode.WIRED));
               }
           }else if (args[0].equals("load")){
               topo.clear();
               String exemple="communicationRange 0.0\nsensingRange 0.0\nA [345.0, 291.0]\nB [419.0, 239.0]\nC [367.0, 367.0]\nNode@671f95 [388.0, 305.0]\nA <--> B\nA <--> C\n";
               new PlainFormatter().importTopology(topo, exemple);
           }else if (args[0].equals("tikz")){
               this.setText(new TikzFormatter().exportTopology(topo));
           }else if (args[0].equals("rename")){
               Node n=(Node)topo.getProperty("selectedNode");
               if (n!=null) 
                   n.setProperty("id", args[1]);
        }
    }
//    public static void main(String args[]) throws Exception{
//        jbotsim.Topology tp=new jbotsim.Topology();
//        Node.getNodeModel("default").setSensingRange(25);
//        JFrame win=new JFrame();
//        jbotsim.ui.JViewer v=new jbotsim.ui.JViewer(tp, false); 
//        win.add(v.getJTopology(), BorderLayout.CENTER);
//        win.add(new JConsole(tp),BorderLayout.SOUTH);
//        win.setSize(800, 650);
//        win.setVisible(true);
//        Node.getNodeModel("default").setCommunicationRange(50);
//    }    
}