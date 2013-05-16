/*
 * This file is part of JBotSim.
 * 
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *  
 *    Authors:
 *    Arnaud Casteigts		<casteig@site.uottawa.ca>
 */
package jbotsim;

import java.util.HashMap;
import java.util.ArrayList;

import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;


public class Message{
	/**
	 * The source of this message.
	 */
	public final Node source;
	/**
	 * The destination of this message.
	 */
	public final Node destination;
	/**
	 * The content of this message, which may be an object of any kind. The
	 * object is not copied while sending the message, so the reference is
	 * shared between the source and the destination(s). 
	 */
	public final Object content;
	protected final boolean retryMode; 
	protected static boolean debuggingMode=false;
	protected static int nbDeliveredMessages=0;
	protected static int messageDelay=1;
	
	Message(Node source, Node destination, Object content){
		this(source, destination, content, false);
	}
	Message(Node source, Node destination, Object content, boolean retryMode){
		assert(destination!=null || !retryMode);
		this.source=source;
		this.destination=destination;
		this.content=content;
		this.retryMode=retryMode;
	}
	public String toString(){
		String dest=(destination==null)?"all"+source.getNeighbors():destination.toString();
		return source + " -> " + dest + ": " + content;
	}
	static class MessageEngine implements ClockListener{
		private HashMap<Message, Integer> currentMessages=new HashMap<Message,Integer>();
		private Topology topo;
		
		MessageEngine(Topology topo){
			this.topo=topo;
			Clock.addClockListener(this, 1);
		}
		public void onClock(){
			for (Node n : topo.nodes){
				ArrayList<Message> toRemove=new ArrayList<Message>();
				for (Message m : n.sendQueue){
					if (m.destination!=null && m.source.getOutLinkTo(m.destination)==null){
						if (!m.retryMode)
							toRemove.add(m);
					}else{
						currentMessages.put(m, messageDelay);
						toRemove.add(m);
					}
				}
				n.sendQueue.removeAll(toRemove);
			}
			for (Message m : new HashMap<Message,Integer>(currentMessages).keySet()){
				int remainingDelay=currentMessages.get(m);
				if (remainingDelay==1){
					currentMessages.remove(m);
					if (m.destination!=null){
						if (m.source.getOutLinkTo(m.destination)!=null)
							deliverMessageTo(m, m.destination);
						else{
							if (m.retryMode)
								m.source.sendQueue.add(m);
						}
					}else
						for(Link l : m.source.getOutLinks())
							deliverMessageTo(m, l.destination);
				}else{
					if (m.source.getOutLinkTo(m.destination)!=null){
						currentMessages.put(m, remainingDelay-1);
					}else{
						currentMessages.remove(m);
						if (m.retryMode)
							m.source.sendQueue.add(m);
					}
				}
			}
		}
		protected void deliverMessageTo(Message m, Node dest){
			dest.mailBox.add(m);
			if (Message.debuggingMode)
				System.err.println((Clock.currentTime()-messageDelay)+"->"+Clock.currentTime()+": "+
						m.source+ "->" + dest+": "+m.content+" ("+
						m.content.getClass().getSimpleName()+")");
			nbDeliveredMessages++;
			for (MessageListener ml : dest.messageListeners)
				ml.onMessage(m);
		}
	}
	/**
	 * Returns the number of clock steps separating the effective delivery of a 
	 * message from its sending through the <tt>Node.send</tt> method. 
	 */
    public static int getMessageDelay(){
    	return Message.messageDelay;
    }
	/**
	 * Sets the number of clock steps separating the effective delivery of a 
	 * message from its sending through the <tt>Node.send</tt> method. The
	 * minimum is 1 (delivery occurring at the next clock step).
	 * @param delay The message delay.
	 */
    public static void setMessageDelay(int delay){
    	Message.messageDelay=Math.max(delay, 1);
    }
    /**
	 * Causes all messages to be printed on <tt>System.err</tt> at delivery 
	 * time.
	 * @param debugMode <tt>true</tt> to enable, <tt>false</tt> to disable.
	 */
	public static void setDebuggingMode(boolean debugMode){
		Message.debuggingMode=debugMode;
	}
    /**
	 * Returns the number of messages successfully delivered so far.
	 * Useful for statistics..
	 */
	public static int nbDeliveredMessages(){
		return nbDeliveredMessages;
	}
}
