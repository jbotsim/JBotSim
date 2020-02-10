
[![Build Status](https://travis-ci.org/jbotsim/JBotSim.svg?branch=master)][travis-jbotsim]
[![Maven Central](https://img.shields.io/badge/maven%20central-1.2.0-informational.svg)][mavencentral-jbotsim-latest]
[![License](https://img.shields.io/badge/license-LGPL%20&ge;%203.0-informational.svg)][lgpl3]
  

  
# The JBotSim Library

  * [Introduction](#introduction)
  * [Hello World example](#hello-world-example)
  * [Current version](#current-version)
  * [*Maven* dependency](#maven-dependency)
    + [Using Gradle](#using-gradle)
    + [Using Maven](#using-maven)
    + [Using IntelliJ IDEA](#using-intellij-idea)
  * [Project structure](#project-structure)
  * [Issue tracking](#issue-tracking)
  * [Contributing to JBotSim](#contributing-to-jbotsim)
  * [License](#license)
  * [Binaries signing](#binaries-signing)

## Introduction

JBotSim is a simulation library for distributed algorithms in dynamic
networks. The style of programming is mainly event-driven: your code
can react to various events (pulse of a clock,
appearance/disappearance of a link, arrival of a message, movement of
the node, etc.). Node movements can be controlled by program (e.g.
mobile robots or mobility models), or by means of mouse-based
interactions during the execution. Beyond its features, the main asset
of JBotSim is its simplicity of use.


## Hello World example

JBotSim's HelloWorld is pretty simple. We simply need to:
1. [declare the dependency](#declaring-the-dependency) to JBotSim,
2. provide the [HelloWorld.java class](#helloworld-class).

It is that simple!

### Declaring the dependency
> Here, we work with a IntelliJ IDEA Java project, fetching dependencies from Maven Central.
> Other configurations are detailed [here](apps/examples/CreateUserProject.md).


For this HelloWorld, we will simply retrieve the `jbotsim-all` artifact 
(See [Which artifact should I use?](lib/README.md#which-artifact-should-i-use) section to easily find out which 
artifact will best suit your project's needs):
  * Open the _Project Structure_ popup

    `File` > `Project Structure`
    
  * Open the "New Project Library" popup:
    * select `Libraries` in the left panel
    * click `+`
    * choose `From Maven ...`.
  
  * In the "Download Library from Maven Repository" popup:
    * provide the following dependence `io.jbotsim:jbotsim-all:1.2.0`
    * make sure to tick `JavaDocs`
    * Hit `OK`.
  * Confirm that you want to add it to your (only) module.

    And you are done! You can start to use [the example code](#helloworld-class).

Maven will take care of retrieving any required dependencies.

### HelloWorld class

The source code of this example is pretty straightforward. You can also download the file from 
[here](apps/examples/src/main/java/examples/basic/helloworld/HelloWorld.java).

```java
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class HelloWorld{
    public static void main(String[] args){
        Topology tp = new Topology();
        new JViewer (tp);
        tp.start();
    }
}
```

1. We simply create a `Topology`, which is the main object of JBotSim and thus contains the main information of the
simulation.
2. We pass it to a `JViewer`, which will display the simulation elements and allow the user to interact with it.
3. And eventually, we start the simulation (`tp.start();`). Since we use a `JViewer`, this step could also be done by 
the user by selecting *"Start execution"* in the `JViewer`'s contextual menu (right click anywhere).


> Please see the [*Examples*](http://jbotsim.io/?p=examples) section of [JBotSim's website](http://jbotsim.io) for more
examples.

## Current version

 
The latest public version is [`1.2.0`][mavencentral-jbotsim-latest]. 
Please see the [CHANGELOG.md](CHANGELOG.md) for previous versions modifications.


***Retrieving JBotSim***

There are two ways to depend on JBotSim's API:
* using [*Maven*](#maven-dependency)
  
  This is the preferred way to depend on JBotSim. It is flexible and convenient: you don't need to download anything 
  manually or bother about dependencies.
* using one of the [standalone jars](fats/README.md)

  This is the way JBotSim was originally published. 
  It is less flexible, but can still be used for existing/straightforward projects. 

## *Maven* dependency
> Although the example provided here should work in most cases, you might want to head to the 
  [Which artifact should I use?](lib/README.md#which-artifact-should-i-use) section for a finer grain explanation of 
  which artifact(s) can suit your needs. 
  
Using *Maven* is the preferred way to retrieve JBotSim (over standalone [fat jars](fats/README.md)).

### Using Gradle

If your build system uses *Gradle*, you will want to add something like this to your `build.gradle`:

```
dependencies {
    implementation "io.jbotsim:jbotsim-all:1.2.0"
}
```

### Using Maven


If your build system uses *Maven*, you will want to add something like this to your `.pom`:

```xml
<dependency>
  <groupId>io.jbotsim</groupId>
  <artifactId>jbotsim-all</artifactId>
  <version>1.2.0</version>
</dependency>
```

### Using IntelliJ IDEA

If you use a _Java Project_ in _IntelliJ IDEA_, add the following dependency (as explained [here](#declaring-the-dependency)):
 ```
 io.jbotsim:jbotsim-all:1.2.0
 ```


## Project structure

The JBotSim project is separated in three main modules.
Please follow the links to each of them for more information:
* [`apps`](./apps/README.md): contains some sample apps and mains using modules from `lib`. 
* [`lib`](./lib/README.md): contains submodules responsible for the generation and publication of unitary *jars* files 
on [Maven Central][mavencentral-jbotsim].
* [`fats`](./fats/README.md): contains submodules responsible for the generation of standalone *fat jars* by using 
existing published JBotSim jars (published by `lib`).


## Issue tracking

JBotSim's issues are currently tracked on [GitHub][github-jbotsim].
If you feel like something is missing or buggy, please feel free to create a ticket (a check on existing/former 
[tickets][github-jbotsim-issues] can't hurt! ;-).


## Contributing to JBotSim

The JBotSim project will gladly welcome help. If you consider contributing, please refer to 
[`CONTRIBUTING.md`](./CONTRIBUTING.md).

 
## License

(C) Copyright 2008-2020, by [Arnaud Casteigts and the JBotSim contributors](CONTRIBUTORS.md). All rights reserved.


JBotSim is published under license [LGPL 3.0 or later][lgpl3]. 


A copy of the license should be available in the product.
The repository also contains a copy of the [GPL](COPYING) and a copy of the [LGPL](COPYING.LESSER).
You can also access copies of the license at <https://www.gnu.org/licenses/>.

SPDX-License-Identifier: LGPL-3.0-or-later

## Binaries signing

JBotSim's artifacts are signed with JBotSim project's PGP key (`DFA48EDB1EDEBA6F`) displaying 
"*Arnaud Casteigts (JBotSim project) \<contact@jbotsim.io\>*".

Should you ever need to verify that your binaries actually come from us, you can retrieve the public key with the 
following command:


```
gpg --keyserver hkp://pool.sks-keyservers.net --recv-keys DFA48EDB1EDEBA6F
```


[travis-jbotsim]: https://travis-ci.org/jbotsim/JBotSim
[github-jbotsim]: https://github.com/jbotsim/JBotSim
[mavencentral-jbotsim-latest]: https://search.maven.org/search?q=g:io.jbotsim%20AND%20v:1.2.0
[mavencentral-jbotsim]: https://search.maven.org/search?q=g:io.jbotsim
[github-jbotsim-issues]: https://github.com/jbotsim/JBotSim/issues
[lgpl3]: http://www.gnu.org/licenses/lgpl-3.0.html
