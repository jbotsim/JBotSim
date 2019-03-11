# JBotSim All submodule

This module publishes a minimal artifact which declares all necessary dependencies to retrieve all JBotSim
artifacts.
This corresponds to the former JBotSim standalone *jar*.

## Dependency usage

To use this *common* version, simply add the following line to your gradle configuration: 

```
dependencies{
    implementation ("io.jbotsim:jbotsim-all:<your-version>")
}
```

## Retrieved dependencies

It currently declares the following dependencies: 

```
    api project(':lib:jbotsim-extras-swing')
```

These result in the following dependency graph containing all modules:
```
\--- project :lib:jbotsim-extras-swing
     +--- project :lib:jbotsim-ui-swing
     |    +--- project :lib:jbotsim-core
     |    +--- project :lib:jbotsim-serialization-common
     |    |    +--- project :lib:jbotsim-core
     |    |    \--- # external dependency
     |    +--- project :lib:jbotsim-ui-common
     |    |    \--- project :lib:jbotsim-core
     |    \--- project :lib:jbotsim-icons
     \--- project :lib:jbotsim-extras-common
          +--- project :lib:jbotsim-core
          \--- project :lib:jbotsim-serialization-common (*)

```


