package io.jbotsim.dynamicity.graph;

import io.jbotsim.core.Node;

public class TVLinkTest {
    public static void main(String args[]){
        TVLink l=new TVLink(new Node(), new Node());
        l.addAppearanceDate(2);
        l.addDisappearanceDate(4);
        l.addAppearanceDate(5);
        l.addDisappearanceDate(7);
        System.out.println(l);
    }
}
