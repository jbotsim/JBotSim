default:
	mkdir -p classes_tmp
	javac -source 1.5 -d classes_tmp jbotsim/core/*.java jbotsim/core/event/*.java jbotsim/engine/*.java jbotsim/engine/event/*.java jbotsim/gui/*.java jbotsim/gui/viewer/*.java jbotsimx/*.java
	cp jbotsim/gui/viewer/circle.png classes_tmp/jbotsim/gui/viewer/
	jar cf jbotsim.jar -C classes_tmp/ jbotsim/ -C classes_tmp/ jbotsimx/
	rm -fr classes_tmp

