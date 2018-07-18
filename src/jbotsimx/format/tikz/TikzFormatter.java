/*
 * Tikz export for objects of type Topology in JBotSim.
 * 
 *    This program is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts        <casteig@site.uottawa.ca>
 */
package jbotsimx.format.tikz;

import jbotsim.Color;
import jbotsim.Link;
import jbotsim.Node;
import jbotsim.Topology;
import jbotsimx.format.common.Format;
import jbotsimx.format.common.Formatter;
import jbotsimx.ui.JViewer;


public class TikzFormatter implements Formatter{
    public static String getStringColor(Color color){
        String result = "";
        if (color == Color.black)
            result = "black";
        if (color == Color.red)
            result = "red";
        if (color == Color.blue)
            result = "blue";
        if (color == Color.green)
            result = "green";
        if (color == Color.white)
            result = "white";
        if (color == Color.gray)
            result = "gray";
        if (color == Color.cyan)
            result = "cyan";
        if (color == Color.magenta)
            result = "magenta";
        if (color == Color.orange)
            result = "orange";
        if (color == Color.darkGray)
            result = "darkgray";
        if (color == Color.lightGray)
            result = "lightGray";
        if (color == Color.pink)
            result = "pink";
        if (color == Color.yellow)
            result = "yellow";
        return result;
    }

    @Override
    public void importTopology(Topology tp, String s) {
        return;
    }

    @Override
    public String exportTopology(Topology tp){
        return exportTopology(tp, 50);
    }

    public String exportTopology(Topology tp, double scale){
        String delim="\n";
        String s="\\begin{tikzpicture}[scale=1]"+delim;
        Integer sr=(int)tp.getSensingRange();
        if (sr!=0){
            s=s+"  \\tikzstyle{every node}=[draw,circle,inner sep="+sr/5.0+", fill opacity=0.5,gray,fill=gray!40]"+delim;
            for (Node n : tp.getNodes()){
                double x=Math.round(n.getX()*100/scale)/100.0;
                double y=Math.round((600-n.getY())*100/scale)/100.0;
                s=s+"  \\path ("+x+","+y+") node ["+getStringColor(n.getColor())+"] (v" + n + ") {};"+delim;
            }
        }
        s=s+"  \\tikzstyle{every node}=[draw,circle,fill=gray,inner sep=1.5]"+delim;
        for (Node n : tp.getNodes()){
            String id = "v"+n.toString();
            double x=Math.round(n.getX()*100/scale)/100.0;
            double y=Math.round((600-n.getY())*100/scale)/100.0;
            String color = (n.getColor()!=null)?"["+n.getColor().toString()+"]":"";
            s=s+"  \\path ("+x+","+y+") node ["+getStringColor(n.getColor())+"] ("+id+") {};"+delim;
        }
        s+="  \\tikzstyle{every path}=[];"+delim;
        for (Link l : tp.getLinks()) {
            String width="";
            if (l.getWidth()>1)
                width=",ultra thick";
            String id1 = "v"+l.source.toString();
            String id2 = "v"+l.destination.toString();
            s += "  \\draw ["+getStringColor(l.getColor())+width+"] (" + id1 + ")--(" + id2 + ");" + delim;
        }
        s+="\\end{tikzpicture}"+delim;
        return s;        
    }

//    // Test
//    public static void main(String[] args) {
//        Format.setDefaultFormatter(new TikzFormatter());
//        Topology tp = new Topology();
//        new JViewer(tp);
//    }
}
