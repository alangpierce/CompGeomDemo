

public class RGB
{
    int r;
    int g;
    int b;

    RGB(Color c)
    {
        switch(c)
        {
            case BLACK:
                r = g = b = 0;
                break;

            case RED:
                r = 255;
                g = 0;
                b = 0;
                break;

            case BLUE:
                r = 0;
                g = 0;
                b = 255;
                break;
        }
    }
}

