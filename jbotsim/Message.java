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


public final class Message extends _Properties{
	public Node source;
	public Node destination;
	public Object content;
	protected boolean retryMode;
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
}
