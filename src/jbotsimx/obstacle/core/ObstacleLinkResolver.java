package jbotsimx.obstacle.core;

import jbotsim.LinkResolver;
import jbotsim.Node;
import jbotsim.Topology;

import java.util.List;

/**
 * This class override the class WLinkCalculator by checking if the given nodes can communicate. To do this it checks is the nodes are close enough and is it does not exist an obstacle between them.
 * @author mbarjon
 *
 */
public class ObstacleLinkResolver extends LinkResolver {
    
    private Topology topology;
    
    public ObstacleLinkResolver(Topology topology){
        this.topology=topology;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isHeardBy(Node arg0, Node arg1) {
        if( super.isHeardBy(arg0, arg1)){
            for(Obstacle o: ((List<Obstacle>) topology.getProperty("Obstacles"))){
                if(o.obstructLink(arg0, arg1))
                    return false;
            }
            return true;
        }
        return false;
    }
    
}
