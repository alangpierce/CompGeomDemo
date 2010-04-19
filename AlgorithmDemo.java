import java.util.*;

/* A single run of the algorithm (recursive calls create more AlgorithmDemo
 * objects). */

class AlgorithmDemo
{
    ScreenState screenState;

    AlgorithmDemo recursiveCall;
    AlgoState algoState;

    /* The points that we choose at the beginning. The color of these points is
     * their original color (red or blue), not the displayed color (green or
     * yellow). */
    ColoredPoint chosenPoint1;
    ColoredPoint chosenPoint2;

    /* Keep track of the point that we remove */
    ColoredPoint removedPoint;
    ColoredPoint removedNeighbor;

    ArrayList<ColoredPoint> searchPoints1;
    ArrayList<ColoredPoint> searchPoints2;

    Triangulation currentTriangulation;

    public static Color chosen1Color = Color.YELLOW;
    public static Color chosen2Color = Color.GREEN;

    /* We keep track of which step in the algorithm we are in. */
    enum AlgoState
    {
        CHOOSE_POINTS,
        FIND_NEIGHBOR,
        REMOVE_POINT,
        RECURSIVE_CALL,
        ADD_POINT,
        DELAUNAY_FLIP,
        END_ALGORITHM
    };

    public AlgorithmDemo(ScreenState screenState)
    {
        this.screenState = screenState;
        algoState = AlgoState.CHOOSE_POINTS;

        assert screenState.displayFormat == ScreenState.DisplayFormat.IN_FORMAT;

        removedPoint = null;
    }

