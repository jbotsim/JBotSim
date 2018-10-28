package examples.snakes;

import jbotsim.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Snake extends Topology {
    private int SNAKE_NODE_SIZE = 2 * Node.DEFAULT_SIZE;
    private int SNAKE_LINK_WIDTH = 5 * Link.DEFAULT_WIDTH;
    private ArrayList<Node> snakeNodes;
    private ArrayList<Link> snakeLinks;
    private int cur_node_index;
    private boolean isMoveComplete;

    public Snake(Topology tp, int len) {
        cur_node_index = 0;
        isMoveComplete = true;

        snakeNodes = new ArrayList<>(len);
        snakeLinks = new ArrayList<>(len); // including one elastic link
        Node cur = chooseSnakeHead(tp);
        cur.setSize(SNAKE_NODE_SIZE);
        cur.setColor(Color.GREEN);
        snakeNodes.add(cur);
        for (int i = 1; i < len; i++) {
            Node next = chooseNext(cur);
            snakeNodes.add(next);
            Link l = cur.getCommonLinkWith(next);
            l.setColor(Color.RED);
            l.setWidth(SNAKE_LINK_WIDTH);
            snakeLinks.add(l);
            next.setColor(Color.RED);
            next.setSize(SNAKE_NODE_SIZE);
            cur = next;
        }
    }

    @Override
    public void onClock() {
        if (isMoveComplete) {
            moveHead(snakeNodes.get(0));
            isMoveComplete = false;
            ++cur_node_index;
        } else {
            moveBody(cur_node_index);
        }
    }

    private Node chooseSnakeHead(Topology tp) {
        int n = tp.getNodes().size();
        Random random = new Random();
        int head = random.nextInt(n);
        return tp.getNodes().get(head);
    }

    private Node chooseNext(Node node) {
        List<Node> neighbors = node.getNeighbors();
        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < neighbors.size(); ++i) {
            if (neighbors.get(i).getColor() == Color.ORANGE) {
                candidates.add(i);
            }
        }

        if (candidates.isEmpty())
            return null;
        Random random = new Random();
        int body = candidates.get(random.nextInt(candidates.size()));
        return neighbors.get(body);
    }

    private void moveHead(Node cur_head) {
        Node next_head = chooseNext(cur_head);
        next_head.setColor(Color.GREEN);
        next_head.setSize(SNAKE_NODE_SIZE);
        cur_head.setColor(Color.ORANGE);
        cur_head.setSize(Node.DEFAULT_SIZE);
        Link l = cur_head.getCommonLinkWith(next_head);
        // ORANGE links show elasticity
        l.setColor(Color.ORANGE);
        l.setWidth(SNAKE_LINK_WIDTH);
        snakeLinks.get(0).setColor(Color.ORANGE); // set the current first link to ORANGE
        snakeLinks.get(0).setWidth(SNAKE_LINK_WIDTH);
        snakeLinks.add(0, l); // insert the new link
        snakeNodes.add(0, next_head);
    }

    private void moveBody(int cur_index) {
        snakeNodes.get(cur_index).setColor(Color.RED);
        snakeNodes.get(cur_index).setSize(SNAKE_NODE_SIZE);
        snakeNodes.get(cur_index + 1).setColor(Color.ORANGE);
        snakeNodes.get(cur_index + 1).setSize(Node.DEFAULT_SIZE);
        snakeLinks.get(cur_index - 1).setColor(Color.RED);
        if (cur_index < snakeNodes.size() - 2) {
            snakeLinks.get(cur_index + 1).setColor(Color.ORANGE);
            ++cur_node_index;
        } else {
            // tail move
            snakeLinks.get(cur_index).setColor(Link.DEFAULT_COLOR);
            snakeLinks.get(cur_index).setWidth(Link.DEFAULT_WIDTH);
            snakeNodes.get(snakeNodes.size() - 1).setSize(Node.DEFAULT_SIZE);
            snakeNodes.remove(snakeNodes.size() - 1);
            snakeLinks.remove(snakeLinks.size() - 1);
            cur_node_index = 0;
            isMoveComplete = true;
        }
    }
}
