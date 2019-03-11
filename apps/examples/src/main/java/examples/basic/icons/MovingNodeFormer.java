package examples.basic.icons;

import io.jbotsim.ui.icons.Icons;

public class MovingNodeFormer extends RandomMovingNode {
    @Override
    public void onStart() {
        super.onStart();
        setIcon(Icons.CIRCLE);
    }

}
