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
package jbotsim.gui;

import java.awt.*;
import java.awt.event.*;

import jbotsim.core.Node;
import jbotsim.core.Topology;


@SuppressWarnings("serial")
public class JConsole extends TextArea implements TextListener{
	Topology topo;
    public JConsole(Topology topo){
        super("",6,10,TextArea.SCROLLBARS_VERTICAL_ONLY);
        this.topo=topo;
        setBackground(Color.black);
        setForeground(Color.white);
        addTextListener(this);
    }
    public void textValueChanged(TextEvent e){
        String s=getText();
        int to=s.lastIndexOf("\n");
        if(to == s.length()-1 && to!=-1){
            int from=s.lastIndexOf("\n",to-1)+1;
            executeCommand(s.substring(from,to).split("\\s"));
        }
    }
    private void executeCommand(String[] args){
    	if (args[0].equals("rename")){
    		Node n=(Node)topo.getProperty("selectedNode");
    		if (n!=null) 
    			n.setProperty("id", args[1]);
        }
    }
}