# JBotSim Format Common submodule

TODO: add specifics


## External dependencies

Although being a *common* module, this module declares some external dependencies.
They are listed here, should you ever encounter dependency conflicts with other libraries. 

In order to implement the `DotTopologySerializer`, we currently use Paypal's 
`digraph-parser`.
The resulting dependencies are:
* [digraph-parser version 1.0](https://github.com/paypal/digraph-parser)
  * [ANTLR runtime version 4.2](https://www.antlr.org/)
  * [abego TreeLayout version 1.0.1](https://github.com/abego/treelayout)

These dependencies are only required if you use the `DotTopologySerializer` or something that might use it 
(for now, the `JViewer`).