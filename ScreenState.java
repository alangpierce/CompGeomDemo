import java.util.*;
import java.awt.geom.*;

/* Structure providing easy access to screen state. */

class ScreenState
{
    public HashSet<ColoredPoint> points;
    public ArrayList<Line2D> edges;
    public int displayX;
    public int displayY;
    public String displayText = "";
}

