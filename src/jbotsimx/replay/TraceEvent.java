package jbotsimx.replay;

public class TraceEvent {
    public enum EventKind {
        ADD_NODE, DEL_NODE, SELECT_NODE, MOVE_NODE, START_TOPOLOGY
    }

    private EventKind kind;
    private int time;
    private int id;
    private double x;
    private double y;
    private String nodeClass;

    protected TraceEvent(int time, EventKind kind) {
        this(time, -1, kind);
    }

    protected TraceEvent(int time, int id, EventKind kind) {
        this.id = id;
        this.time = time;
        this.kind = kind;
    }

    public static TraceEvent newStartTopology(int time) {
        return new TraceEvent(time, EventKind.START_TOPOLOGY);
    }

    public static TraceEvent newAddNode(int time, int id, double x, double y, String className) {
        TraceEvent result = new TraceEvent(time, id, EventKind.ADD_NODE);
        result.x = x;
        result.y = y;
        result.nodeClass = className;

        return result;
    }

    public static TraceEvent newDeleteNode(int time, int id) {
        return new TraceEvent(time, id, EventKind.DEL_NODE);
    }

    public static TraceEvent newSelectNode(int time, int id) {
        return new TraceEvent(time, id, EventKind.SELECT_NODE);
    }

    public static TraceEvent newMoveNode(int time, int id, double x, double y) {
        TraceEvent result = new TraceEvent(time, id, EventKind.MOVE_NODE);
        result.x = x;
        result.y = y;

        return result;
    }

    public EventKind getKind() {
        return kind;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getNodeClass() {
        return nodeClass;
    }

    public int getTime() {

        return time;
    }

    public int getNodeID() {
        return id;
    }
}
