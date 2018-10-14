package examples.snakes;

import jbotsim.Node;
import jbotsim.Topology;
import jbotsim.event.ClockListener;
import java.util.Random;
import java.util.ArrayList;

public class Snake implements ClockListener {
    private ArrayList<Node> snakeNodes;
    private int currentMoveNode;

    public Snake(Topology tp, int len) {
        snakeNodes.chooseSnakeHead(tp);
        for (int i = 0; i < len; i++) {

        }
    }

    @Override
    public void onClock() {

    }

    private Node chooseSnakeHead(Topology tp) {
        int n = tp.getNodes().size();
        Random random = new Random();
        int head = random.nextInt(n + 1);
        return tp.getNodes().get(head);
    }
}