    /* Returns true on success, false if the demo is over. */
    public boolean step()
    {
        boolean endMessageEnabled = true;
        switch (algoState)
        {
            case CHOOSE_POINTS:
                if (screenState.triangulation.points().size() < 3)
                { /* Test for base case in our first step. */
                    screenState.displayText =
                        "There are fewer than 3 points, so this is a base case";

                    for (ColoredPoint p : screenState.triangulation.points())
                    {
                        if (p.getColor() == Color.RED)
                            screenState.redTriangulation.addPoint(p);
                        else
                            screenState.blueTriangulation.addPoint(p);
                    }

                    screenState.triangulation.clear();

                    screenState.displayFormat = ScreenState.DisplayFormat.OUT_FORMAT;
                    algoState = AlgoState.END_ALGORITHM;
                }
                else
                { /* The normal condition is that we choose two points at
                   * random. */
                    screenState.displayText = 
                        "Choose two points at random.";

                    searchPoints1 = new
                        ArrayList<ColoredPoint>(screenState.triangulation.points());
                    searchPoints2 = new ArrayList<ColoredPoint>(searchPoints1);

                    Random r = new Random();
                    int pt1 = r.nextInt(searchPoints1.size());
                    int pt2;
                    
                    do
                    {
                        pt2 = r.nextInt(searchPoints1.size());
                    } while (pt2 == pt1);

                    chosenPoint1 = searchPoints1.get(pt1);
                    chosenPoint2 = searchPoints1.get(pt2);

                    searchPoints1.remove(chosenPoint1);
                    searchPoints2.remove(chosenPoint2);

                    screenState.selectedPoints.add(
                            chosenPoint1.withColor(chosen1Color));
                    screenState.selectedPoints.add(
                            chosenPoint2.withColor(chosen2Color));

                    algoState = AlgoState.FIND_NEIGHBOR;
                }
                break;

            case FIND_NEIGHBOR:
                screenState.displayText = 
                    "Find the nearest same-color neighbor of one of those " +
                    "points.";

                double nearestDistance = -1.0;
                ColoredPoint nearPoint = null;
                ColoredPoint neighbor = null;

                /* Find the nearest neighbor of either point that hasn't yet
                 * been removed. We need to keep two separate lists beause if we
                 * don't, then we might remove a point for being the wrong
                 * color, when really it should have matched with the other
                 * point. */
                for (ColoredPoint p : searchPoints1)
                {
                    if (nearestDistance == -1.0 ||
                        p.distance(chosenPoint1) < nearestDistance)
                    {
                        nearestDistance = p.distance(chosenPoint1);
                        nearPoint = chosenPoint1;
                        neighbor = p;
                    }
                }
                for (ColoredPoint p : searchPoints2)
                {
                    if (nearestDistance == -1.0 ||
                        p.distance(chosenPoint2) < nearestDistance)
                    {
                        nearestDistance = p.distance(chosenPoint2);
                        nearPoint = chosenPoint2;
                        neighbor = p;
                    }
                }

                if (neighbor.getColor() == nearPoint.getColor())
                {
                    removedPoint = nearPoint;
                    Color goodColor = null;
                    if (nearPoint.equals(chosenPoint1))
                        goodColor = chosen1Color;
                    else if (nearPoint.equals(chosenPoint2))
                        goodColor = chosen2Color;
                    else
                        assert false;

                    screenState.foundPoints.add(neighbor.withColor(goodColor));
                    screenState.selectedPoints.clear();
                    screenState.selectedPoints.add(
                            nearPoint.withColor(goodColor));

                    screenState.crossedOffPoints.clear();
                    algoState = AlgoState.REMOVE_POINT;
                }
                else
                {
                    ColoredPoint crossedOff =
                            neighbor.withColor(nearPoint.getColor());
                    /* We want to shift left or right based on the color, so
                     * that it is apparent when a point gets crossed off twice. */
                    if (nearPoint.equals(chosenPoint1))
                    {
                        screenState.crossedOffPoints.add(
                                crossedOff.withColor(chosen1Color).shiftX(-2));
                        searchPoints1.remove(neighbor);
                    }
                    else if (nearPoint.equals(chosenPoint2))
                    {
                        screenState.crossedOffPoints.add(
                                crossedOff.withColor(chosen2Color).shiftX(2));
                        searchPoints2.remove(neighbor);
                    }
                    else
                        assert false;
                }

                break;

            case REMOVE_POINT:
                screenState.displayText = 
                    "Save the information about the neighbor and remove the" +
                    " point from the triangulation. .";

                screenState.triangulation.removePoint(removedPoint);
                screenState.selectedPoints.clear();
                screenState.foundPoints.clear();

                algoState = AlgoState.RECURSIVE_CALL;
                break;

            case RECURSIVE_CALL:
                if (recursiveCall == null)
                { /* If we haven't started the recursive call yet, start it. */
                    recursiveCall = new AlgorithmDemo(screenState);
                    screenState.displayText = 
                        "Recursively compute the smaller split triangulations.";
                }
                else
                { /* If we're in the middle of a recursive call, do that. If
                   * it's already finished, move to the next step. */
                    if (!recursiveCall.step())
                    {
                        screenState.displayText = 
                            "The recursive call has finished.";
                        algoState = AlgoState.ADD_POINT;
                    }
                    else
                        endMessageEnabled = false;
                }
                break;

            case ADD_POINT:
                screenState.displayText = 
                    "Find the triangle incident with the previous nearest " +
                    "neighbor.\n" +
                    "Add the removed point back in.";

                if (removedPoint.getColor() == Color.RED)
                    currentTriangulation = screenState.redTriangulation;
                else
                    currentTriangulation = screenState.blueTriangulation;

                currentTriangulation.addPoint(removedPoint);

                algoState = AlgoState.DELAUNAY_FLIP;
                break;

            case DELAUNAY_FLIP:
                screenState.displayText = 
                    "Apply Delaunay flips to make the triangulation a " +
                    "Delaunay triangulation.";
                
                if (!currentTriangulation.stepDelaunay(removedPoint))
                    algoState = AlgoState.END_ALGORITHM;

                break;

            case END_ALGORITHM:
                return false;
        }

        if (endMessageEnabled)
            screenState.displayText += "\nPress space to step the algorithm.";

        return true;
    }
}
