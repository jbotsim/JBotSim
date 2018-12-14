# Fat jars generation submodule

This module generates *fat jars* based on several *jars* published in the `lib` submodule. 

Please feel free to propose new submodules if a *fat jar* is really needed and not already provided. 
See project structure for existing ones.

## Project structure
The submodules are listed below:
* [`jbotsim-full`](./jbotsim-full/README.md): generates a *fat jar* containing all JBotSim *jars*.
It contains all classes, even the AWT/SWING specific ones, just as the legacy *jar* used to do.
* [`jbotsim-common`](./jbotsim-common/README.md): generates a *fat jar* containing all non-specific classes from JBotSim.
This *jar* contains as many *common* classes as possible. 
