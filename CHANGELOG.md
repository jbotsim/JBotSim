# Changelog

This file lists modifications introduced by each version.

As of version 1.0.0, the project will try and follow [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

###  JViewer class modifications

**Behavior modifications in JViewer:**

* The `JViewer` now also detects `.gv` and `.xdot` files as to be handled with the `DotTopologySerializer`. [[issue 48]][issue: #48]

###  DotTopologySerializer class modifications

[[issue 48]][issue: #48]

**Behavior modifications in DotTopologySerializer:**

* `DotTopologySerializer.exportToString(Topology)` is now implemented (`null` was knowingly returned in the previous 
  version).

  Amongst other implementation points:
  * node positions are output with the `pos = "x, y!"` syntax;
  * the Y-coordinates are exported flipped, to be consistent with `DotTopologySerializer.importFromString()`;
  * the exportation is done without respect to the `scale` member.

* By default, the `DotTopologySerializer.importFromString(Topology , String)` does not scale anymore (it formerly 
  doubled distances).
     
  The former behavior can be achieved by either:
    * using the `DotTopologySerializer(double scale, int margin, boolean reorganize)` constructor or
    * using `TopologyLayouts.autoscale()`.

**Method symbols in DotTopologySerializer:**

  * `DotTopologySerializer.DEFAULT_SCALE` has been added
   
     The previous default scale factor was `2.0`. It is now set to `1.0`. 
 
  * `DotTopologySerializer.DEFAULT_MARGIN` has been added
   
     The default value is unchanged: `50`.
  
  * `DotTopologySerializer.DOT_FILENAME_EXTENSIONS` has been added
  
    This array contains known extensions for DOT files. It currently contains `"gv"`, `"dot"` and  `"xdot"`.
    Please [prefer][graphviz-gv-extension] the `.gv` extension.
    
[graphviz-gv-extension]: https://marc.info/?l=graphviz-devel&m=129418103126092
[issue: #48]: https://github.com/jbotsim/JBotSim/issues/48

###  TopologyLayouts class modifications

**Method modifications in TopologyLayouts:**
* `TopologyLayouts.autoscale(Topology)` is now `public` [[issue 51]][issue: #51]


**New methods in TopologyLayouts:**
* `TopologyLayouts.autoscale(Topology, AutoScaleParams)` created [[issue 52]][issue: #52]

  This method allows to control the scaling process more precisely than the previous default, thanks to the 
  `AutoScaleParams` object.
  The parameters used with the default method are:
  * *margin ratios*: `0.1`
  * *centering*: enabled
  * *communication ranges scaling*: enabled
  * *sensing ranges scaling*: enabled
  
* `TopologyLayouts.center(Topology)` created [[issue 52]][issue: #52]

  This method centers the content of the `Topology` inside its boundaries (without scaling it).
  
* `TopologyBoundaries TopologyLayouts.computeBoundaries(Topology)` created [[issue 52]][issue: #52]

  This method computes the boundaries of a `Topology`. These are represented by `TopologyBoundaries` object, with 
  minimum and maximum coordinates on 3 axes.
  

[issue: #51]: https://github.com/jbotsim/JBotSim/issues/51
[issue: #52]: https://github.com/jbotsim/JBotSim/issues/52

## [1.0.0] - 2019/02/28

This first official version introduces several changes. They are listed here.

### Maven publication

The project has been split into several artifacts and published on Maven Central under the `io.jbotsim` group.

For modularity, clarity and re-usability sake the project is now split into several subprojects. 
Each resulting artifact is published along with its source code and javadoc, making integration in IDE easier 
(documentation, debugging, etc.).

The initial list of artifact: `jbotsim-all`, `jbotsim-common`, `jbotsim-core`, `jbotsim-extras-common`, 
`jbotsim-extras-swing`, `jbotsim-icons`, `jbotsim-serialization-common`, `jbotsim-ui-common`, `jbotsim-ui-swing`. 

Although Maven Central is now the preferred way to retrieve JBotSim, the former standalone jar is still available.
  
### AWT/Swing free versions

An strong effort has been put to separate AWT/Swing code from the rest, so that *common* artifacts can be used with 
other platforms:
* *common* maven artifacts can be retrieved from Maven Central;
* a *common* standalone jar is also available 

### Package names unification 

All classes have been unified under the same package name: `io.jbotsim`.
*  `jbotsim.*` -> `io.jbotsim.*`
*  `jbotsimx.*` -> `io.jbotsim.*`


### Renamed and/or moved classes

Some classes have been renamed or moved to different packages. 
Here are listed the items that don't have a section of their own.

* Generally, all classes have been moved due to the package names unification:
  * `jbotsim.*` -> `io.jbotsim.*`
  * `jbotsimx.*` -> `io.jbotsim.*`

* "core" classes: 
  * `jbotsim.Clock` -> `io.jbotsim.core.Clock`
  * `jbotsim.ClockManager` -> `io.jbotsim.core.ClockManager`
  * `jbotsim.Color` -> `io.jbotsim.core.Color`
  * `jbotsim.DefaultClock` -> `io.jbotsim.core.DefaultClock`
  * `jbotsim.DefaultScheduler` -> `io.jbotsim.core.Scheduler`
  * `jbotsim.Link` -> `io.jbotsim.core.Link`
  * `jbotsim.LinkResolver` -> `io.jbotsim.core.LinkResolver`
  * `jbotsim.MessageEngine` -> `io.jbotsim.core.MessageEngine`
  * `jbotsim.Message` -> `io.jbotsim.core.Message`
  * `jbotsim.Node` -> `io.jbotsim.core.Node`
  * `jbotsim.Point` -> `io.jbotsim.core.Point`
  * `jbotsim._Properties` -> `io.jbotsim.core.Properties` [[issue 22]][issue: #22]
  * `jbotsim.Scheduler` -> x
  * `jbotsim.Topology` -> `io.jbotsim.core.Topology`
  * `jbotsim.event.*` -> `io.jbotsim.core.event.*`

* Other classes:
  * `jbotsimx.ui.painting.DefaultBackgroundPainter` -> `io.jbotsim.ui.painting.JBackgroundPainter`
  * `jbotsimx.ui.painting.DefaultLinkPainter` -> `io.jbotsim.ui.painting.JLinkPainter`
  * `jbotsimx.ui.painting.DefaultNodePainter` -> `io.jbotsim.ui.painting.JNodePainter`
  * `jbotsimx.dygraph.*` -> `ui.jbotsim.gen.dynamic.graph.*`
  * `jbotsimx.geometry.*` -> `ui.jbotsim.contrib.geometry.*`
  * `jbotsimx.misc.UtilClock` -> x
  * `jbotsimx.obstacle.*` -> `ui.jbotsim.contrib.obstacle.*`
  * `jbotsimx.replay.*` -> `ui.jbotsim.gen.dynamic.trace.*`
  * `jbotsimx.Connectivity` -> `ui.jbotsim.contrib.algos.Connectivity`
  * `jbotsimx.Algorithms` -> `ui.jbotsim.contrib.algos.Algorithms`
  
[issue: #22]: https://github.com/jbotsim/JBotSim/issues/22

### FileManager creation 

`jbotsim-core`/`io.jbotsim.io.FileManager`

The FileManager provides unified files accessing methods (read/write). 
An instance of FileManager is accessible (getter/setter) in the Topology object.

The provided implementation will work on Java Desktop; specific implementation should be provided for other platforms 
(*e.g.* Android). 
  
### Formatter / TopologySerializer

The Format static class has been removed and its behaviours have been pushed into several objects (TopologySerializer,
TopologySerializerFilenameMatcher, FileManager):
* `jbotsimx.format.common.Format` -> x


The Formatter interface has been renamed to TopologySerializer.
The Topology now holds a TopologySerializer (by default, a PlainTopologySerializer).
* `jbotsimx.format.common.Formatter` -> `ui.jbotsim.io.TopologySerializer`
    
The new TopologySerializerFilenameMatcher allows to get a TopologySerializer matching specific filename patterns:
* `jbotsim-serialization-common`/`io.jbotsim.io.format.TopologySerializerFilenameMatcher`
  
  
Former implementations of Formatter have been moved and renamed accordingly:
* `jbotsimx.format.dot.DotFormatter` -> `ui.jbotsim.io.format.dot.DotTopologySerializer`
* `jbotsimx.format.plain.PlainFormatter` -> `ui.jbotsim.io.format.plain.PlainTopologySerializer`
* `jbotsimx.format.tikz.TikzFormatter` -> `ui.jbotsim.io.format.tikz.TikzTopologySerializer`
* `jbotsimx.format.xml.XmlFormatter` -> `ui.jbotsim.io.format.xml.XmlTopologySerializer`
* `jbotsimx.format.xml.*` -> `ui.jbotsim.io.format.xml.*`

    
### UIComponent creation 

`jbotsim-ui-common`/`io.jbotsim.ui.painting.UIComponent`

The UIComponent has been introduced in the `jbotsim-ui-common` artifact, along with several *common* components used 
by JBotSim's default UI implementation (provided by the `jbotsim-ui-swing` artifact).
Although not required, following the same pattern for your implementation would help further implementations.  

To this end, the UIComponent has been introduced to hide the real type of the graphical element.
in order to facilitate user interfaces creation and re-usability. 

For more details, please see the implementation of the `JBackgroundPainter.paintBackground()` and its usage in
`JTopology.paintComponent()`.

### jbotsim-icons module



`jbotsim-icons`/`io.jbotsim.ui.icons`

A specific resources module has been added to provide a limited set of extra icons.
They can be browsed in the corresponding folder. [[issue 26]][issue: #26]


This module contains the `io.jbotsim.ui.icons.Icons` class which provides a set of constants matching the paths of the 
provided icons. [[issue 46]][issue: #46]

[issue: #26]: https://github.com/jbotsim/JBotSim/issues/26
[issue: #46]: https://github.com/jbotsim/JBotSim/issues/46

### Color class modifications

Some color related behaviours have been moved from Node, to Color ([[issue 33]][issue: #33], see also 
[here](#node-class-modifications)).

**Modifications in Color:**
* `Color.getColorAt(int)` creation

  This method returns a Color object supposedly stored at the provided index.   
  A call to this method will make sure that all Colors up to the provided are actually instantiated and return
  the Color object associated to the index.
  
  The sequence of generated Colors is consistent across runs.

* `Color.indexOf(Color)` creation

  This method returns the index of the provided Color object if it corresponds to a Color indexed by a call to 
  `Color.getColorAt()`. It is merely a call to `Color.getIndexedColors().indexOf(Color)`.
  
* `Color.getBasicColors()` is now `Color.getIndexedColors()`

  This method still returns the current state of the indexed Colors list.
  
* `Color.getRandomColor(Random)` creation

  This method returns a Color object generated using 3 calls to the provided Random.
  It has been introduce to foster experiments reproducibility.

[issue: #33]: https://github.com/jbotsim/JBotSim/issues/33

### Node class modifications

**Default behaviour modification:**

[[issue 42]][issue: #42]:
* The `Node`'s default size has been increased from `8` to `10`.
* New 32x32 icons are available in the `jbotsim-icons` submodule:
  * `jbotsim-icons`/`io/jbotsim/ui/icons/circle-blue-32x32.png` 
  * `jbotsim-icons`/`io/jbotsim/ui/icons/circle-blue-ocean-32x32.png` 
  * `jbotsim-ui-swing`/`io/jbotsim/ui/icons/default-node-icon.png`
* The default icon is now `default-node-icon.png`. It is a copy of `circle-blue-32x32.png`.
* The former default icon is still available: `jbotsim-icons`/`io/jbotsim/ui/icons/circle.png`.

**Removals in Node:**
* `Node.setState()` and `Node.getState()` have been removed [[issue 41]][issue: #41]

* `Node.getIntColor()` have been removed [[issue 33]][issue: #33]

   Please use `Color.indexOf(Color)` in association with `Node.getColor()` instead.
   
* `Node.setIntColor()` have been removed [[issue 33]][issue: #33]

   Please use `Node.setColor(Color)` in association with `Color.getColorAt(Integer)` instead.
  
* `Node.setRandomColor()` have been removed [[issue 33]][issue: #33]

  Please use `Node.setColor()` with a color from `Color.getRandomColor()` instead.

**Deprecations in Node:**

* Send methods taking `Object` as a payload have been deprecated for clarity sake:
  * `Node.send(Node, Object)` has been deprecated
  
    Please use `Node.send(Node, Message)` instead.
    
  * `Node.sendAll(Node, Object)` has been deprecated
  
    Please use `Node.sendAll(Node, Message)` instead.
  
  * `Node.sendAll(Object)` has been deprecated
  
    Please use `Node.sendAll(Message)` instead.

**New methods in Node:**

* `Node.die()` has been added [[issue 45]][issue: #45]
  
  It requests the `Node` to die by the end of the round. A dead node can't be revived.
  
* `Node.isDying()` has been added [[issue 45]][issue: #45]
  
  It tells whether the `Node` is dying (or already dead) or not.


**Node methods renaming:**

* `Node.setSize()` is now `Node.setIconSize()` [[issue 39]][issue: #39]
* `Node.getSize()` is now `Node.getIconSize()` [[issue 39]][issue: #39]

[issue: #39]: https://github.com/jbotsim/JBotSim/issues/39
[issue: #41]: https://github.com/jbotsim/JBotSim/issues/41
[issue: #42]: https://github.com/jbotsim/JBotSim/issues/42
[issue: #45]: https://github.com/jbotsim/JBotSim/issues/45

### Point class modifications


* `Point.setLocation(double, double, double)` creation

* `Point.setLocation(Point)` now also takes z component into account

### DefaultClock class modifications

[[issue 44]][issue: #44]
 
* `DefaultClock` now takes `Clock.setTimeUnit()` into account. 

  The default clock speed from the `Topology` is thus now applied. 
  If you want to use it in bulk mode, you need to specify `Topology.setClockSpeed(0)`.
  
* `Defaultclock` is now implemented with a `ReentrantLock` and a `Condition` to wait while paused.
  

[issue: #44]: https://github.com/jbotsim/JBotSim/issues/44

### Command management modifications

The command management has been moved from `JTopology` to `Topology`

***Moved interface:***

`jbotsimx.ui.CommandListener` -> `jbotsim-core`/`io.jbotsim.core.event.CommandListener`

***Moved methods:***
* `JTopology.addCommandListener()` -> `Topology.addCommandListener()`
* `JTopology.removeCommandListener()` ->  `Topology.removeCommandListener()` 
* `JTopology.removeCommand()` ->  `Topology.removeCommand()` 
* `JTopology.removeAllCommands()` ->  `Topology.removeAllCommands()` 
* `JTopology.addCommand()` ->  `Topology.addCommand()` 
  
  A specific string (`Topology.COMMAND_SEPARATOR`) is provided to be used as separator. 
  It is simply ignored for command execution.

***New methods:***
* `Topology.getCommands()` has been created
 
  This methods computes the current list of commands. It merges the commands added via `Topology.addCommand()` 
  to a set of default commands (see Topology.DefaultCommands). 
  The default commands can be disabled using `Topology.disableDefaultCommands()`

* `Topology.disableDefaultCommands()` has been created

  Disables the default commands. They are enabled by default?
* `Topology.enableDefaultCommands()` has been created

  Enables the default commands. They are enabled by default?

* `Topology.executeCommand()` has been created
 
  This method tries to execute the provided command (as a default command) and then passes it to all `CommandListner`s. 

### Topology class modifications

**Removal in Topology:**
* deprecated `Topology.getJTopology()` has been removed

* `Topology.setLinkPainter()` has been removed (and replace by `add`/`remove`/`setDefault` accessors)


**New methods in Topology:**

* `Topology.setDefaultLinkPainter()` has been added
  
  It removes any previous `LinkPainter` and replace them by the provided one.

* `Topology.addLinkPainter()` has been added
  
  It adds the provided `LinkPainter` to the internal list of `LinkPainter`s.
  
* `Topology.removeLinkPainter()` has been added
  
  It removes the provided `LinkPainter` from the internal list of `LinkPainter`s.


**Topology methods renaming:**

* `Topology.setClockSpeed()` is now `Topology.setTimeUnit()`
* `Topology.getClockSpeed()` is now `Topology.getTimeUnit()`
  
### JConsole class removal

The `JConsole` has been removed:
  * `jbotsimx.JConsole` -> x


### DefaultScheduler/Scheduler modifications

The `Scheduler` interface has been removed:
  * `jbotsim.Scheduler` -> x

The `DefaultScheduler` class has been renamed:
  * `jbotsim.DefaultScheduler` -> `io.jbotsim.core.Scheduler`
  
### TopologyLayouts

`jbotsim-extras-common`/`io.jbotsim.gen.basic.TopologyLayouts`

The new `Layouts` class proposes static methods which apply specific layouts to existing Nodes in a Topology.

Amongst others, the `TopologyLayouts.autoscale()` static method automatically scales the provided Topology to fit its 
boundaries.

  
### TopologyGenerator

[[issue 40]][issue: #40]

The former `TopologyGenerator` static class, applying specific topologies to an existing `Topology` object, has been
modified:
* `jbotsimx.topology.TopologyGenerator` -> `jbotsim-extras-common`/`ui.jbotsim.gen.basic.TopologyGenerators`

* new methods in `ui.jbotsim.gen.basic.TopologyGenerators` now also provide the (known) model name of the Node type to 
be instantiated.

A `TopologyGenerator` interface has been created, along with a set of implementation.
These objects will add as many nodes as required to match the requirements.

The content of the new `ui.jbotsim.gen.basic.generators` package is listed there after:
*  `ui.jbotsim.gen.basic.generators.TopologyGenerator` creation

   This is the interface providing a simple `generate()` method.
   
*  `ui.jbotsim.gen.basic.generators.AbstractGenerator` creation

   This is a convenience abstract class used for actual implementations.
   
*  `ui.jbotsim.gen.basic.generators.GridGenerator` creation

   This implementation generates a grid topology.
   
*  `ui.jbotsim.gen.basic.generators.KNGenerator` creation

   This implementation generates a complete graph based on a ring topology.
   
*  `ui.jbotsim.gen.basic.generators.LineGenerator` creation

   This implementation generates a line topology.
   
*  `ui.jbotsim.gen.basic.generators.RandomLocationsGenerator` creation

   This implementation generates a randomly distributed set of nodes.
   
*  `ui.jbotsim.gen.basic.generators.RingGenerator` creation

   This implementation generates a ring topology.

*  `ui.jbotsim.gen.basic.generators.TorusGenerator` creation

   This implementation generates a torus topology.

[issue: #40]: https://github.com/jbotsim/JBotSim/issues/40


### MovementListener interface modifications


**MovementListener method renaming:**

* `MovementListener.onMove()` is now `MovementListener.onMovement()`

## API CHANGES FROM 1.0-ALPHA TO 1.0-BETA

JBotSim is advancing towards the release of version 1.0.
We took the opportunity of the "beta" cycle to make a number of scheduled API 
changes.

The main changes removed AWT & SWING dependencies of non-UI classes  (in view
of enabling JBotSim on other platforms like Android). 
Most changes impact only the "imports" in your programs + other minor changes.

Please contact us (contact@jbotsim.io)  would you have some trouble migrating
your code.

Main changes:

- (import) awt.Color -> jbotsim.Color

- (import) awt.Point2D -> jbotsim.Point

- (import) jbotsim.Point3D -> jbotsim.Point

- (import) jbotsim.ui -> jbotsimx.ui

- (Topology) getDimension -> deleted (please use getWidth + getHeight directly)

- (Topology) fromString, toString, fromFile, toFile -> moved to jbotsimx.format

- (Node) basicColors -> moved to the new jbotsim.Color

- (internals) DefaultClock is now a timerless thread looping without delay.
  You can ignore this if you are using the classical JViewer or JTopology.

- (Topology) start() must be called explicitly once the simulation is set up

The last change is certainly the most critical, as the previous behavior
of the topology was to start automatically (by default). Now, it does not start
automatically, and start() must be called at the end of the simulation set up.

Note: If you want to start in a paused stated, but still want your nodes to 
have executed their onStart() method before that, you may simply call start() 
on your topology immediately followed by a call to pause().
(Eventually we will provide an atomic call to this effect.)

[Unreleased]: https://github.com/acasteigts/JBotSim/compare/v1.0.0...develop
[1.0.0]: https://github.com/acasteigts/JBotSim/compare/v1.0.0-beta03...v1.0.0