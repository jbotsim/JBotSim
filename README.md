# The JBotSim Library

[![Build Status](https://travis-ci.org/acasteigts/JBotSim.svg?branch=master)](https://travis-ci.org/acasteigts/JBotSim)

JBotSim is a simulation library for distributed algorithms in dynamic
networks. The style of programming is mainly event-driven: your code
can react to various events (pulse of a clock,
appearance/disappearance of a link, arrival of a message, movement of
the node, etc.). Node movements can be controlled by program (e.g.
mobile robots or mobility models), or by means of mouse-based
interactions during the execution. Beyond its features, the main asset
of JBotSim is its simplicity of use.

For examples of code, refer to the `src/examples` directory (here), or to the example section of [JBotSim's website](http://jbotsim.io).

Here is a basic HelloWorld with JBotSim:

```java
import jbotsim.Topology;
import jbotsimx.ui.JViewer;

public class HelloWorld{
    public static void main(String[] args){
        Topology tp = new Topology();
        new JViewer (tp);
	tp.start();
    }
}
```
