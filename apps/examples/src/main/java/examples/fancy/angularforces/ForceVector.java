package examples.fancy.angularforces;

public class ForceVector{
    protected double x, y, weight;
	public ForceVector(){
		this.setXYW(0,0,0);
	}
	public ForceVector(double angle, double magnitude, double weight){
		this.setPolar(angle, magnitude, weight);
	}
	public void add(ForceVector vec){
		if (this.weight==0 && vec.weight==0)
			return;
		double tmpx=x*weight+vec.x*vec.weight;
		double tmpy=y*weight+vec.y*vec.weight;
		double tmpweight=weight+vec.weight;
		ForceVector tmp=new ForceVector();tmp.setXYW(tmpx/tmpweight, tmpy/tmpweight, tmpweight);
		x=tmp.x;y=tmp.y; weight=tmp.weight;
	}
	public void setXYW(double x, double y, double weight){
		this.x=x; this.y=y; this.weight=weight;
	}
	public void setPolar(double angle, double magnitude, double weight){
		x=Math.cos(angle)*magnitude; y=-Math.sin(angle)*magnitude; this.weight=weight;
	}
	public double getMagnitude(){
		return Math.sqrt(x*x+y*y);
	}
	public double getDirection(){
		return -Math.atan2(y, x);
	}
	public String toString(){
		return "("+x+","+y+","+weight+")";
	}
}
