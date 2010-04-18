

/* A single run of the algorithm (recursive calls create more AlgorithmDemo
 * objects). */

class AlgorithmDemo
{
    ScreenState screenState;

    AlgorithmDemo recursiveCall;
    AlgoState algoState;

    /* Keep track of the point that we remove */
    ColoredPoint removedPoint;
    ColoredPoint removedNeighbor;

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
                if (screenState.triangulation.isEmpty())
                { /* Test for base case in our first step. */
                    screenState.displayText =
                        "There are no points, so this is the base case";
                    screenState.displayFormat = ScreenState.DisplayFormat.OUT_FORMAT;
                    algoState = AlgoState.END_ALGORITHM;
                }
                else
                { /* The normal condition is that we choose the points at
                   * random. */
                    screenState.displayText = 
                        "Choose two points at random.";
                    algoState = AlgoState.FIND_NEIGHBOR;
                }
                break;

            case FIND_NEIGHBOR:
                screenState.displayText = 
                    "Find the nearest same-color neighbor of one of those " +
                    "points.";
                algoState = AlgoState.REMOVE_POINT;
                break;

            case REMOVE_POINT:
                screenState.displayText = 
                    "Remove the point from the triangulation.";
                for (ColoredPoint p : screenState.triangulation.points())
                {
                    removedPoint = p;
                    screenState.triangulation.removePoint(p);
                    break;
                }
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

                screenState.blueTriangulation.addPoint(removedPoint);

                algoState = AlgoState.DELAUNAY_FLIP;
                break;

            case DELAUNAY_FLIP:
                screenState.displayText = 
                    "Apply Delaunay flips to make the triangulation a " +
                    "Delaunay triangulation.";
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
