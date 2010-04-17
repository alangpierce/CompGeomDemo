

/* A single run of the algorithm (recursive calls create more AlgorithmDemo
 * objects. */

class AlgorithmDemo
{
    ScreenState screenState;

    AlgorithmDemo recursiveCall;

    DisplayState displayState;

    /* We keep track of which step in the algorithm we are in. */
    enum DisplayState
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
        displayState = DisplayState.CHOOSE_POINTS;
    }

    /* Returns true on success, false if the demo is over. */
    public boolean step()
    {
        switch (displayState)
        {
            case CHOOSE_POINTS:
                screenState.displayText = 
                    "Choose two points at random.";
                displayState = DisplayState.FIND_NEIGHBOR;
                break;
            case FIND_NEIGHBOR:
                screenState.displayText = 
                    "Find the nearest neighbor of one of those points.";
                displayState = DisplayState.REMOVE_POINT;
                break;
            case REMOVE_POINT:
                screenState.displayText = 
                    "Remove the point from the triangulation.";
                displayState = DisplayState.RECURSIVE_CALL;
                break;
            case RECURSIVE_CALL:
                screenState.displayText = 
                    "Recursively compute the smaller split triangulations.";
                displayState = DisplayState.ADD_POINT;
                break;
            case ADD_POINT:
                screenState.displayText = 
                    "Find the triangle incident with the previous nearest " +
                    "neighbor.\n" +
                    "Add the removed point back in.";
                displayState = DisplayState.DELAUNAY_FLIP;
                break;
            case DELAUNAY_FLIP:
                screenState.displayText = 
                    "Apply Delaunay flips to make the triangulation a " +
                    "Delaunay triangulation.";
                displayState = DisplayState.END_ALGORITHM;
                break;
            case END_ALGORITHM:
                return false;
        }

        screenState.displayText += "\nPress space to continue.";

        return true;
    }
}
