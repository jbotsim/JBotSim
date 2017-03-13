/*
 * This file is part of JBotSim.
 *
 *    JBotSim is free software: you can redistribute it and/or modify it
 *    under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Authors:
 *    Arnaud Casteigts        <arnaud.casteigts@labri.fr>
 */
package jbotsim;


public final class Message extends _Properties{
    protected Node sender;
    protected Node destination;
    protected Object content;
    protected boolean retryMode;

    public Message(){
        this(null, null, "");
    }
    /**
     * @param content The content of this message. It may be an object of any class, whose
     *                reference is going to be shared between sender and destination (no copy).
     */
    public Message(Object content){
        this(null, null, content);
    }
    Message(Node sender, Node destination, Object content){
        assert(destination!=null);
        this.sender = sender;
        this.destination=destination;
        this.content=content;
    }
    /**
     * The sender of this message.
     */
    public Node getSender(){
        return sender;
    }
    /**
     * The destination of this message.
     */
    public Node getDestination(){
        return destination;
    }
    /**
     * The content of this message, which may be an object of any class.
     */
    public Object getContent(){
        return content;
    }
    public String toString(){
        return sender + " -> " + destination+ ": " + content;
    }
}
