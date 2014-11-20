package jbotsim;

public class NodePoint {
	private double x, y, z;
	
	public NodePoint(){
		this(0, 0, 0);
	}
	public NodePoint(double x, double y){
		this(x, y, 0);
	}
	public NodePoint(double x, double y, double z){
		setLocation(x, y, z);
	}
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public void setLocation(double x, double y){
        setLocation(x, y, z);
    }
    public void setLocation(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double distance(NodePoint p){
    	return distance(p.x, p.y, p.z);
    }
    public double distance(double x, double y){
        return distance(x, y, z);
    }
    public double distance(double x, double y, double z){
        return Math.sqrt(distanceSq(x, y, z));
    }
    public double distanceSq(double x, double y, double z){
		double xd = this.x - x;
		double yd = this.y - y;
    	if (this.z == z)
    		return (xd*xd + yd*yd);
    	else{
    		double zd = this.z - z;
    		return (xd*xd + yd*yd + zd*zd);
    	}
    }
    public boolean isWithin(NodePoint p, double threshold){
    	return isWithin(p.x, p.y, p.z, threshold);
    }
    public boolean isWithin(double x, double y, double threshold){
    	return isWithin(x, y, z, threshold);
    }
    public boolean isWithin(double x, double y, double z, double threshold){
    	return distanceSq(x, y, z) < threshold*threshold;
    }
}
