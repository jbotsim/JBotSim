package examples.basic.icons;

import examples.basic.moving.MovingNode;

public class RandomMovingNode extends MovingNode {
    @Override
    public void onStart() {
        super.onStart();
        setDirection(Math.random()*2*Math.PI);
    }

}
