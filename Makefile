all : DTSplitter.class ColoredPoint.class Color.class DemoManager.class \
      ScreenState.class AlgorithmDemo.class

DTSplitter.class : DTSplitter.java
	javac DTSplitter.java

Color.class : Color.java
	javac Color.java

ColoredPoint.class : ColoredPoint.java
	javac ColoredPoint.java

DemoManager.class : DemoManager.java
	javac DemoManager.java

ScreenState.class : ScreenState.java
	javac ScreenState.java

AlgorithmDemo.class : AlgorithmDemo.java
	javac AlgorithmDemo.java

clean :
	rm *.class


