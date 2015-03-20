default:
	mkdir -p classes_tmp
	find . -name "*.java" | xargs javac -d classes_tmp
	cp jbotsim/ui/circle.png classes_tmp/jbotsim/ui/
	jar cf jbotsim.jar -C classes_tmp/ jbotsim/ -C classes_tmp/ jbotsimx/
	rm -fr classes_tmp

