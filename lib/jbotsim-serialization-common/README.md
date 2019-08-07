# JBotSim Format Common submodule

TODO: add specifics


## External dependencies

Although being a *common* module, this module declares some external dependencies.
They are listed here, should you ever encounter dependency conflicts with other libraries. 

In order to implement the `DotTopologySerializer`, we use ANTLR 4. As a consequence there exists a dependency
to [ANTLR runtime version 4.7](https://www.antlr.org/). 
This dependency is only required if you use the `DotTopologySerializer` or something that might use it 
(for now, the `JViewer`).