public class Simulation {
    public static void main(String[] args) {
        int days = 365;
        int trials = 100; // number of trials
        int people = 0; // total number of people over all trials
// repeat experiment trials times
        for (int t = 0; t < trials; t++) {
// hasBirthday[d] = true if someone born on day d; false otherwise
            boolean[] hasBirthday = new boolean[days];
            while (true) {
                people++; // one more person enters the room
                int d = (int) (days * Math.random()); // integer between 0 and days-1
                if (hasBirthday[d]) break; // found two people with the same birthday
                hasBirthday[d] = true; // update array
            }
        }
        double average = (double) people / trials;
        System.out.println("Average = " + average);
    }
}
