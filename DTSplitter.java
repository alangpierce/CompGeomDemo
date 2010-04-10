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

public class DTSplitter extends PApplet {

    HashSet<Point2D> points;

    // Use a really naive algorithm to form the edges: just add lines as long as
    // they don't create crossings.
    ArrayList<Line2D> edges = new ArrayList<Line2D>();

    public void setup()
    {
        points = new HashSet<Point2D>();
        size(640, 480);
        smooth();
        fill(0,0,0);
    }  
    
    public void draw()
    {
        background(226);

        // Draw the edges
        for (Line2D l : edges)
        {
            line((int)l.getX1(),
                 (int)l.getY1(),
                 (int)l.getX2(),
                 (int)l.getY2());
        }

        // Draw the points
        for (Point2D p : points)
        {
            double x = p.getX();
            double y = p.getY();
            ellipse((int)x,(int)y, 3, 3);
        }

    }
    
    public void mousePressed()
    {
        Point2D mousePoint = new Point2D.Double(mouseX ,mouseY);
        for (Point2D p : points)
        {
            boolean good = true;

            for (Line2D l : edges)
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
                edges.add(new Line2D.Double(p, mousePoint));
        }

        // When the user clicks, add all necessary edges, then add that point
        // to out set of points.
        points.add(new Point2D.Double(mouseX, mouseY));
    }
    
    static public void main(String args[])
    {
        PApplet.main(new String[] { "--bgcolor=#FFFFFF", "DTSplitter" });
    }
}
