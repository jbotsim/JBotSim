# Libraries submodule

This module contains the actual JBotSim code. It is responsible for building and publishing every unitary *jars* on 
[Maven Central][maven-central-jbotsim].

In order to foster application size minimisation, modularity, third-party dependencies minimisation, and multi-platform
implementations, JBotSim is now split in several easy-to-use artifacts.  

This documentation will help you find out which dependencies you need. See:
* [Project structure](#project-structure) for a general overview of the modules and their dependencies;
* [Which artifact should I use?](#which-artifact-should-i-use) for a deeper understanding on what to use depending on 
your constraints and what each artifact can offer.

## Project structure
All subprojects in this project aim at publishing an artifact on 
[Maven Central][maven-central-jbotsim]. 
They are all listed below:

* [`jbotsim-all`](./jbotsim-all/README.md): phony target which publishes a minimal artifact with dependency declarations 
for all artifacts (historically Swing) JBotSim.
* [`jbotsim-common`](./jbotsim-common/README.md): phony target which publishes a minimal artifact with dependency 
declarations for *common* artifacts of JBotSim.
* [`jbotsim-core`](./jbotsim-core/README.md): generates/publish a jar containing the core of JBotSim.
This jar contains the JBotSim's main classes.
* [`jbotsim-serialization-common`](./jbotsim-serialization-common/README.md): generates/publish a jar containing common classes for 
topologies import/export.
* [`jbotsim-extras-common`](./jbotsim-extras-common/README.md): generates/publish a jar containing common classes for
 several "extra" features (e.g.: Connectivity testing).
* [`jbotsim-extras-swing`](./jbotsim-extras-swing/README.md): generates/publish a jar containing AWT/SWING-specific 
classes for several "extra" features (e.g.: Connectivity testing).
  This submodule is the AWT/SWING extension of the `jbotsim-extras-common` submodule.
* [`jbotsim-icons`](./jbotsim-icons/README.md): generates/publish a jar containing a set of icons usable with JBotSim.
* [`jbotsim-ui-common`](./jbotsim-ui-common/README.md): generates/publish a jar containing common classes for UI 
manipulation.
* [`jbotsim-ui-swing`](./jbotsim-ui-swing/README.md): generates/publish a jar containing AWT/SWING-specific classes 
for UI manipulation.
  This submodule is the AWT/SWING extension of the `jbotsim-ui-common` submodule.



## Which artifact should I use?
This section helps you figure out which artifact you should use. 
Each artifact is the result of the publication of one of the subprojects listed [previously](#project-structure) 
(brief exhaustive list). 

As of version `1.0.0`:
* `jbotsim-all` is meant to be the replacement of the former JBotSim *jar*. 

  If you try to migrate an existing project, you can definitely start here! 
  All the previous features will be available using this dependency. Please see
   the [changelog](../CHANGELOG.md) for migration.

  Amongst others, this artifact will also retrieve all AWT/Swing dependencies. 
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-all:1.0.0
    +--- io.jbotsim:jbotsim-extras-swing:1.0.0
    |    +--- io.jbotsim:jbotsim-ui-swing:1.0.0
    |    |    +--- io.jbotsim:jbotsim-core:1.0.0
    |    |    |    \--- io.jbotsim:jbotsim-serialization-common:1.0.0
    |    |    |         +--- io.jbotsim:jbotsim-core:1.0.0
    |    |    |         \--- org.antlr:antlr4-runtime:4.7.2
    |    |    +--- io.jbotsim:jbotsim-ui-common:1.0.0
    |    |         \--- io.jbotsim:jbotsim-core:1.0.0
    |    \--- io.jbotsim:jbotsim-extras-common:1.0.0
    |         +--- io.jbotsim:jbotsim-core:1.0.0
    |         \--- io.jbotsim:jbotsim-serialization-common:1.0.0
    \--- io.jbotsim:jbotsim-icons:1.0.0

    ```

  </p>
  </details>
  

* `jbotsim-common` is meant to retrieve commonly used non-platform-dependent, aka "*common*", artifacts 
  (for example, `jbotsim-extras-common` is omitted).

  Indeed, whenever possible (and meaningful), we try to:
  * regroup as much *common* code as possible in a *common* module (`jbotsim-xxx-common` artifact), and 
  * put platform-specific code in separate modules (`jbotsim-xxx-<specific>` artifact). 
    Such modules depend on the same *common* module. This provides an easier way of supporting new platforms, 
    and fixing the *common* code once for all platforms.
  
  If you want to provide your own UI and/or you don't want any dependencies to AWT/Swing, this artifact is a good 
  starting point.
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-common:1.0.0
    +--- io.jbotsim:jbotsim-serialization-common:1.0.0
    |    +--- io.jbotsim:jbotsim-core:1.0.0
    |    \--- org.antlr:antlr4-runtime:4.7.2
    +--- io.jbotsim:jbotsim-ui-common:1.0.0
    |    \--- io.jbotsim:jbotsim-core:1.0.0
    \--- io.jbotsim:jbotsim-icons:1.0.0
    
    ```

  </p>
  </details>
  
* `jbotsim-core` contains JBotSim's main classes.

  If you only want to retrieve the main classes, this artifact is for you!  
    
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-core:1.0.0
    ```
    
  </p>
  </details>
  
* `jbotsim-serialization-common` contains classes responsible for the import/export of a JBotSim's `Topology`s.

  You only need to import this artifact if you plan to perform `Topology` import/export.
  
  This "*common*" module currently does not have a platform-specific counterpart.
  
  As stated by the following dependency tree, there is a dependency to `org.antlr:antlr4` 
  (it is [induced](jbotsim-serialization-common/README.md#external-dependencies) by the `DotTopologySerializer`). 
  We consider that this dependency should be compatible with all platforms.

  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-serialization-common:1.0.0
    +--- io.jbotsim:jbotsim-core:1.0.0
    \--- org.antlr:antlr4-runtime:4.7.2
    ```
    
  </p>
  </details>

* `jbotsim-ui-common` specifies *common* objects and interfaces to use if you want to ease later multi-platform UI 
  development.
  
  `jbotsim-ui-swing` is its current platform-specific counterpart.
  
  You only need to explicitly declare a dependency to this artifact if you plan to provide your own UI. 
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-ui-common:1.0.0
    \--- io.jbotsim:jbotsim-core:1.0.0
    ```
    
  </p>
  </details>

* `jbotsim-ui-swing` contains JBotSim's historical AWT/Swing UI components (`JViewer` and `JTopology` are here!).

  This module is `jbotsim-ui-common`'s current platform-specific counterpart.
  
  You only need to explicitly declare a dependence to this artifact if you plan to provide your own UI. 
  
  As stated by the following dependency tree, there is an obvious dependency to `jbotsim-ui-common`.
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-ui-swing:1.0.0
    +--- io.jbotsim::jbotsim-core:1.0.0
    +--- io.jbotsim:jbotsim-serialization-common:1.0.0
    |    +--- io.jbotsim:jbotsim-core:1.0.0
    |    \--- org.antlr:antlr4-runtime:4.7.2
    \--- io.jbotsim::jbotsim-ui-common:1.0.0
         \--- io.jbotsim::jbotsim-core:1.0.0    
    ```
    
  </p>
  </details>


* `jbotsim-icons` contains a set of png icons usable by JBotSim's `Node` object.

  This artifact only contain resources, no Java code. 
  
  It comes with `jbotsim-ui-swing`, so you only need to explicitly declare a dependence to this artifact if you plan to 
  provide your own UI. 
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    +--- io.jbotsim:jbotsim-icons:1.0.0
    
    ```
    
  </p>
  </details>
    

* `jbotsim-extras-swing` and `jbotsim-extras-common` contain legacy or contribution classes that are not deemed 
  essential or currently worthy of being another artifact (one of the existing ones, or a new one).
  
  These classes are delivered as is. Please consider that some of these are less reviewed/tested than the rest of the 
  project. 
  Nevertheless, we will gladly help you with those, should they interest you.
  
  <details open><summary>Artifact dependencies</summary>
  <p>
  
    ```
    io.jbotsim:jbotsim-extras-swing:1.0.0
    +--- io.jbotsim:jbotsim-ui-swing:1.0.0
    |    +--- io.jbotsim:jbotsim-core:1.0.0
    |    +--- io.jbotsim:jbotsim-serialization-common:1.0.0
    |    |    +--- io.jbotsim:jbotsim-core:1.0.0
    |    |    \--- org.antlr:antlr4-runtime:4.7.2
    |    +--- io.jbotsim:jbotsim-ui-common:1.0.0
    |         \--- io.jbotsim:jbotsim-core:1.0.0
    \--- io.jbotsim:jbotsim-extras-common:1.0.0
         +--- io.jbotsim:jbotsim-core:1.0.0
         \--- io.jbotsim:jbotsim-serialization-common:1.0.0
    ```
    
  </p>
  </details>


[maven-central-jbotsim]: https://search.maven.org/search?q=g:io.jbotsim