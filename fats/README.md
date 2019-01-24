# Fat jars submodule
> Please consider using [*Maven dependency management*](../README.md#versions) instead of *fat jars*. 

This module is responsible for the generation of *fat jars* based on several *jars* published in the `lib` submodule.
The *jars* generated here allow projects that do not yet use *Maven* dependency management to continue using JBotSim.
 

Currently the following *fat jars* exist:
* [`jbotsim-full`](TODO DOWNLOAD LINK): a *fat jar* containing all JBotSim *jars*.
  
  This *jar* all classes, even the AWT/SWING specific ones, just as the legacy *jar* used to do.
* [`jbotsim-common`](TODO DOWNLOAD LINK): a *fat jar* containing all non-specific classes from JBotSim.
  
  This *jar* contains as many *common* classes as possible. 

Please feel free to propose new submodules if a *fat jar* is really needed and not already provided. 
See [project structure](#project-structure) for existing ones.

 
## Project structure
The submodules are listed below:
* [`jbotsim-full`](./jbotsim-full/README.md): a *fat jar* containing all JBotSim *jars*.
* [`jbotsim-common`](./jbotsim-common/README.md): a *fat jar* containing all non-specific classes from JBotSim.

