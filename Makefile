default:
	mkdir -p classes_tmp
	/usr/lib/jvm/java-7-oracle/bin/javac -source 1.6 -d classes_tmp jbotsim/*.java jbotsim/event/*.java jbotsim/ui/*.java jbotsimx/*.java jbotsimx/tvg/*.java
	cp jbotsim/ui/circle.png classes_tmp/jbotsim/ui/
	/usr/lib/jvm/java-7-oracle/bin/jar cf jbotsim.jar -C classes_tmp/ jbotsim/ -C classes_tmp/ jbotsimx/
	rm -fr classes_tmp

