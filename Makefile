all : DTSplitter.class ColoredPoint.class Color.class DemoManager.class \
      ScreenState.class

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

clean :
	rm *.class


