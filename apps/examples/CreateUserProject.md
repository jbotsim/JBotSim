
# Creating a project using JBotSim
> The steps described here have been generated with a IntelliJ IDEA 2018.3.3 (Ultimate Edition) 
> (Build #IU-183.5153.38, built on January 9, 2019)


## Pre requisite
The guidance provided here suppose that you have a 
[usable](https://www.jetbrains.com/help/idea/install-and-set-up-product.html) *IntelliJ IDEA* instance.

## Introduction

In this documentation, we focus on creating a project that will:
* allow you to run JBotSim's [*Hello World*](./README.md#helloworld-class),
* make *IntelliJ IDEA*'s [Quick Documentation](https://www.jetbrains.com/help/idea/viewing-reference-information.html#inline-quick-documentation) 
on JBotSim symbols available.
 


The configuration process takes place in the following two steps.
* First, the [project creation](#project-creation). You will either be:
  * [creating a Java project](#creating-a-java-project) or
  * [creating a Gradle project](#creating-a-gradle-project).
* Then, the [configuration of your dependency to JBotSim](#adding-the-jbotsim-dependency):
  * using the [Maven central version](#using-the-maven-central-version) with
    * a [Java project](#java-project) or
    * a [Gradle project](#gradle-project);
  * using the [standalone jar version](#using-the-standalone-jar-version) with
    * a [Java project](#java-project-standalone) or
    * a [Gradle project](#gradle-project-standalone). 
  
## Project creation

### Creating a Java project 

* Starting the new project wizard:

  `File` > `New` > `Project...`
  
* Creating a Java project 

  * Select: `Java`
  * Make sure a *Project SDK* is selected (either 8 or 10) -- should be set by default
    * Nothing more to select. You can deselect anything selected in there.
  * Hit `Next`.
  
* Choosing a template
  
  * You can optionally select the `Java Hello World` template. 
  
    This will generate a `Main.java` file.
  * Hit `Next`.
  
* Choosing your project's name and directory
 
  * Provide a project name 
  * Provide a directory
  
    This field is automatically filled by *IntelliJ IDEA* depending on your project's name. In most cases, you will want
    to leave it as is.
  * Hit `Finish`.
  
    After some automatic configuration, the project should be created.

### Creating a Gradle project 

* Starting the new project wizard:

  `File` > `New` > `Project...`
  
* Creating a Java project 

  * Select: `Gradle`
  * Make sure a Project SDK is chosen (either 8 or 10) -- should be set by default
    * In "Additional Libraries and Frameworks", make sure "Java" is ticked.
  * Hit `Next`.
  
* *GroupId* and *ArtifactId* Screen
  * Fill in the fields:
    * `GroupId`:  your namespace (*e.g.* `com.example`)
    * `ArtifactId`: your application/library name (*e.g.* `jbotsim-app`) 
    * `Version`: leave as is (usually `1.0-SNAPSHOT`)
  * Hit `Next`.
  
* Gradle configuration screen
  * tick `Use auto-import`
  * make sure `Use default gradle wrapper` radio button is selected
  * you can leave the rest as is
  * Hit `Next`.


* Project name and location screen
  * these should already be filled in. Only modify those fields if you know you have to.
  * Hit `Finish`.
  
  After some automatic configuration, the project should be created.


## Adding the JBotSim dependency

### Using the Maven Central version

This method takes advantage of the fact that JBotSim has been published on *Maven Central*. 


Any JBotSim classes, source code and documentation will automatically be downloaded for you.

#### Java project

***Adding the JBotSim project jar as a dependency***
  * Open the Project Structure popup

    `File` > `Project Structure`
    
  * Open the "New Project Library" popup:
    * select `Libraries` in the left panel
    * click `+`
    * choose `From Maven ...`.
  
  * In the "Download Library from Maven Repository" popup:
    * provide the following dependency: `io.jbotsim:jbotsim-all:1.1.0`
    * make sure to tick `JavaDocs`
    * Hit `OK`.
  * Confirm that you want to add it to your (only) module.

    And you are done! You can copy paste the example.
    
***Documentation***
* if you have ticked "JavaDocs", the documentation should already be available.
* you can also provide the *javadoc-only* *jar*, containing only the javadoc, by using the same process described 
[here](#java-project-standalone). 
* providing the url to the online javadoc is still possible
    * open Project Structure 
    * in `Libraries`, select your maven dependency
    * click on `Specify Documentation URL`
    * provide the javadoc url
     
#### Gradle project

Simply add the following line to your `build.gradle`:

```
dependencies {
    implementation "io.jbotsim:jbotsim-all:1.1.0"
}
```


### Using the standalone jar version

> Using the standalone version should be reserved to already existing projects and offline usages.
> Please consider using the [Maven Central version](#using-the-maven-central-version).  


#### Java project standalone
 
***Adding the JBotSim project jar as a dependency***
  * Open the Project Structure popup

    `File` > `Project Structure`
    
  * Open the "New Project Library" popup:
    * select `Libraries` in the left panel
    * click `+`
    * choose `Java`.
  
  * In the popup, provide the JBotSim full *jar* (from the location you stored it)
  * Confirm that you want to add it to your (only) module.

    And you are done! You can copy paste the example.
    
***Documentation***
* if you have provided the *classes + javadoc* jar, intellij will automatically find the documentation.

  Be careful on not distributing your application with this version of the *jar*.

* if you have provided the *classes only* jar, can add documentation by: 
  * providing the *javadoc-only* *jar*, containing only the javadoc, by using the same process. 

  * providing the url to the online javadoc
    * open Project Structure 
    * select your .jar in modules
    * click on `Specify Documentation URL`
    * provide the javadoc url 

 
#### Gradle project standalone

>Creating a *gradle* project with the fat-jar makes sense if you are really afraid of (or not able to use) *maven* at 
>the time you create your project but consider using it later. 

>Please see [gradle-related *maven* configuration](#gradle-project). 


***Adding the JBotSim project jar as a dependency***
  * Put the jar in a directory inside your project
  
    *e.g.* directory `mylibs`
    
  * In the `build.gradle` file, add the following statement:
   
    ```
    dependencies {
        compile files('mylibs/jbotsim-full-1.1.0.jar')
    }
    ``` 
    
    And you are done! You can copy paste the example.
    
    
***Documentation***
* you can also provide the *javadoc-only* *jar*, containing only the javadoc, by using the same process described 
[here](#java-project-standalone). 
* providing the url to the online javadoc can work, but should be linked to another (possibly empty) lib, or your jvm.

