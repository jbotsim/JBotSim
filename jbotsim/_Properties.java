package jbotsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jbotsim.event.PropertyListener;

public abstract class _Properties {
	protected HashMap<String,Object> properties = new HashMap<String,Object>();
    List<PropertyListener> propertyListeners=new ArrayList<PropertyListener>();
    
	/**
	 * Registers the specified property listener to this node. The listener
	 * will be notified every time a property of this node changes. 
	 * @param listener The movement listener.
	 */
	public void addPropertyListener(PropertyListener listener){
	    propertyListeners.add(listener);
	}
	/**
	 * Unregisters the specified property listener for this node.
	 * @param listener The property listener. 
	 */
	public void removePropertyListener(PropertyListener listener){
	    propertyListeners.remove(listener);
	}
	/**
	 * Returns the property stored under the specified key.
	 * @param key The property key.
	 */
	public Object getProperty(String key){
		return properties.get(key);
	}
	/**
	 * Stores the specified property (<tt>value</tt>) under the specified name
	 * (<tt>key</tt>). 
	 * @param key The property name.
	 * @param value The property value.
	 */
	public void setProperty(String key, Object value){
		properties.put(key, value);
	    for (PropertyListener pl : new ArrayList<PropertyListener>(propertyListeners))
	        pl.propertyChanged(this, key);
	}

}
