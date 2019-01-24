# Examples submodule

The examples present in this module are used to demonstrate JBotSim usage.

This README contains:
* [Simple example](#simple-example): your first JBotSim example (simple code + dependency declaration);
* [Submodule dependency management](#submodule-dependency-management): an explanation of the submodule's dependencies;
* [Examples list](#examples-list): the list of the main examples, and their explanation (***TODO***).

## Simple example

JBotSim's HelloWorld is pretty simple. We simply need to:
1. [declare the dependency](#declaring-the-dependency) to JBotSim,
2. provide the [HelloWorld class](#helloworld-class).

It is that simple!
### Declaring the dependency

In your `build.gradle`, make sure to have declared [Maven Central](https://search.maven.org/search?q=g:io.jbotsim) as a 
repository:
```
repositories {
    mavenCentral()
}
```

For this HelloWorld, we need the content of the `jbotsim-ui-swing` artifact 
(See [Which artifact should I use?](../../lib/README.md#which-artifact-should-i-use) section to easily find out which 
artifact suits your needs). 
So our `build.gradle` will simply declare the dependency as follows:
```
dependencies {
    implementation "io.jbotsim:jbotsim-ui-swing:1.0.0"
}
```
Gradle and Maven will take care of retrieving any required dependencies.

### HelloWorld class
The source code of this example is pretty straightforward. You can also download the file from 
[here](src/main/java/examples/basic/helloworld/HelloWorld.java).

```java
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class HelloWorld{
    public static void main(String[] args){
        Topology tp = new Topology();
        new JViewer (tp);
        tp.start();
    }
}
```

1. We simply create a `Topology`, which is the main object of JBotSim and thus contains the main information of the
simulation.
2. We pass it to a `JViewer`, which will display the simulation elements and allow the user to interact with it.
3. And eventually, we start the simulation (`tp.start();`). Since we use a `JViewer`, this step could also be done by 
the user by selecting *"Pause or resume execution"* in the `JViewer`'s contextual menu (right click anywhere).



## Submodule dependency management

For development purpose, the whole module currently declares project dependencies 
on `:lib:jbotsim-extras-common` and `:lib:jbotsim-ui-swing`. 

Amongst other points, this means that:
* `:lib:jbotsim-extras-swing` is not used;
* it does not use the *fat jar* or Maven Central.

## Examples list


***TODO: add specifics***