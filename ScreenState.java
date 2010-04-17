import java.util.*;
import java.awt.geom.*;

/* Structure providing easy access to screen state. */

class ScreenState
{
    public ScreenState()
    {
        points = new HashSet<ColoredPoint>();
        edges = new ArrayList<Line2D>();
        selectedPoints = new ArrayList<Point2D>();
        redPoints = new HashSet<Point2D>();
        redEdges = new ArrayList<Line2D>();
        bluePoints = new HashSet<Point2D>();
        blueEdges = new ArrayList<Line2D>();
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
    public HashSet<ColoredPoint> points;
    public ArrayList<Line2D> edges;

    public ArrayList<Point2D> selectedPoints;

    /* Output format: colored points with colored edges creating two Delaunay
     * triangulations. */
    public HashSet<Point2D> redPoints;
    public ArrayList<Line2D> redEdges;
    public HashSet<Point2D> bluePoints;
    public ArrayList<Line2D> blueEdges;

    /* Text display info. */
    public int displayX;
    public int displayY;
    public String displayText = "";
}

