/*
 * DTSplitter
 *
 * This is the main class of the applet. It demonstrates an algorithm to split
 * a Delaunay triangulation.
 */

import processing.core.*; 
import processing.xml.*; 

import java.applet.*; 
import java.awt.Dimension; 
import java.awt.Frame; 
import java.awt.event.MouseEvent; 
import java.awt.event.KeyEvent; 
import java.awt.event.FocusEvent; 
import java.awt.Image; 
import java.awt.geom.*;
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

public class DTSplitter extends PApplet
{
    Color drawColor = Color.RED;
    MainMode mainMode = MainMode.INPUT_MODE;
    DemoManager demoManager;
    ScreenState screenState;

    enum MainMode
    {
        INPUT_MODE,
        DEMO_MODE
    }

    public void setup()
    {
        size(640, 480);
        smooth();
        fill(0,0,0);
        frameRate(8);
        screenState = new ScreenState();
        screenState.edges = new ArrayList<Line2D>();
        screenState.points = new HashSet<ColoredPoint>();
        demoManager = new DemoManager(screenState);
    }  
    
    public void draw()
    {
        background(226);

        // Draw the edges
        for (Line2D l : screenState.edges)
        {
            stroke(0,0,0);
            line((int)l.getX1(),
                 (int)l.getY1(),
                 (int)l.getX2(),
                 (int)l.getY2());
        }

        // Draw the points
        for (ColoredPoint p : screenState.points)
        {
            double x = p.getX();
            double y = p.getY();
            if (p.getColor() == Color.RED)
            {
                stroke(255,0,0);
                fill(255,0,0);
            }
            else
            {
                stroke(0,0,255);
                fill(0,0,255);
            }
            ellipse((int)x,(int)y, 5, 5);
        }

        if (mainMode == MainMode.INPUT_MODE)
        { /* The info at the bottom depends on what mode we're in. */
            // Draw the instructions
            fill(0,0,0);
            text("Current Color:", 425, 440);

            if (drawColor == Color.RED)
            {
                fill(255,0,0);
                text("Red", 517, 440);
            }
            else
            {
                fill(0,0,255);
                text("Blue", 517, 440);
            }

            fill(0,0,0);
            text("Press space to toggle color.", 425, 455);
            text("Press enter to start the algorithm.", 425, 470);
        }
        else
        {
            fill(0,0,0);
            stroke(0,0,0);
            text(screenState.displayText,
                 screenState.displayX,
                 screenState.displayY);
        }
    }
    
    public void mousePressed()
    {
        if (mainMode == MainMode.INPUT_MODE)
        { // Handle user input
            ColoredPoint mousePoint = new ColoredPoint(mouseX, mouseY, drawColor);
            for (ColoredPoint p : screenState.points)
            {
                boolean good = true;

                for (Line2D l : screenState.edges)
                {
                    // We can't intersect a line if one of the points is the
                    // same. We aren't using any math operations to cause
                    // roundoff, and all of this code is a hack anyway, so I'm
                    // just using equals.
                    if (l.getP1().equals(p) ||
                            l.getP1().equals(mousePoint) ||
                            l.getP2().equals(p) ||
                            l.getP2().equals(mousePoint))
                        continue;

                    if (l.intersectsLine(p.getX(),
                                         p.getY(),
                                         mousePoint.getX(),
                                         mousePoint.getY()))
                    {
                        good = false;
                        break;
                    }
                }

                if (good)
                    screenState.edges.add(new Line2D.Double(p, mousePoint));
            }

            // When the user clicks, add all necessary edges, then add that point
            // to our set of points.
            screenState.points.add(mousePoint);
        }
    }

    public void keyPressed()
    {
        if (mainMode == MainMode.INPUT_MODE)
        { // Space toggles color in input mode.
            if (key == ' ')
            {
                if (drawColor == Color.RED)
                    drawColor = Color.BLUE;
                else
                    drawColor = Color.RED;
            }

            if (key == ENTER || key == RETURN)
            {
                demoManager.startDemo();
                mainMode = MainMode.DEMO_MODE;
            }
        }
        else
        { // Space steps the algorithm demonstration in demo mode.
            if (!demoManager.step())
            { // If we're done, go back into input mode.
                mainMode = MainMode.INPUT_MODE;
            }
        }
    }
    
    static public void main(String args[])
    {
        PApplet.main(new String[] { "--bgcolor=#FFFFFF", "DTSplitter" });
    }
}
