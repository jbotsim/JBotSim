# Changelog

This file lists modifications introduced by each version.

## [Unreleased]

###  ClockManager class modifications

**Bug fixes in ClockManager:**

[[issue 86]][issue: #86]

* The modification of the `ClockManager`'s internal time value (the one actually used by the `Topology`) now coincides 
  with the ticking of the `Clock`.
  
  Now, the clock is first incremented and then the `Scheduler` is called. 
  It was previously swapped, resulting in some user interactions happening before the call to the `Scheduler` (and thus 
  the `MessageEngine` and all `Node.onClock()`).
  Everything is now properly synchronized.

[issue: #86]: https://github.com/jbotsim/JBotSim/issues/86

### New icon in the jbotsim-icons module

`jbotsim-icons`/`io.jbotsim.ui.icons`

* A fully transparent new icon has been added [[issue 84]][issue: #84] 
  * `jbotsim-icons`/`io/jbotsim/ui/icons/transparent.png` 

  As usual, it's path can be accessed using the corresponding constant `Icons.TRANSPARENT`.

[issue: #84]: https://github.com/jbotsim/JBotSim/issues/84

###  Topology class modifications

**Bug fixes in Topology:**

* `Topology.restart()` and `Topology.clearMessages()` now properly remove delayed messages [[issue 83]][issue: #83]
 
  This is delegated to the new `MessageEngine.reset()` method. 

[issue: #83]: https://github.com/jbotsim/JBotSim/issues/83

###  MessageEngine class modification

**Symbol modifications in MessageEngine:**

* The `MessageEngine.reset()` method has been added  [[issue 83]][issue: #83]

  This method properly discards any message handled by the MessageEngine.

###  TikzTopologySerializer class modifications

[[issue 80]][issue: #80]

**Behavior modifications in TikzTopologySerializer:**

* Directed links are now supported
 
  Directed links are properly exported (using `"->"` option).
  This supposes that `Topology.getOrientation()` returns `DIRECTED`. 
* TikZ colors are now better supported
* The previous *scale* parameter is now a proper scale factor.

  Its previous default value was `50`. It is now `1/50.`, resulting in the same default value.  

**Symbol modifications in TikzTopologySerializer:**

* The `TikzTopologySerializer.DEFAULT_EOL` constant has been added

  This constant sets the value of the default End-Of-Line delimiter: `"\n"`.

* The `TikzTopologySerializer.DEFAULT_SCALE_FACTOR` constant has been added

  This constant sets the default scale factor that must be applied to the distance between nodes: `1/50`.

* A new constructor `TikzTopologySerializer(String, double)` has been added

  This constructor allows to specify custom values for both the *EOL* and the *scale factor*.

* `exportTopology(Topology topology, double scaleFactor)` is now `protected`

  It was previously `public`. You can achieve the same behavior by using the new constructor.
   
[issue: #80]: https://github.com/jbotsim/JBotSim/issues/80

###  JNode class modifications

[[issue 77]][issue: #77]

**Bug fix in JNode:**

* The wired `Link` created between two nodes using the Swing UI (start a right click on the first and drag to the second 
before releasing the button) now takes `Topology.getOrientation()` into account instead of always creating undirected 
links.
 
[issue: #77]: https://github.com/jbotsim/JBotSim/issues/77

##  [1.1.1] - 2019/09/26

###  Topology class modifications

[[issue 74]][issue: #74]

In order to better match the behavior introduced around `Topology.getOrientation()` in [[issue 49]][issue: #49], the
following modifications have been applied:
* `Topology.addConnectivityListener(ConnectivityListener, Orientation)` and 
`Topology.removeConnectivityListener(ConnectivityListener, Orientation)` have been added 
* `Topology.addConnectivityListener(ConnectivityListener, boolean)` and 
`Topology.removeConnectivityListener(ConnectivityListener, boolean)` have been marked **deprecated**

  Please use `Topology.addConnectivityListener(ConnectivityListener, Orientation)` and
   `Topology.removeConnectivityListener(ConnectivityListener, Orientation)` instead.
* `Topology.addConnectivityListener(ConnectivityListener)` and 
`Topology.removeConnectivityListener(ConnectivityListener)`'s behaviors are changed

  They now takes `Topology.getOrientation()` into account when registering/unregistering a listener.
  Previously, it always worked on undirected events.

[issue: #74]: https://github.com/jbotsim/JBotSim/issues/74

###  PlainTopologySerializer class modifications

**Bug fix in PlainTopologySerializer:**

* `PlainTopologySerializer.importFromString()` does not clear the `Topology` anymore [[issue 72]][issue: #72]

[issue: #72]: https://github.com/jbotsim/JBotSim/issues/72

### New icon in the jbotsim-icons module

`jbotsim-icons`/`io.jbotsim.ui.icons`

* A cross-shaped new icon has been added [[issue 73]][issue: #73] 
  * `jbotsim-icons`/`io/jbotsim/ui/icons/plus.png` 

  As usual, it's path can be accessed using the corresponding constant `Icons.PLUS`.

[issue: #73]: https://github.com/jbotsim/JBotSim/issues/73


## [1.1.0] - 2019/09/10

### Topology, Node and Link classes modifications

**Links handling clarification:**

[[issue 49]][issue: #49]

* The `Link.Type` enum has been **renamed** as `Link.Orientation`
* The `Link.DEFAULT_TYPE` enum has been **renamed** into `Link.DEFAULT_ORIENTATION`
* The `Topology` class is augmented with an instance variable `orientation` that contains the default orientation of 
the topology

  Accessors/Actuators have been added as `Topology.setOrientation()` and `Topology.getOrientation()`.
* A new `Topology.isDirected()` helper method has been added
 
  It is a simple test of the value returned by `Topology.getOrientation()`.  
* Accessors to links `Topology.getLinks()`, `Topology.getLink()` and `Node.getLinks()` now return links whose type is 
determined with respect to `Topology.getOrientation()`
* Accessors to links based on a `Boolean` specification of the orientation i.e., `Topology.getLinks(boolean)`, 
`Topology.getLink(Node,Node,boolean)` and `Node.getLinks(boolean)` are marked **deprecated** and replaced by methods 
where the `Boolean` is replaced by a `Link.Orientation` parameter
* The method `Topology.hasDirectedLinks()` is marked as **deprecated**; `Topology.isDirected()` should be used instead
* The implementation of `Node.getCommonLinkWith()` and `Node.getNeighbors()` is changed to explicitly request undirected 
links
* A test suite has been added to check behaviors of `Topology.getLinks()` w.r.t. topology orientation

[issue: #49]: https://github.com/jbotsim/JBotSim/issues/49


**Bug fix in Link:**

* Fixed a `NullPointerException` when calling `Link.equals(null)` [[issue 65]][issue: #65]

[issue: #65]: https://github.com/jbotsim/JBotSim/issues/65

###  MessageEngine class modifications

[[issue 58]][issue: #58]

**Fix in MessageEngine:**

* A call to the now **deprecated** `MessageEngine.setSpeed()` (now `MessageEngine.setDelay()`) method does not duplicate
  messages delivery anymore

**Behavior modifications in MessageEngine:**

* The delay feature from the former `DelayMessageEngine` has finally been integrated in the `MessageEngine`

  Modifying the `MessageEngine`'s delay (`setDelay()`) will only affect messages sent during this round and the 
  following ones. Already queued messages retain their counters. 

* A message will now be dropped if the link between its sender and destination is broken, while the message is queued

  In order for a message to be delivered, a link from the sender to the destination must be present during each round,
  while the message is queued. If at some point the `MessageEngine` can't find such a link, the message will be dropped.
   
  Note that if `Message.retryMode` is set to `true`, the `MessageEngine` will try requeue it, with a new delay.

* Concurrent messages delivery is now consistent with insertion order 

  When several messages between a sender and a destination are supposed to be delivered during the same round (they 
  have possibly been queued during different rounds, but with different delays), they will now consistently be delivered
  according to their queuing order.
    

**New symbols in MessageEngine:**

* The `MessageEngine.setDelay(int)` and `MessageEngine.getDelay()` have been created

  These methods allow you to control the delay (in rounds) applied before actually trying to deliver a message.

* The `MessageEngine.DELAY_INSTANT` constant has been added

  This constant sets the value of the delay use for the quickest possible delivery.

* The `MessageEngine.DEFAULT_DELAY` constant has been added

  This constant sets the delay's default value. It actually matches `MessageEngine.DELAY_INSTANT`.

* The explicit constructors `MessageEngine(Topology)` and `MessageEngine(Topology, int)` have been created

  * It is thus now mandatory to provide at least a `Topology` when creating a `MessageEngine`.
  * `MessageEngine.setTopology(Topology)` has however been kept.
  * No default constructor is available. Please check that your code do not use it (reflection calls included).

**Deprecations in MessageEngine:**

* The `MessageEngine.setSpeed(int)` method has been deprecated

  Please use `MessageEngine.setDelay(int)` instead.

###  DelayMessageEngine/RandomMessageEngine class modifications

[[issue 58]][issue: #58]

Since the base `MessageEngine` now handles the _delay_ feature:

* `DelayMessageEngine` has been renamed to `RandomMessageEngine`
   
   `jbotsim-extras-common`/`io.jbotsim.contrib.messaging.DelayMessageEngine` -> `io.jbotsim.contrib.messaging.RandomMessageEngine`
   
* The `RandomMessageEngine` inherits from all delay-related improvements in `MessageEngine`:
  
  * The delay applied to message delivery is now modifiable during the object's lifecycle
  * Concurrent messages delivery is now consistent with insertion order
  * A broken link leads to message dropping
  * `Message.retryMode` is now taken into account
    
###  AsyncMessageEngine class modifications

[[issue 58]][issue: #58]

**Behavior modifications in AsyncMessageEngine:**

* The _average_ feature is now better handled and documented

  In non-FIFO cases, the delay is drawn randomly according to an exponential law with average value 
    `AsyncMessageEngine.getAverageDuration()`.
    
  In FIFO cases, the theoretical random delay is drawn the same way as for non-FIFO cases, but also taking care that the 
  new message won't be delivered before a pre-existing one.


**New symbols in AsyncMessageEngine:**

* The `AsyncMessageEngine.setAverageDuration(int)` and `AsyncMessageEngine.getAverageDuration()` have been created

  These methods allow you to control the delay (in rounds) applied before actually trying to deliver a message.

* The `AsyncMessageEngine.DEFAULT_TYPE` constant has been added

  This constant sets the default queue type value: `AsyncMessageEngine.Type.FIFO`.

* The `AsyncMessageEngine.DEFAULT_AVERAGE_DURATION` constant has been added

  This constant sets the desired average delivery duration's default value: `10` rounds.
  
* The explicit constructors `AsyncMessageEngine(Topology, double, Type)` and `AsyncMessageEngine(Topology)` have been created

**Removals in AsyncMessageEngine:**

* The explicit constructor `AsyncMessageEngine(double, Type)` has been removed

  Please use  `AsyncMessageEngine(Topology, double, Type)` instead.

[issue: #58]: https://github.com/jbotsim/JBotSim/issues/58

###  JViewer class modifications

**Bug fix in JViewer:**

* Fixed a truncation issue when importing a Topology via the *Load Topology* command [[issue 60]][issue: #60]

[issue: #60]: https://github.com/jbotsim/JBotSim/issues/60

**Behavior modifications in JViewer:**

* The `JViewer` now also detects `.gv` and `.xdot` files as to be handled with the `DotTopologySerializer` [[issue 48]][issue: #48]
* The `JViewer` now detects `.d6` and `.g6` files as to be handled with the new `Graph6TopologySerializer` [[issue 28]][issue: #28]
 
###  DotTopologySerializer class modifications

[[issue 48]][issue: #48]

**Behavior modifications in DotTopologySerializer:**

* `DotTopologySerializer.exportToString(Topology)` is now implemented (`null` was knowingly returned in the previous 
  version)

  Amongst other implementation points:
  * Node positions are output with the `pos = "x, y!"` syntax
  * The Y-coordinates are exported flipped, to be consistent with `DotTopologySerializer.importFromString()`
  * The exportation is done without respect to the `scale` member

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

### DotTopologySerializer new parser implementation

[[issue 55]][issue: #55]

* The replacement of the parser removes the dependency to [com.paypal.digraph.digraph-parser](https://github.com/paypal/digraph-parser)

* The new parser has been written from the ANTLR grammar used by digraph-parser package 
(actually it is the grammar given in "The definitive ANTLR 4 Reference" book by T. Parr)
 
* ANTLR runtime classes are still required to run the parser

* The parser builds a graph structure (`DotGraph`, `DotNode`, `DotEdge`) that is translated into a topology

* The new parser permits to distinguish directed and undirected graphs

[issue: #55]: https://github.com/jbotsim/JBotSim/issues/55

### Graph6 file format support 

[[issue 28]][issue: #28]

* A new serializer `Graph6TopologySerializer` is added to `jbotsim-serialization-common`/`io.jbotsim.io.format.graph6`.

* Extensions `.d6` and `.g6` have been registered in client classes of `TopologySerializerFilenameMatcher`: 
    * `examples.tools.JBotSimConvert` 
    * `examples.tools.JBotSimViewer`
    * `io.jbotsim.ui.JViewer` 

[issue: #28]: https://github.com/jbotsim/JBotSim/issues/28

### TopologySerializerFilenameMatcher

* An helper method has been added in `TopologySerializerFilenameMatcher` that associates a set of filename extensions 
  with a single `TopologySerializer`

###  Color class modifications

* Some internal fixes have been provided, and unit tests added, to better match AWT's implementation regarding 
  constructors and the alpha component
  
* `Color.getColor()` variants now better match their AWT counter parts

  JBotSim's implementations used to directly consider the provided String as a RGB value, instead of trying to get the 
  said value from a system property.  

* General documentation effort

### JBackgroundPainter, JLinkPainter, JNodePainter classes modifications

In order to ease behavior modifications by inheritance, `JBackgroundPainter`, `JLinkPainter` and `JNodePainter` have 
been modified so that they all expose a `protected` variant of:
* `setColor()`: this method is called by the object when it needs to set the AWT `Color` of the element
* `setStroke()`: this method is called by the object when it needs to set the AWT `Stroke` used to draw the element
* `setRenderingHints()`: this method is called by the object when it needs to set the AWT `RenderingHints` used to draw 
the element

Overriding these methods should be fairly straightforward.

The behaviors associated to each class stay unchanged.

###  JDirectedLinkPainter creation

* `JDirectedLinkPainter` is added to `jbotsim-extras-swing`/`io.jbotsim.ui.painting` [[issue 71]][issue: #71]
  
  It provides arrow-tips to directed links. Its implementation uses the reusable  
  `jbotsim-extras-common`/`io.jbotsim.ui.painting.ArrowTipPathGenerator` class.

[issue: #71]: https://github.com/jbotsim/JBotSim/issues/71

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

### New icon in the jbotsim-icons module

`jbotsim-icons`/`io.jbotsim.ui.icons`

* A new icon suitable for servers/mainframes has been added [[issue 59]][issue: #59] 
  * `jbotsim-icons`/`io/jbotsim/ui/icons/server.png` 

  As usual, it's path can be accessed using the corresponding constant `Icons.SERVER`. 


[issue: #59]: https://github.com/jbotsim/JBotSim/issues/59


### Javadoc modifications 

* Link JSE8 javadoc from javadoc [[issue 62]][issue: #62]
* Prevent some errors in javadoc compilation by specifying UTF-8 encoding for javac and javadoc [[issue 66]][issue: #66]
* Also remove some invalid tags (i.e. \<tt\>) from comments [[issue 66]][issue: #66]

[issue: #62]: https://github.com/jbotsim/JBotSim/issues/62
[issue: #66]: https://github.com/jbotsim/JBotSim/issues/66

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

[Unreleased]: https://github.com/jbotsim/JBotSim/compare/v1.1.1...develop
[1.1.1]: https://github.com/jbotsim/JBotSim/compare/v1.1.0...v1.1.1
[1.1.0]: https://github.com/jbotsim/JBotSim/compare/v1.0.0...v1.1.0
[1.0.0]: https://github.com/jbotsim/JBotSim/compare/v1.0.0-beta03...v1.0.0
