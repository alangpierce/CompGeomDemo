import java.awt.geom.*;

/*
 * Immutable point class, which also keeps track of color.
 */


class ColoredPoint
    extends Point2D.Double
{
    Color color;

    public ColoredPoint(double x, double y)
    {
        super(x,y);
        color = Color.BLACK;
    }

    public ColoredPoint(double x, double y, Color color)
    {
        super(x,y);
        this.color = color;
    }

    Color getColor()
    {
        return color;
    }

    /* Return a new point with a different color. */
    public ColoredPoint withColor(Color newColor)
    {
        return new ColoredPoint(getX(), getY(), newColor);
    }

    /* Return a new point with x shifted. */
    public ColoredPoint shiftX(double dx)
    {
        return new ColoredPoint(getX()+dx, getY(), color);
    }
}


