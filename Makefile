JC = javac
JFLAGS = -source 1.5 -target 1.5

all : DTSplitter.class ColoredPoint.class Color.class DemoManager.class \
      ScreenState.class AlgorithmDemo.class RGBA.class Triangulation.class \
      Matrix.class Timer.class

DTSplitter.class : DTSplitter.java
	$(JC) $(JFLAGS) DTSplitter.java

Color.class : Color.java
	$(JC) $(JFLAGS) Color.java

ColoredPoint.class : ColoredPoint.java
	$(JC) $(JFLAGS) ColoredPoint.java

DemoManager.class : DemoManager.java
	$(JC) $(JFLAGS) DemoManager.java

ScreenState.class : ScreenState.java
	$(JC) $(JFLAGS) ScreenState.java

AlgorithmDemo.class : AlgorithmDemo.java
	$(JC) $(JFLAGS) AlgorithmDemo.java

RGBA.class : RGBA.java
	$(JC) $(JFLAGS) RGBA.java

Triangulation.class : Triangulation.java
	$(JC) $(JFLAGS) Triangulation.java

Matrix.class : Matrix.java
	$(JC) $(JFLAGS) Matrix.java

Timer.class : Timer.java
	$(JC) $(JFLAGS) Timer.java

clean :
	rm *.class


