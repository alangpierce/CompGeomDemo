import java.awt.geom.*;

/* Line class that stores an unordered pair of points, so that we can do hash
 * table lookups. */
class Line
    extends Line2D.Double
{
    public Line(Point2D p1, Point2D p2)
    {
        super(p1, p2);
    }

    public int hashCode()
    {
        return getP1().hashCode() ^ getP2().hashCode();
    }
    
    public boolean equals(Object obj)
    {
        Line other = (Line)obj;
        return (getP1().equals(other.getP1()) && getP2().equals(other.getP2())) ||
               (getP1().equals(other.getP2()) && getP2().equals(other.getP1()));
    }
}

