import java.util.*;

class Matrix
{
    public static double determinant(double[][] m)
    {
        double res = 1.0;

        /* Row reduce. */
        for (int i = 0; i < m.length; i++)
        {
            /* Find a row with a nonzero entry and swap. */
            for (int j = i; j < m.length; j++)
            {
                if (Math.abs(m[j][i]) > 1e-9)
                {
                    if (i != j)
                    {
                        swapRows(m,i,j);
                        res = -res;
                    }
                    break;
                }
            }

            for (int j = i+1; j < m.length; j++)
                addRow(m, i, j, -m[j][i] / m[i][i]);
        }

        for (int i = 0; i < m.length; i++)
            System.out.println(Arrays.toString(m[i]));
        System.out.println();

        /* Multiply the terms along the diagonal. */
        for(int i = 0; i < m.length; i++)
            res *= m[i][i];

        return res;
    }

    /* Set r2 = r2 + scale*r1. */
    public static void addRow(double[][] m, int r1, int r2, double scale)
    {
        for (int i = 0; i < m[0].length; i++)
            m[r2][i] += scale*m[r1][i];
    }

    /* Swap rows r1 and r2 */
    public static void swapRows(double[][] m, int r1, int r2)
    {
        for (int i = 0; i < m[0].length; i++)
        {
            double temp = m[r1][i];
            m[r1][i] = m[r2][i];
            m[r2][i] = temp;
        }
    }

    /* Unit tests */
    public static void main(String[] args)
    {
        double[][] matrix1 = {{5.0,3.0},
                              {2.0,1.0}};

        System.out.println("Det: " + determinant(matrix1));

        assert Math.abs(determinant(matrix1) + 1) < 1e-9;

        System.out.println("Tests passed!");
    }
}
