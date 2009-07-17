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
package jbotsim.core;

import java.util.Hashtable;
import java.util.Vector;

import jbotsim.core.event.PropertiesListener;

/**
 * Class equivalent to an {@link Hashtable&lt;String,Object&gt;} whose changes can be observed
 * through the {@link event.PropertiesListener} interface.
 */
public class _Properties{

    protected Hashtable<String,Object> properties;
    protected Vector<PropertiesListener> listeners;

    public _Properties(){
    	this(null);
    }
    public _Properties(_Properties model){
    	listeners=new Vector<PropertiesListener>();
    	properties=(model!=null)?new Hashtable<String,Object>(model.properties):new Hashtable<String,Object>();
    }
    /**
     * Retrieves the object pointed by 'key' (you will have to cast it back on your side).
     */
    public Object getProperty(String key){
    	return (properties.containsKey(key)) ? properties.get(key) : null;
    }
    public void setProperty(String key, Object value){
    	if (value==null)
    		properties.remove(key);
    	else
    		properties.put(key, value);
    	notifyPropertyChanged(key);
    }
    public void addPropertiesListener(PropertiesListener listener){
    	listeners.add(listener);
    }
    public void removePropertiesListener(PropertiesListener listener){
    	listeners.remove(listener);
    }
    protected void notifyPropertyChanged(String property){
        for (PropertiesListener pl : listeners)
            pl.propertyChanged(this, property);
    }
}
