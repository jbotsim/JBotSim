# Test classes submodule

This submodule is the result of the removal from `lib:jbotsim*` modules of their testing classes.
Indeed, those classes often required wider dependencies than the production *jar* actually needed. 

This is the place to add new *main* classes when adding new features to JBotSim.

## Dependency management

The module currently declare project dependencies on `:lib::lib:jbotsimx-ui-swing` and `:lib:jbotsimx-ui-swing`. 

Amongst other points, this means that:
* it currently uses all submodules;
* it does not use the *fat jar* or the *maven* dependency management.


***TODO: add specifics***