import java.util.Random;

public class Walker
{
    public static void main(String[] args)
    {
        int n = Integer.parseInt(args[0]);

        // Accumulator for steps
        int c = 0;

        int x = 0;
        int y = 0;

        Random r = new Random();

        while (Math.abs(x) < n && Math.abs(y) < n)
        {
            int direction = r.nextInt(1,5);

            if      (direction == 1)  y += 1; // NORTH
            else if (direction == 2)  x += 1; // EAST
            else if (direction == 3)  y -= 1; // SOUTH
            else if (direction == 4)  x -= 1; // WEST

            c += 1;
        }


        System.out.print("The walker took ");
        System.out.print(c);
        System.out.println(" steps.");
    }
}
