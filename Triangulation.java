import java.util.*;
import java.awt.geom.*;

/* Holds a general-purpose triangulation  */

class Triangulation
{
    HashSet<ColoredPoint> points;
    HashSet<Line2D> edges;

    public Triangulation()
    {
        points = new HashSet<ColoredPoint>();
        edges = new HashSet<Line2D>();
    }

    public HashSet<ColoredPoint> points()
    {
        // TODO
        return points;
    }

    public HashSet<Line2D> edges()
    {
        // TODO
        return edges;
    }

    /* Add a point and add edges to make it a triangulation. */
    public void addPoint(ColoredPoint p)
    {
        points.add(p);

        fixTriangulation();
    }

    /* Add edges to make this a triangulation. */
    public void fixTriangulation()
    {
        /* Totally insane algorithm: for each pair of points, check all edges to
         * find intersections. If there are none, add that edges. */

        for (ColoredPoint p : points)
        {
            for (ColoredPoint q : points)
            {
                boolean good = true;

                if (p.equals(q))
                    continue;

                if (lineInSet(new Line2D.Double(p,q), edges))
                    continue;

                for (Line2D l : edges)
                {
                    if (l.getP1().equals(p) ||
                            l.getP1().equals(q) ||
                            l.getP2().equals(p) ||
                            l.getP2().equals(q))
                        continue;

                    if (l.intersectsLine(q.getX(),
                                         q.getY(),
                                         p.getX(),
                                         p.getY()))
                    {
                        good = false;
                        break;
                    }
                }
                if (good)
                    edges.add(new Line2D.Double(p,q));
            }
        }
    }

    /* Add a point, ensuring that if the triangulation was Delaunay before, it
     * remains Delaunay. */
    public void addDelaunayPoint(ColoredPoint p)
    {
        /* For now, we do this the stupid way. This can probably be made more
         * efficient. */
        addPoint(p);

        makeDelaunay();

        /* Delaunize the surrounding points and edges until we can't anymore. */
        while (stepDelaunay(p))
            ;
    }

    public void removePoint(ColoredPoint p)
    {
        points.remove(p);

        /* Remove all edges incident with the removed point. */
        Iterator<Line2D> iter = edges.iterator();
        while (iter.hasNext())
        {
            Line2D l = iter.next();

            if (l.getP1().equals(p) ||
                    l.getP2().equals(p))
                iter.remove();
        }

        fixTriangulation();

        makeDelaunay();
    }

    public boolean isEmpty()
    {
        return points.isEmpty();
    }

    public void clear()
    {
        points.clear();
        edges.clear();
    }

    /* Flip non-Delaunay edges until we are Delaunay. */
    public void makeDelaunay()
    {
        boolean updated;
        int counter = 0;
        do
        { /* Loop as long as we change something. */
            updated = false;

            for (Line2D l : edges)
            {
                /* Try to do a flip on this edges. If it's successful, we've
                 * changed our edges data structure, so we need to iterate back
                 * from the start. */
                if (flipEdge(l))
                {
                    updated = true;
                    break;
                }
            }
            counter++;
            if (counter > 100)
                break;
        } while (updated);
    }

    /* Delaunize the neighborhood of a point, as we would do in the incremental
     * Delaunay Triangulation algorithm. Return false if no edge could be
     * flipped. Otherwise, return true and flip one edge. */
    public boolean stepDelaunay(ColoredPoint p)
    {
        for (Line2D l : edges)
        {
            if (lineInSet(new Line2D.Double(p, l.getP1()), edges) &&
                lineInSet(new Line2D.Double(p, l.getP2()), edges))
            {
                if (flipEdge(l))
                    return true;
            }
        }
        return false;
    }

