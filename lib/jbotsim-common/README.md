# JBotSim Common submodule

This module publishes a minimal artifact which declares all necessary dependencies to retrieve all *common* JBotSim
artifacts.

## Dependency usage

To use this *common* version, simply add the following line to your gradle configuration: 

```
dependencies{
    implementation ("io.jbotsim:jbotsim-common:<your-version>")
}
```


## Retrieved dependencies

It currently declares the following dependencies: 

```
    api project(':lib:jbotsim-serialization-common')
    api project(':lib:jbotsim-ui-common')
```

These result in the following dependency graph containing all *common* modules (including `core`):

```
+--- project :lib:jbotsim-serialization-common
|    +--- project :lib:jbotsim-core
|    \--- # external dependency
\--- project :lib:jbotsim-ui-common
     \--- project :lib:jbotsim-core

```