package io.jbotsim.dynamicity.graph;

import java.util.Iterator;
import java.util.TreeSet;

import io.jbotsim.core.Link;
import io.jbotsim.core.Node;

public class TVLink extends Link{
    TreeSet<Integer> appearanceDates=new TreeSet<Integer>();
    TreeSet<Integer> disappearanceDates=new TreeSet<Integer>();
    /*
     * Creates an undirected wired edge between n1 and n2.
     */
    public TVLink(Node n1, Node n2) {
        super(n1, n2);
    }
    public void addAppearanceDate(int date){
        appearanceDates.add(date);
    }
    public void addDisappearanceDate(int date){
        disappearanceDates.add(date);
    }
    public boolean isPresentAtTime(int time){
        Integer previousApp = appearanceDates.floor(time);
        if (previousApp==null)
            return false;
        Integer nextDis = disappearanceDates.ceiling(previousApp+1);
        if (nextDis==null)
            return true;
        else
            return (time < nextDis);
    }
    public String toString(){
        String s="";
        Iterator<Integer> appIt=appearanceDates.iterator();
        Iterator<Integer> disIt=disappearanceDates.iterator();
        while (appIt.hasNext() && disIt.hasNext())
            s+="("+appIt.next()+","+disIt.next()+"],";
        if (appIt.hasNext())
            s+="("+appIt.next()+",-)";
        else{
            if (s.length()>0)
                s=s.substring(0, s.length()-1);
        }
        return super.toString()+" : "+s;
    }
}
