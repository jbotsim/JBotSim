package jbotsimx.obstacle.shapes3d;


import jbotsim.Point;

public class Vector3D{

    double vx;
    double vy;
    double vz;

    public Vector3D(Point p1, Point p2){
        vx=p2.getX()-p1.getX();
        vy=p2.getY()-p1.getY();
        vz=p2.getZ()-p1.getZ();
    }

    public Vector3D(double vx, double vy, double vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    public Vector3D(Vector3D a){
        vx=a.vx;
        vy=a.vy;
        vz=a.vz;
    }

    public Vector3D produitVectorielWith(Vector3D b){

        return new Vector3D(vy*b.vz-vz*b.vy,vz*b.vx-vx*b.vz,vx*b.vy-vy*b.vx);
    }

    public double norme(){

        return Math.sqrt(vx*vx+vy*vy+vz*vz);
    }

    public Vector3D dividedBy(double val){
        return new Vector3D(vx/val,vy/val,vz/val);

    }

    public double dot(Vector3D b){
        return vx*b.vx+vy*b.vy+vz*b.vz;
    }

    public Vector3D sum(Vector3D v){
        return new Vector3D(vx+v.vx,vy+v.vy,vz+v.vz);
    }

    public Vector3D multipliedBy(double val){
        return new Vector3D(vx*val,vy*val,vz*val);
    }

    public Point getNewPointFrom(Point n){
        return new Point(n.getX()+vx,n.getY()+vy,n.getZ()+vz);
    }

}
