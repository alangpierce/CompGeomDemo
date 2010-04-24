

public class RGBA
{
    int r;
    int g;
    int b;
    int a;

    RGBA(Color c)
    {
        switch(c)
        {
            case BLACK:
                r = g = b = 0;
                a = 255;
                break;

            case RED:
                r = 255;
                g = 0;
                b = 0;
                a = 255;
                break;

            case BLUE:
                r = 0;
                g = 0;
                b = 255;
                a = 255;
                break;

            case YELLOW:
                r = 192;
                g = 192;
                b = 0;
                a = 255;
                break;

            case GREEN:
                r = 0;
                g = 128; // Dark green is better-looking than pure green
                b = 0;
                a = 255;
                break;

            case LIGHT_GRAY:
                r = 128;
                g = 128;
                b = 128;
                a = 48;

            default:
                assert false;
        }
    }
}