    /* Find the circle containing the triangle under the given point, or null if
     * there is none. */
    public Circle highlightedTriangle(ColoredPoint p)
    {
        double bestArea = 0.0;
        ColoredPoint bestP1 = null;
        ColoredPoint bestP2 = null;
        ColoredPoint bestP3 = null;

        /* Try every triangle by brute force, finding the one of minimal area
         * (this works because any bad ones will strictly contain the good one. */
        for (ColoredPoint p1 : points)
        {
            for (ColoredPoint p2 : points)
            {
                if (p1.equals(p2))
                    break;
                if (!lineInSet(new Line2D.Double(p1, p2), edges))
                    continue;

                for (ColoredPoint p3 : points)
                {
                    if (p3.equals(p2))
                        break;
                    if (!lineInSet(new Line2D.Double(p1, p3), edges) ||
                        !lineInSet(new Line2D.Double(p2, p3), edges))
                        continue;

                    if (ptInTriangle(p, p1, p2, p3))
                    {
                        double newArea = triangleArea(p1, p2, p3);
                        if (bestP1 == null ||
                            newArea < bestArea)
                        {
                            bestP1 = p1;
                            bestP2 = p2;
                            bestP3 = p3;
                            bestArea = newArea;
                        }
                    }
                }
            }
        }

        if (bestP1 == null)
            return null;

        /* From here, we want to find the intersection of any two perpendicular
         * bisectors. q1 and q2 are the two midpoints. */
        ColoredPoint q1 = new ColoredPoint((bestP1.getX() + bestP2.getX()) / 2.0,
                                           (bestP1.getY() + bestP2.getY()) / 2.0);
        ColoredPoint q2 = new ColoredPoint((bestP2.getX() + bestP3.getX()) / 2.0,
                                           (bestP2.getY() + bestP3.getY()) / 2.0);

        /* d1 and d2 are direction vectors along the original edges. */
        ColoredPoint d1 = new ColoredPoint(q1.getX() - bestP1.getX(),
                                           q1.getY() - bestP1.getY());
        ColoredPoint d2 = new ColoredPoint(q2.getX() - bestP2.getX(),
                                           q2.getY() - bestP2.getY());

        /* dir1 and dir2 are rotated versions of d1 and d2.  */
        ColoredPoint dir1 = new ColoredPoint(-d1.getY(), d1.getX());
        ColoredPoint dir2 = new ColoredPoint(-d2.getY(), d2.getX());

        /* We now want to solve the equation q1 + a*dir1 = q2 + b*dir2 for a and
         * b. Putting this into a standard form, we get
         * a*dir1 - b*dir2 = q2 - q1. Let r = q2 - q1 for easier manipulations
         * later. */
        ColoredPoint r = new ColoredPoint(q2.getX() - q1.getX(),
                                          q2.getY() - q1.getY());

        /* We can use Cramer's Rule to compute the actual solution. */
        double det = dir1.getX()*(-dir2.getY()) - (-dir2.getX())*dir1.getY();

        if (Math.abs(det) < 1e-9)
            return null;

        double a = (-r.getX()*dir2.getY() + r.getY()*dir2.getX()) / det;
        double b = (dir1.getX()*r.getY() - dir1.getY()*r.getX()) / det;

        ColoredPoint centerPoint = new ColoredPoint(q1.getX() + a*dir1.getX(),
                                              q1.getY() + a*dir1.getY());

        return new Circle(centerPoint, centerPoint.distance(bestP1));
    }

    public boolean lineInSet(Line2D l, HashSet<Line2D> set)
    {
        return set.contains(l) ||
               set.contains(new Line2D.Double(l.getP2(), l.getP1()));
        /*
        for (Line2D m : set)
        {
            // TODO: fix this awful hack. It turns out the Line2D isn't as nice
            // as I wanted it to be, and keeps the points as an ordered pair
            // instead of an unordered one.
            if ((l.getP1().equals(m.getP1()) && l.getP2().equals(m.getP2())) ||
                (l.getP1().equals(m.getP2()) && l.getP2().equals(m.getP1())))
                return true;
        }
        return false;
        */
    }

