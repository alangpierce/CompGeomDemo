
/* Class that manages the demonstration. */
class DemoManager
{
    ScreenState screenState;
    AlgorithmDemo topLevelDemo;

    public DemoManager(ScreenState screenState)
    {
        this.screenState = screenState;
        screenState.displayX = 10;
        screenState.displayY = 407;
    }

    public void startDemo()
    {
        topLevelDemo = new AlgorithmDemo(screenState);
        step();
    }

    /* Returns false if the demo is over. */
    public boolean step()
    {
        return topLevelDemo.step();
    }
}

