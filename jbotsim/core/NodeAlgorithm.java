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

/**	
 * The class NodeAlgorithm provides the simplest way to code an algorithm with JBotSim. All you
 * have to do is to create a class that extends it and implements the three abstract methods:
 * {@link init}, {@link exit}, and {@link performStep}. Once your class ready, just attach
 * an instance of it to any node that should execute it (using {@link Node.addAlgorithm}). 
 * The code in {@link performStep} will be periodically executed, and is the right place to put
 * your code, whereas {@link init} and {@link exit} will be executed only when the node is added 
 * or removed from the topology, respectively. 
 */
public abstract class NodeAlgorithm {
	protected int rate;
	protected boolean initialized;
	
	public void setRate(int rate){
		this.rate=rate;
	}
	public int getRate(){
		return rate;
	}
	public void setInitialized(boolean initialized){
		this.initialized=initialized;
	}
	public boolean isInitialized(){
		return initialized;
	}
	public abstract void init(Node node);
	public abstract void performStep(Node node);
	public abstract void exit(Node node);
}