    /* Returns true if success, false if it didn't need flipping. */
    public boolean flipEdge(Line2D l)
    {
        Point2D neighbor1 = null;
        Point2D neighbor2 = null;

        Point2D p1 = l.getP1();
        Point2D p2 = l.getP2();

        /* If this edge is not on the convex hull, then there are 2 points that
         * form "empty" triangles when connected with this edge. If the edge is
         * on the convex hull, then there is only one such point. */
        for (Point2D q : points)
        {
            /* Any neighbors we add need to be adjacent to both points in our
             * edge. */
            if (!lineInSet(new Line2D.Double(q,p1), edges) ||
                !lineInSet(new Line2D.Double(q,p2), edges))
            {
                continue;
            }

            /* If we haven't seen anything yet, set this point as a neighbor.
             * Otherwise, if one point is inside the other, use the inner point.
             * Otherwise, this point must go on the other side. */
            if (neighbor1 == null)
            {
                neighbor1 = q;
                continue;
            }

            if (ptInTriangle(q,p1,p2,neighbor1))
            {
                neighbor1 = q;
                continue;
            }
            else if (ptInTriangle(neighbor1,p1,p2,q))
                continue;

            /* Check other side. */
            if (neighbor2 == null)
            {
                neighbor2 = q;
                continue;
            }

            if (ptInTriangle(q,p1,p2,neighbor2))
                neighbor2 = q;
        }

        if (neighbor2 == null)
            return false;

        if (ptInCircle(neighbor1, p1, p2, neighbor2) ||
            ptInCircle(neighbor2, p1, p2, neighbor1))
        {
            edges.remove(l);
            edges.add(new Line2D.Double(neighbor1, neighbor2));
            return true;
        }

        return false;
    }

    /* Get the area of the triangle defined by the three points. */
    public static double triangleArea(Point2D a,
                                      Point2D b,
                                      Point2D c)
    {
        /* Use the shoelace theorem. */
        return Math.abs(a.getX()*b.getY() +
                        b.getX()*c.getY() +
                        c.getX()*a.getY() -
                        a.getY()*b.getX() -
                        b.getY()*c.getX() -
                        c.getY()*a.getX()) / 2.0;
    }

    /* Return true iff point p is inside triangle abc. */
    public static boolean ptInTriangle(Point2D p,
                                       Point2D a,
                                       Point2D b,
                                       Point2D c)
    {
        int ccw1 = CCW(a,b,p);
        int ccw2 = CCW(b,c,p);
        int ccw3 = CCW(c,a,p);

        return (ccw1 == ccw2) && (ccw2 == ccw3);
    }

    public static boolean ptInCircle(Point2D p,
                              Point2D a,
                              Point2D b,
                              Point2D c)
    {
        double psqr = p.distanceSq(0.0, 0.0);
        double asqr = a.distanceSq(0.0, 0.0);
        double bsqr = b.distanceSq(0.0, 0.0);
        double csqr = c.distanceSq(0.0, 0.0);

        double [][] matrix = {{a.getX(), b.getX(), c.getX(), p.getX()},
                              {a.getY(), b.getY(), c.getY(), p.getY()},
                              {asqr,     bsqr,     csqr,     psqr},
                              {1.0,      1.0,      1.0,      1.0}};

        return sign(Matrix.determinant(matrix)) * CCW(a,b,c) >= 0;
    }

    public static int CCW(Point2D a,
                   Point2D b,
                   Point2D c)
    {
        double[][] matrix = {{a.getX(), b.getX(), c.getX()},
                             {a.getY(), b.getY(), c.getY()},
                             {1.0,      1.0,      1.0}};
        return sign(Matrix.determinant(matrix));
    }

    public static int sign(double x)
    {
        if (x > 0)
            return 1;
        else if (x < 0)
            return -1;
        else
            return 0;
    }

    /* Unit tests. */
    public static void main(String[] args)
    {
        boolean enabled = false;
        try
        {
            assert false;
        }
        catch (AssertionError e)
        {
            enabled = true;
        }

        if (!enabled)
        {
            System.out.println("You need to enable assertions!");
            return;
        }

        assert CCW(new Point2D.Double(5,5),
                   new Point2D.Double(3,10),
                   new Point2D.Double(0,0)) == 1;

        assert CCW(new Point2D.Double(5,5),
                   new Point2D.Double(3,10),
                   new Point2D.Double(10,10)) == -1;

        assert ptInCircle(new Point2D.Double(-1,0),
                          new Point2D.Double(0,1),
                          new Point2D.Double(0,-1),
                          new Point2D.Double(0.5,0));

        assert ptInCircle(new Point2D.Double(-1,0),
                          new Point2D.Double(0,-1),
                          new Point2D.Double(0,1),
                          new Point2D.Double(0.5,0));

        System.out.println("Tests passed!");
    }

}

