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
        demoManager = new DemoManager(screenState);
    }  

    public void draw()
    {
        background(226);

        if (screenState.displayFormat == ScreenState.DisplayFormat.IN_FORMAT)
        { // Display a triangulation with black edges and colored vertices.
            // Draw the edges
            for (Line2D l : screenState.triangulation.edges())
                drawLine(l, Color.BLACK);

            // Draw the points
            for (ColoredPoint p : screenState.triangulation.points())
                drawPoint(p, p.getColor());

            for (ColoredPoint p : screenState.selectedPoints)
                drawSelectedPoint(p);

            for (ColoredPoint p : screenState.crossedOffPoints)
                drawCrossedOffPoint(p);
        }
        else
        { // Display 2 triangulations with colored edges and vertices.
            // Draw the edges
            for (Line2D l : screenState.redTriangulation.edges())
                drawLine(l, Color.RED);
            for (Line2D l : screenState.blueTriangulation.edges())
                drawLine(l, Color.BLUE);

            // Draw the points
            for (Point2D p : screenState.redTriangulation.points())
                drawPoint(p, Color.RED);
            for (Point2D p : screenState.blueTriangulation.points())
                drawPoint(p, Color.BLUE);
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

    private void drawLine(Line2D l, Color color)
    {
        RGB c = new RGB(color);
        fill(c.r,c.g,c.b);
        stroke(c.r,c.g,c.b);
        line((int)l.getX1(),
             (int)l.getY1(),
             (int)l.getX2(),
             (int)l.getY2());
    }
    
    private void drawPoint(Point2D p, Color color)
    {
        RGB c = new RGB(color);
        fill(c.r,c.g,c.b);
        stroke(c.r,c.g,c.b);
        ellipse((int)p.getX(),(int)p.getY(), 5, 5);
    }

    private void drawSelectedPoint(Point2D p)
    {
        // The circle should be transparent on the interior
        fill(0,0,0,0);
        stroke(0,0,0);
        ellipse((int)p.getX(),(int)p.getY(), 15, 15);
    }

    private void drawCrossedOffPoint(Point2D p)
    {
        fill(0,0,0);
        stroke(0,0,0);
        // Draw the two lines to make an X
        line((int)p.getX() - 5,
             (int)p.getY() - 5,
             (int)p.getX() + 5,
             (int)p.getY() + 5);
        line((int)p.getX() + 5,
             (int)p.getY() - 5,
             (int)p.getX() - 5,
             (int)p.getY() + 5);
    }
    
    public void mousePressed()
    {
        if (mainMode == MainMode.INPUT_MODE)
        { // Handle user input
            screenState.triangulation.addDelaunayPoint(
                    new ColoredPoint(mouseX, mouseY, drawColor));
        }
    }

    public void keyPressed()
    {
        if (mainMode == MainMode.INPUT_MODE)
        { // Space toggles color in input mode.
            assert false;
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
            if (key == ' ')
            {
                if (!demoManager.step())
                { // If we're done, go back into input mode.
                    mainMode = MainMode.INPUT_MODE;
                    screenState.displayFormat = ScreenState.DisplayFormat.IN_FORMAT;
                    assert screenState.triangulation.isEmpty();
                    assert screenState.selectedPoints.isEmpty();

                    screenState.redTriangulation.clear();
                    screenState.blueTriangulation.clear();
                }
            }
        }
    }
    
    static public void main(String args[])
    {
        PApplet.main(new String[] { "--bgcolor=#FFFFFF", "DTSplitter" });
    }
}
