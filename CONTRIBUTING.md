
# Contributing to JBotSim

You are more then welcome to contribute to JBotSim!

We try here to provide the helpful information for you to understand the project and how to contribute to it.
If something is missing, please do not hesitate to contact us at <contact@jbotsim.io>.

## To the code!

It is most likely that you want to contribute to the actual source code. 
In the following sections, valuable information on how the JBotSim project works are listed. 
Please have a look. :-)  

Anyway, if you directly want to reach the source code related documentation, it is in 
[`lib/CONTRIBUTING.md`](lib/CONTRIBUTING.md).

## License

As mentioned [here](README.md#license), JBotSim is published under LGPL 3.0 and later.
Should your code proposal be accepted, it should fall under the same license.

## Environment

After cloning it, the project should build properly, provided that you match the following small requirements:
* an Internet connection;
* [JDK 8 or higher](#jdk-18-or-higher).

The following sections clarify different points on why things are required or not. 

### JDK 1.8 or higher

To be able to simply build the project you need to have a Java Development Kit version 1.8 or higher installed.

For collaborators [allowed to publish](#publication), other restrictions apply. This is covered 
[here](lib/CONTRIBUTING.md#jdk-version). 

### IntelliJ IDEA configuration

Although it is optional [for building](#gradle-build-system), the JBotSim project is intended to be opened with 
[*IntelliJ IDEA*](https://www.jetbrains.com/idea/), as a *Gradle* project.

The steps to open the project vary depending on whether you clone the [Git](#git) project manually or directly with 
IntelliJ IDEA.

#### Open manually cloned project

Once you have manually cloned your project:
* `File`>`Open`
* In the `Open File or Project` popup: 
  * Select the directory in which you have cloned the project. 
  * Hit `OK`.

You should now be able to click on the [Import Gradle Project](#importing-gradle-project) toast, on the bottom right.

#### Cloning project using IntelliJ IDEA

* `File`>`New`>`Project from Version Control`>`Git`
* In the `Clone Repository` popup, provide:
  * `URL`: `https://github.com/jbotsim/JBotSim.git` .
  * `Directory`: the directory in which the project should be cloned. In most cases, you will want to leave it as is.
  * Hit `Clone`.

After the clone, you should be able to click on the [Import Gradle Project](#importing-gradle-project) toast,
on the bottom right.

#### Importing Gradle project

In the `Import Module from Gradle` popup you can generally leave all configurations as is and hit `OK`.
Some details about this popup:
* If _Intellij IDEA_ complains about a missing SDK, please make sure to provide a proper Java Development Kit
([preferably 8](#jdk-18-or-higher)) path in the `Gradle Jvm` field.
* You might want to tick `Use auto-import`.
* Please make sure to chose the `Use gradle default wrapper` option (See [here](#gradle-build-system)). 

Once you have hit `OK`, IntelliJ IDEA will take a bit of time to automatically configure everything (_i.e._ create 
configuration files, retrieve required dependencies, etc.).


Your _IntelliJ IDEA_ project is now configured!

### *Gradle* build system

Our build system is based on *Gradle*. 
Please use the gradle wrapper (*i.e.* `./gradlew` on UNIX systems).
Its advantages are that:
* you don't need to install *gradle* on your machine;
* it guarantees are sure that everyone (including the [CI](#continuous-integration)) uses the same *gradle* version. 

### Dependency management

All dependencies are declared in `build.gradle` files.
* Local dependencies: JBotSim is composed of [several subprojects](README.md#project-structure) which depend on each 
other. These subprojects are all present in this repository.  
* Remote dependencies: JBotSim's few external dependencies are all automatically retrieved via *Maven*. 

Nothing to install here!

## Publication

The artifacts publication is done on Maven Central. Only Arnaud or Rémi can publish on it.

Only the [lib](lib) subproject and its subprojects can actually publish something.
Thus, the publication process is explained in [`lib/CONTRIBUTING.md`](lib/CONTRIBUTING.md#publication).

## Issues

JBotSim's issues are currently tracked on [GitHub][github-jbotsim].

## Git
> The project is clonable with this url: https://github.com/jbotsim/JBotSim.git

JBotSim's source code is currently hosted on [GitHub][github-jbotsim].
If, at some point, you need specific permission to access the repository, please send us a mail at <contact@jbotsim.io>.

Some general rules to keep things in a decent order:
* The `master` branch holds the latest production code. 
Only Arnaud and Rémi can push on it.
* The `develop` branch holds the features which should be available in the next release. 
Only Arnaud and Rémi can push on it.

### Branch naming

We try to enforce the following naming conventions for branches:
* branches names are lower case;
* branches names contain dashes `-` as spaces;
* *feature* branches associated with a tracking number follow the pattern `feature/<tracking-number>-<branch-name>`.
 
  Example: `feature/26-icons-as-module` 
* *fixes* branches associated with a tracking number follow the pattern `fix/<tracking-number>-<branch-name>`.
 
  Example: `fix/22-properties-class-refacto` 

* *release* branches follow the pattern `release/<x>-<y>-<z>` (See [here](#release-cycle)).
 
  Example: `release/1-0-0`
  
### Commit messages 

We try to enforce the following pattern for commit messages:

```
<prefix>: #<tracking number> - <commit description>
```

Where:
* `<prefix>`: the prefix can be one of the following:
  * `New`: you added something that was not there previously;
  * `Chg`: you modified something that existed;
  * `Fix`: you fixed something that was broken or buggy.
* `#<tracking number> - `: the tracking number should be provided if present;
* `<commit description>`: a short description of what the commit does.

Here are some examples:
* `Fix: #31 - remove build artefact from the index`
* `Fix: remove typo in README.md`
* `New: #12 - add time travel feature`
* `Chg: #13 - increase time travel range`

### Release cycle

Here is what happens during a release:
1. The `release/x-y-z` release branch from `develop` by the release manager when it contains all features for next 
release (*e.g.* `release/1-0-0`).
   * The `x-y-z` version number is decided upon creation of the release branch. 
2. All tests, debugging, etc. are done on branch `release/x-y-z`.
   * This allows for new features to be merged into `develop` without interfering with the release.
3. Branch `release/x-y-z` is merged into `master` after version `x-y-z` is officially published on Maven Central.
4. The merge commit on the `master` branch is tagged `vx.y.z` (*e.g.* `v1.0.0`).
5. After release, the new content of `master` is merged into `develop`.
   * This allows for developers on `develop` to benefit from fixes made during the release.

## Pull / Merge requests

Pull requests (forked contributions) and merge requests (for collaborators having access to the repository) are handled
by Arnaud and Rémi.

Before merging `branch1` into `branch2`, they will:
* check that the proposed feature/modification is relevant for the next release
* check that the `branch1` is properly rebased above `branch2`
* check that CI still passes
* check for code compliance with the existing project and rules (*e.g.* your code is in the proper package)

Before submitting a request, please make sure you pass obvious points from the list above. 

## Continuous Integration

JBotSim's official continuous integration platform is [Travis][travis-jbotsim].

The repository also contains an up-to-date `.gitlab-ci` for those who prefer pushing their work on a private GitLab 
repository.

## Copyright notice

Each file added to the JBotSim project should contain the copyright notice.
It has been configured so that _IntelliJ IDEA_ finds it and applies it automatically.

Should you need it, a copy is provided here.

```
Copyright 2008 - $today.year, Arnaud Casteigts and the JBotSim contributors <contact@jbotsim.io>


This file is part of JBotSim.

JBotSim is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

JBotSim is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public License
along with JBotSim.  If not, see <https://www.gnu.org/licenses/>.

```

Beware of the `$today.year` variable in this _Velocity_ template. 

[github-jbotsim]: https://github.com/jbotsim/JBotSim
[travis-jbotsim]: https://travis-ci.org/jbotsim/JBotSim
