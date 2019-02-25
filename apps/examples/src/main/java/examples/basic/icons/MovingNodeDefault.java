package examples.basic.icons;

public class MovingNodeDefault extends RandomMovingNode {
    @Override
    public void onStart() {
        super.onStart();
        setIcon("/io/jbotsim/ui/icons/default-node-icon.png");
    }

}
