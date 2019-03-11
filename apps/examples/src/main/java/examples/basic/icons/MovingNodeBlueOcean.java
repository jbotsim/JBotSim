package examples.basic.icons;

import io.jbotsim.ui.icons.Icons;

public class MovingNodeBlueOcean extends RandomMovingNode {
    @Override
    public void onStart() {
        super.onStart();
        setIcon(Icons.CIRCLE_BLUE_OCEAN_32);
    }

}
