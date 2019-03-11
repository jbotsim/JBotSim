# Contributing to JBotSim's fat jars generation submodule

Before adding code/features, please make sure you understand the *user* part of the module by reading the 
[README.md](README.md). 


## Project structure
The submodules are listed below:
* [`jbotsim-full`](./jbotsim-full/README.md): generates a *fat jar* containing all JBotSim *jars*.
It contains all classes, even the AWT/SWING specific ones, just as the legacy *jar* used to do.
* [`jbotsim-common`](./jbotsim-common/README.md): generates a *fat jar* containing all non-specific classes from JBotSim.
This *jar* contains as many *common* classes as possible. 
