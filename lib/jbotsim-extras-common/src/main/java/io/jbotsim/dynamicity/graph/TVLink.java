/*
 * Copyright 2008 - 2019, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>
 *
 *
 * This file is part of JBotSim.
 *
 * JBotSim is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JBotSim is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
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
