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

    static boolean demoOver = false;

    /* The "boldness" of a circle that selects a point. */
    static final int selectCircleDiameter = 15;
    static final int selectCircleWidth = 4;
    
    /* Width of an X that crossed off a point. */
    static final int crossedOffWidth = 2;

    /* Width of an X that crossed off a point. */
    static final int foundSquareWidth = 4;
    static final int foundSquareLength = 15;

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
            
            // Draw the background triangulation
            for (Line2D l : screenState.backgroundTriangulation.edges())
                drawLine(l, Color.LIGHT_GRAY);

            for (ColoredPoint p : screenState.backgroundTriangulation.points())
                drawPoint(p, Color.LIGHT_GRAY);

            // Draw the edges
            for (Line2D l : screenState.triangulation.edges())
                drawLine(l, Color.BLACK);

            // Draw the points
            for (ColoredPoint p : screenState.triangulation.points())
                drawPoint(p, p.getColor());

            for (ColoredPoint p : screenState.selectedPoints)
                drawSelectedPoint(p);

            for (ColoredPoint p : screenState.foundPoints)
                drawFoundPoint(p);

            for (ColoredPoint p : screenState.crossedOffPoints)
                drawCrossedOffPoint(p);
        }
        else
        { // Display 2 triangulations with colored edges and vertices.

            // Draw the background triangulation
            for (Line2D l : screenState.backgroundTriangulation.edges())
                drawLine(l, Color.LIGHT_GRAY);

            for (ColoredPoint p : screenState.backgroundTriangulation.points())
                drawPoint(p, Color.LIGHT_GRAY);

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
        RGBA c = new RGBA(color);
        fill(c.r,c.g,c.b,c.a);
        stroke(c.r,c.g,c.b,c.a);
        line((int)l.getX1(),
             (int)l.getY1(),
             (int)l.getX2(),
             (int)l.getY2());
    }
    
    private void drawPoint(Point2D p, Color color)
    {
        RGBA c = new RGBA(color);
        fill(c.r,c.g,c.b,c.a);
        stroke(c.r,c.g,c.b,c.a);
        ellipse((int)p.getX(),(int)p.getY(), 5, 5);
    }

    private void drawSelectedPoint(ColoredPoint p)
    {
        // The circle should be transparent on the interior
        RGBA c = new RGBA(p.getColor());
        fill(0,0,0,0);
        stroke(c.r,c.g,c.b,c.a);
        strokeWeight(selectCircleWidth);

        ellipse((int)p.getX(),(int)p.getY(),
                    selectCircleDiameter, selectCircleDiameter);

        strokeWeight(1);
    }

    private void drawFoundPoint(ColoredPoint p)
    {
        RGBA c = new RGBA(p.getColor());
        fill(0,0,0,0);
        stroke(c.r,c.g,c.b,c.a);
        strokeWeight(foundSquareWidth);
        rectMode(CENTER);

        // Draw the border over and over, one for each width
        rect((int)p.getX(),(int)p.getY(),
                    foundSquareLength, foundSquareLength);

        strokeWeight(1);
    }

    private void drawCrossedOffPoint(ColoredPoint p)
    {
        RGBA c = new RGBA(p.getColor());
        fill(0,0,0);
        stroke(c.r,c.g,c.b,c.a);
        strokeWeight(crossedOffWidth);

        // Draw the two lines to make an X
        line((int)p.getX() - 5,
             (int)p.getY() - 5,
             (int)p.getX() + 5,
             (int)p.getY() + 5);
        line((int)p.getX() + 5,
             (int)p.getY() - 5,
             (int)p.getX() - 5,
             (int)p.getY() + 5);

        strokeWeight(1);
    }
    
    public void mousePressed()
    {
        if (mainMode == MainMode.INPUT_MODE)
        { // Handle user input
            screenState.triangulation.addDelaunayPoint(
                    new ColoredPoint(mouseX, mouseY, drawColor));
            screenState.backgroundTriangulation.addDelaunayPoint(
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
                { // Wait for the user to press enter, in case they were
                  // spamming the space bar.
                    screenState.displayText = "Demo over. Press Enter to " +
                        "input a new set of points";
                    demoOver = true;
                }
            }

            if (key == ENTER || key == RETURN)
            {
                if (demoOver)
                {
                    demoOver = false;
                    mainMode = MainMode.INPUT_MODE;
                    screenState.backgroundTriangulation.clear();
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
