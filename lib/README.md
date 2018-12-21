# Libraries submodule

This module contains the actual JBotSim code. It is responsible for building and publishing every unitary *jars*.
Before adding code/features, please consider the following project structure in order to put it in the right spot.

## Project structure
The submodules are listed below:

* [`jbotsim-all`](./jbotsim-all/README.md): phony target which publishes a minimal artifact with dependency declarations 
for all artifacts (historically Swing) JBotSim.
  There is no code in this convenience submodule. 
  It should be updated (dependencies and documentation) when the structure of the project changes.
* [`jbotsim-common`](./jbotsim-common/README.md): phony target which publishes a minimal artifact with dependency 
declarations for *common* artifacts of JBotSim.
  There is no code in this convenience submodule. 
  It should be updated (dependencies and documentation) when the structure of the project changes.
* [`jbotsim-core`](./jbotsim-core/README.md): generates/publish a jar containing the core of JBotSim.
Please only add here features that are considered central to JBotSim, avoiding adding external or platform-dependent
dependencies.
* [`jbotsim-serialization-common`](./jbotsim-serialization-common/README.md): generates/publish a jar containing common classes for 
topologies import/export.
  Please only add here classes which are generically involved in formatting, avoiding adding external or 
platform-dependent dependencies.
* [`jbotsimx-extras-common`](./jbotsimx-extras-common/README.md): generates/publish a jar containing common classes for
 several "extra" features (e.g.: Connectivity testing).
  Here goes everything that does not (yet?) require to have its own submodule and that is not central to JBotSim.
  Please be careful not avoiding adding external or platform-dependent dependencies in this submodule.
* [`jbotsimx-extras-swing`](./jbotsimx-extras-swing/README.md): generates/publish a jar containing AWT/SWING-specific 
classes for several "extra" features (e.g.: Connectivity testing).
  This submodule is the AWT/SWING extension of the `jbotsimx-extras-common` submodule.
  For other platforms, you might need to create a new `jbotsimx-extras-xxx` module.
* [`jbotsimx-ui-common`](./jbotsimx-ui-common/README.md): generates/publish a jar containing common classes for UI 
manipulation.
  Please only add here classes which are generically involved in UI manipulation by JBotSim, avoiding adding external or 
platform-dependent dependencies.
* [`jbotsimx-ui-swing`](./jbotsimx-ui-swing/README.md): generates/publish a jar containing AWT/SWING-specific classes 
for UI manipulation.
  This submodule is the AWT/SWING extension of the `jbotsimx-ui-common` submodule.
  You can add here classes which are involved in UI manipulation by JBotSIM, with potential AWT/SWING dependencies.
  For other platforms, you might need to create a new `jbotsim-ui-xxx` module.
* [`jbotsim-adapter-jgrapht`](./jbotsim-adapter-jgrapht/README.md): generates/publish a jar containing an adapter of 
the JGraphT library for JBotSim.
  Since it implies a dependency to an external library, this submodule is separated from the others and should only be
  pulled when needed.
  You can add here classes used as a bridge between JBotSim and JGraphT.

