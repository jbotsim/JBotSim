# Contributing to JBotSim's publishable code base

Before adding code/features, please make sure you understand the *user* part of the module by reading the 
[README.md](README.md). 
The [Project Structure](#project-structure) section should help you figure the right spot where you should put your 
contribution.

## Project structure

Please refer to [README.md](README.md#project-structure) for a broad view of the submodules.

The submodules are listed below in more detail:

* Phony submodules:
  * [`jbotsim-all`](./jbotsim-all/README.md): 
    There is no code in this convenience submodule. 
    It should be updated (dependencies and documentation) when the structure of the project changes.
  * [`jbotsim-common`](./jbotsim-common/README.md):
    There is no code in this convenience submodule. 
    It should be updated (dependencies and documentation) when the structure of the project changes.
* Actual code submodules:
  * [`jbotsim-core`](./jbotsim-core/README.md): 
    Please only add here features that are considered central to JBotSim, avoiding adding external or platform-dependent
dependencies.
  * [`jbotsim-serialization-common`](./jbotsim-serialization-common/README.md):
    Please only add here classes which are generically involved in formatting, avoiding adding external or 
    platform-dependent dependencies.
  * [`jbotsim-extras-common`](./jbotsim-extras-common/README.md):
    Here goes everything that does not (yet?) require to have its own submodule and that is not central to JBotSim.
    Please be careful avoiding adding external or platform-dependent dependencies in this submodule.
  * [`jbotsim-extras-swing`](./jbotsim-extras-swing/README.md): 
    This submodule is the AWT/SWING extension of the `jbotsim-extras-common` submodule.
    For other platforms, you might need to create a new `jbotsim-extras-xxx` module.
  * [`jbotsim-icons`](./jbotsim-icons/README.md): 
    Please add here reusable png icons, with size around or below 10ko.
    This module is resources-oriented, so you probably should not add Java source code in it. 
  * [`jbotsim-ui-common`](./jbotsim-ui-common/README.md):
    Please only add here classes which are generically involved in UI manipulation by JBotSim, avoiding adding external 
    or platform-dependent dependencies.
  * [`jbotsim-ui-swing`](./jbotsim-ui-swing/README.md): 
    This submodule is the AWT/SWING extension of the `jbotsim-ui-common` submodule.
    You can add here classes which are involved in UI manipulation by JBotSIM, with potential AWT/SWING dependencies.
    For other platforms, you might need to create a new `jbotsim-ui-xxx` module.

 
## Publication

This section describes aspects related to the publishing of JBotSim's artifacts.

### Signing

Maven Central requires published artifacts to be signed (see [here](../README.md#binaries-signing)).  
However, we have configured the project so that signing be optional if you try to publish with a version number matching
`.*SNAPSHOT` (see the `version` variable in the root [build.gradle](../build.gradle)).
If you have [signatory credentials](https://docs.gradle.org/current/userguide/signing_plugin.html) configured, gradle
will try to use them in order to sign the artifacts.

### Repositories 

Several maven repositories are declared (in [lib/build.gradle](build.gradle)) with the following names:
* `Sonatype`: the official Maven Central repository.
* `LocalRepo`: a local repository created in a `repo` directory at the project's root.
  * It is useful for quick testing. It allows you to locally inspect the result of your publication process, and clean 
  it easily (just delete the `repo` directory).
* `NexusDocker`: a local Maven repository listening on `localhost:8081`.
  * This supposes that you have configured a maven server (by default, at `localhost:8081`).
  * Use case: publication of some semi-permanent artifacts for close collaboration.

### Publication commands

The following commands have been created to help with the publication process. 
They all publish all the artifacts for each subproject of `lib`. The sole difference is the repository on which they 
publish:
* `lib:publishToSonatypeRepository`: publishes on the `Sonatype` repository;
* `lib:publishToTestRepository`: publishes on the `LocalRepo` repository;
* `lib:publishToRemoteRepository`: publishes on a configurable repository. It can be any of the three above. By default,
 it is `NexusDocker`.

### JDK version 

The published binaries must be compiled specifically with a Java Development Kit version 1.8. 

