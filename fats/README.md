# Fat jars submodule
> Please consider using [*Maven dependency management*](../README.md#versions) instead of *fat jars*. 

This module is responsible for the generation of *fat jars* based on several *jars* published in the `lib` submodule.
The *jars* generated here allow projects that do not yet use *Maven* dependency management to continue using JBotSim.
 

Currently the following *fat jars* is available:
* [`jbotsim-standalone`](https://github.com/jbotsim/JBotSim/releases/download/v1.2.0/jbotsim-standalone-1.2.0.jar): a 
*fat jar* containing all JBotSim *jars*.

  This *jar* all classes, even the AWT/SWING specific ones, just as the legacy *jar* used to do.

Please feel free to propose new submodules if a *fat jar* is really needed and not already provided. 
See [project structure](#project-structure) for existing ones.

 
## Project structure
The submodules are listed below:
* [`fat-jbotsim-full`](./fat-jbotsim-full/README.md): a *fat jar* containing all JBotSim *jars*.
* [`fat-jbotsim-common`](./fat-jbotsim-common/README.md): a *fat jar* containing all non-specific classes from JBotSim.

   Please note that the corresponding *fat jar* is currently not published. If you happen to need it, please 
   [contact us](mailto:contact@jbotsim.io).

