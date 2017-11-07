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


import java.util.HashMap;

public final class Message extends _Properties {
    protected Node sender;
    protected Node destination;
    protected Object content;
    protected boolean retryMode;
    protected String flag;

    /**
     * Default constructor with empty content
     */
    public Message() {
        this(null, null, "");
    }

    /**
     * @param content The content of this message. It may be an object of any class, whose
     *                reference is going to be shared between sender and destination (no copy).
     */
    public Message(Object content) {
        this(null, null, content);
    }

    /**
     * @param content The content of this message. It may be an object of any class, whose
     *                reference is going to be shared between sender and destination (no copy).
     * @param flag    A custom flag for this message
     */
    public Message(Object content, String flag) {
        this(null, null, content, flag);
    }

    /**
     * Custom constructor
     *
     * @param sender      The sender of the message
     * @param destination The destination of the message
     * @param content     The content of this message. It may be an object of any class, whose
     *                    reference is going to be shared between sender and destination (no copy).
     */
    Message(Node sender, Node destination, Object content) {
        this.sender = sender;
        this.destination = destination;
        this.content = content;
        this.flag = content.getClass().toString();
    }

    /**
     * Custom constructor
     *
     * @param sender      The sender of the message
     * @param destination The destination of the message
     * @param content     The content of this message. It may be an object of any class, whose
     *                    reference is going to be shared between sender and destination (no copy).
     * @param flag        A custom flag for this message
     */
    Message(Node sender, Node destination, Object content, String flag) {
        this.sender = sender;
        this.destination = destination;
        this.content = content;
        this.flag = flag;
    }

    /**
     * Copy constructor
     *
     * @param message The original message to be copied.
     */
    public Message(Message message) {
        this(message.getSender(), message.getDestination(), message);
    }

    /**
     * Copy constructor with custom sender and destination
     *
     * @param sender      The new sender of the message
     * @param destination The new destination of the message
     * @param message     The original message to be copied.
     */
    Message(Node sender, Node destination, Message message) {
        this.sender = sender;
        this.destination = destination;
        this.content = message.content;
        this.retryMode = message.retryMode;
        this.flag = message.flag;
        this.properties = new HashMap<>(message.properties);
    }

    /**
     * Copy the current message, changing only the destination
     *
     * @param newDestination The new destination of the message
     */
    public Message withDestination(Node newDestination) {
        return new Message(this.getSender(), newDestination, this);
    }

    /**
     * The sender of this message.
     */
    public Node getSender() {
        return sender;
    }

    /**
     * The destination of this message.
     */
    public Node getDestination() {
        return destination;
    }

    /**
     * The content of this message, which may be an object of any class.
     */
    public Object getContent() {
        return content;
    }

    /**
     * Returns the flag of this message.
     */
    public String getFlag() {
        return flag;
    }

    public String toString() {
        return sender + " -> " + destination + ": " + content;
    }
}
