import java.util.Random;

public class OtherWalker
{
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);

        // Accumulator for steps
        int c = 0;

        // Set the walker's start position
        int x = 0;
        int y = 0;

        Random random = new Random();
        // While the walker is not at a boundary, take a step
        while ( (Math.abs(x) < n) && (Math.abs(y) < n) )
        {
            int direction = random.nextInt(1,5);
            if      (direction == 1)  y = y + 1;
            else if (direction == 2)  x = x + 1;
            else if (direction == 3)  y = y - 1;
            else if (direction == 4)  x = x - 1;
            // Count the step
            c = c + 1;
        }

        System.out.print("The walker took ");
        System.out.print(c);
        System.out.println(" steps.");
    }
}