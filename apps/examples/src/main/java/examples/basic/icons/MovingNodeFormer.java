package examples.basic.icons;

public class MovingNodeFormer extends RandomMovingNode {
    @Override
    public void onStart() {
        super.onStart();
        setIcon("/io/jbotsim/ui/icons/circle.png");
    }

}
