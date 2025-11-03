public class Distance {
    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]); //Set the size of the arrays (length of the vectors)
//int min = 0;
        int max = 100;
// create the two arrays and initialize them with random numbers.
// This is strictly to demonstrate the code fragment working, and is not a
// required part of the assignment.
        int[] first_array = new int[size];
        int[] second_array = new int[size];
//Generate random vectors with inputs between 0 and user defined max
// value and store them in the arrays.
        for (int i = 0; i < size; i++) {
            first_array[i] = (int) (max * Math.random());
            second_array[i] = (int) (max * Math.random());
        }
//This part is the required bit of the assignment.
//We will use an accumulator loop to collect the sum of the squared differences
//for each pair of elements in our vectors.
//calculate the Euclidean distance between the two point.
        double distance = 0.0;
        for (int i = 0; i < size; i++) {
            double diff = first_array[i] - second_array[i];
            distance += diff * diff;
        }
//The distance is the square root of the sum of squared differences.
        distance = Math.sqrt(distance);

        System.out.println("Distance between the points: " + distance);
    }
}
