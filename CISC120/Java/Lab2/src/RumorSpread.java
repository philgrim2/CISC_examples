import java.util.Random;

public class RumorSpread
{
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java RumorSpreadSimulation <number_of_people>");
            return;
        }

        int n = Integer.parseInt(args[0]);

        if (n <= 1) {
            System.out.println("Please specify a valid number of people (greater than 1).");
            return;
        }

        Random random = new Random();
        int spreader = 0;
        int lastSpreader = 0;
        boolean[] heardRumor = new boolean[n];

        System.out.println("Rumor spreading simulation started!");
        int step = 0;

        while (true) {
            step++;

            int listener;
            do {
                listener = random.nextInt(n);
            } while (listener == spreader || listener == lastSpreader);

            heardRumor[listener] = true;
            lastSpreader = spreader;
            spreader = listener;

            System.out.println("Step " + step + ": Person " + spreader + " heard the rumor.");

            if (heardRumor[spreader]) {
                System.out.println("The rumor has reached the same person again. Spread stopped.");
                break;
            }

            boolean allHeard = true;
            for (boolean heard : heardRumor) {
                if (!heard) {
                    allHeard = false;
                    break;
                }
            }

            if (allHeard) {
                System.out.println("All people have heard the rumor. Spread stopped.");
                break;
            }
        }
    }
}