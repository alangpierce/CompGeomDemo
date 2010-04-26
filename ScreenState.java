import java.util.*;
import java.awt.geom.*;

/* Structure providing easy access to screen state. */

class ScreenState
{
    public ScreenState()
    {
        backgroundTriangulation = new Triangulation();
        triangulation = new Triangulation();
        redTriangulation = new Triangulation();
        blueTriangulation = new Triangulation();
        selectedPoints = new ArrayList<ColoredPoint>();
        foundPoints = new ArrayList<ColoredPoint>();
        crossedOffPoints = new ArrayList<ColoredPoint>();
        displayFormat = DisplayFormat.IN_FORMAT;
    }

    public enum DisplayFormat
    {
        IN_FORMAT,
        OUT_FORMAT
    };

    public DisplayFormat displayFormat;

    /* The entire time, we display the original triangulation in light gray. */
    public Triangulation backgroundTriangulation;

    /* User input format: colored points with edges denoting their Delaunay
     * triangulation. */
    public Triangulation triangulation;
    public ArrayList<ColoredPoint> selectedPoints;
    public ArrayList<ColoredPoint> foundPoints;
    public ArrayList<ColoredPoint> crossedOffPoints;

    /* Output format: colored points with colored edges creating two Delaunay
     * triangulations. */
    public Triangulation redTriangulation;
    public Triangulation blueTriangulation;

    /* Text display info. */
    public int displayX;
    public int displayY;
    public String displayText = "";

    public void clearAll()
    { /* Clear all of the data structures that we hold. */
        displayFormat = DisplayFormat.IN_FORMAT;
        backgroundTriangulation.clear();
        triangulation.clear();
        selectedPoints.clear();
        foundPoints.clear();
        crossedOffPoints.clear();
        redTriangulation.clear();
        blueTriangulation.clear();
    }
}

