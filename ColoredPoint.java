import java.awt.geom.*;

/*
 * Immutable point class, which also keeps track of color.
 */


class ColoredPoint
    extends Point2D.Double
{
    Color color;

    public ColoredPoint(double x, double y, Color color)
    {
        super(x,y);
        this.color = color;
    }

    Color getColor()
    {
        return color;
    }
}


