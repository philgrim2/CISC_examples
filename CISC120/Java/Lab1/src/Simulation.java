import java.util.Random;

public class Simulation
{
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        Random random = new Random();  // Random number generator

        int i = 0; // Loop counter
        int total_steps = 0; // Accumulator for counting steps over all runs

        while (i < m)
        {
            // Accumulator for steps
            int c = 0;

            int x = 0;
            int y = 0;

            while (Math.abs(x) < n && Math.abs(y) < n)
            {
                int direction = random.nextInt(1,5);

                if      (direction == 1)  y += 1; // NORTH
                else if (direction == 2)  x += 1; // EAST
                else if (direction == 3)  y -= 1; // SOUTH
                else if (direction == 4)  x -= 1; // WEST

                c += 1;
            }

            total_steps += c;
            i++;
        }

        double average_steps = (double) total_steps / m;
        System.out.println("The average number of steps was: " + average_steps);
    }
}
