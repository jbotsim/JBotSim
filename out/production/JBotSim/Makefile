# Makefile for jbotsim
# make -> build jbotsim.jar
# make release -> build jbotsim-<tag>.jar where <tag> is derived from git tags


# Setting path of java install
# Override with make JAVA_HOME=/your/path/to/java/
# Or by setting JAVA_HOME=/your/path/to/java in your environment
ifndef JAVA_HOME
  JAVA_HOME=/usr
endif
JC=${JAVA_HOME}/bin/javac
JAR=${JAVA_HOME}/bin/jar


# jar filename
JARFILE=jbotsim.jar
build:
	mkdir -p classes_tmp
	find . -name "*.java" | xargs	${JC} ${JFLAGS} -d classes_tmp 
	cp jbotsimx/ui/circle.png classes_tmp/jbotsimx/ui/
	cp jbotsimx/format/xml/*.xsd classes_tmp/jbotsimx/format/xml/
	${JAR} cf ${JARFILE} -C classes_tmp/ jbotsim/ -C classes_tmp/ jbotsimx/
	rm -fr classes_tmp

# get git tag
TAG=`git describe --dirty --abbrev --always --tags`
release: 
	make build JARFILE=jbotsim-${TAG}.jar

