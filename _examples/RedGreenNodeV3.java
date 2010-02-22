import jbotsim.Node;
import jbotsim.Clock;
import jbotsim.Message;
import jbotsim.event.ClockListener;
import jbotsim.event.MessageListener;

public class RedGreenNodeV3 extends Node implements ClockListener, MessageListener{
	int lastReceptionDate = -30;
	public RedGreenNodeV3(){
		Clock.addClockListener(this, 30);
		super.addMessageListener(this);
	}
	public void onClock() {
		super.send(null, "HELLO");
		if (lastReceptionDate < Clock.currentTime() - 30)
			super.setProperty("color", "red");
	}
	public void onMessage(Message msg) {
		super.setProperty("color", "green");
		lastReceptionDate=Clock.currentTime();
	}
}
