import java.util.*;
import java.awt.geom.*;

/* Structure providing easy access to screen state. */

class ScreenState
{
    public ScreenState()
    {
        triangulation = new Triangulation();
        redTriangulation = new Triangulation();
        blueTriangulation = new Triangulation();
        selectedPoints = new ArrayList<ColoredPoint>();
        crossedOffPoints = new ArrayList<ColoredPoint>();
        displayFormat = DisplayFormat.IN_FORMAT;
    }

    public enum DisplayFormat
    {
        IN_FORMAT,
        OUT_FORMAT
    };

    public DisplayFormat displayFormat;

    /* User input format: colored points with edges denoting their Delaunay
     * triangulation. */
    public Triangulation triangulation;
    public ArrayList<ColoredPoint> selectedPoints;
    public ArrayList<ColoredPoint> crossedOffPoints;

    /* Output format: colored points with colored edges creating two Delaunay
     * triangulations. */
    public Triangulation redTriangulation;
    public Triangulation blueTriangulation;

    /* Text display info. */
    public int displayX;
    public int displayY;
    public String displayText = "";
}

