default:
	mkdir -p classes_tmp
	javac -source 1.5 -d classes_tmp jbotsim/*.java jbotsim/event/*.java jbotsim/ui/*.java jbotsimx/*.java jbotsimx/tvg/*.java
	cp jbotsim/ui/circle.png classes_tmp/jbotsim/ui/
	jar cf jbotsim.jar -C classes_tmp/ jbotsim/ -C classes_tmp/ jbotsimx/
	rm -fr classes_tmp

