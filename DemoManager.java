
/* Singleton class that manages the demo. */

class DemoManager
{
    ScreenState screenState;

    String[] instructions = new String[]
        { "Choose two points at random.",
          "Find the nearest neighbor of one of those points.",
          "Remove the point from the triangulation.",
          "Recursively compute the smaller split triangulations.",
          "Find the triangle incident with the previous nearest neighbor.\n" +
          "Add the removed point back in.",
          "Apply Delaunay flips to make the triangulation a Delaunay " +
          "triangulation." };
    
    int stepIndex;

    public DemoManager(ScreenState screenState)
    {
        this.screenState = screenState;
        screenState.displayX = 100;
        screenState.displayY = 400;
    }

    public void startDemo()
    {
        stepIndex = 0;
        step();
    }

    /* Returns false if the demo is over. */
    public boolean step()
    {
        if (stepIndex == instructions.length)
            return false;

        screenState.displayText = instructions[stepIndex] +
                                  "\nPress space to continue.";

        stepIndex++;
        return true;
    }
}

