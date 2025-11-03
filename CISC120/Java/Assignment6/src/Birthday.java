public class Birthday {
    public static void main(String[] args) {
        int days = 365;
//people will represent the array index of the person in question
        int people = 0; // total number of people
// Create an empty array of Boolean type for the people, who has birthday on a
// randomly generated day of the year.
// hasBirthday[d] = true if someone born on day d; false otherwise
// auto-initialized to false
        boolean[] hasBirthday = new boolean[days];
// People begin to enter the room. We will use a loop to simulate this.
// We will do the first person manually, to make setting the loop up easier.
// d will represent the day of the year. The loop continues until a match is found,
// at which point we print the number of people that have entered the room
// before the match was found.
        while (true) {
            people++;
// integer between 0 and days-1
            int d = (int) (days * Math.random());

//two people with the same birthday,
// so break out of loop
            if (hasBirthday[d]) break;
            hasBirthday[d] = true; //update array
        }
        System.out.println(people);
    }
}