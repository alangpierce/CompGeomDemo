
/* Timer object. This class needs to be fed with a consistent timer, and if it
 * is  */
class Timer
{
    TickHandler handler;
    int ticksPerSecond;
    int currentCount;
    boolean active;
    int maxTick;

    Timer(TickHandler handler, int ticksPerSecond)
    {
        this.handler = handler;
        this.ticksPerSecond = ticksPerSecond;
        active = false;
    }

    /* Take as an argument the period for the timer, in milliseconds. */
    void start(int period)
    {
        if (!active)
        {
            active = true;
            currentCount = 0;
            /* We need to round to a certain extent. */
            maxTick = period * ticksPerSecond / 1000;
        }
    }

    void stop()
    {
        if (active)
            active = false;
    }

    /* Regardless of what's going on, this method should be called at the rate
     * specified by the constructor. */
    void feed()
    {
        if (active)
        {
            currentCount++;
            if (currentCount == ticksPerSecond)
            {
                currentCount = 0;
                handler.tick();
            }
        }
    }


    /* To use this timer, a TickHandler needs to be passed in, which will
     * receive ticks at the appropriate interval. */
    public interface TickHandler
    {
        void tick();
    }
}

