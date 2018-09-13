package jbotsimx.obstacle.shapes3d;

public class NotPerpendicularException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -2980352726574175724L;

    public NotPerpendicularException(){
        System.out.println("The given point do not represent a rectangular facet");
    }
}
