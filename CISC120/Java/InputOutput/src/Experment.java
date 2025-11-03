public class Experment {

    public static void main(String[] args)
    {


        String header = StdIn.readLine();
        String[] columns = header.split(" ");

        while (!StdIn.isEmpty()) {
            String name = StdIn.readString();
            double[] row = new double[columns.length];
            double sum = 0;
            for (int i = 0; i < columns.length - 1; i++){
                row[i] = StdIn.readDouble();
                sum += row[i];
            }
            row[row.length - 1] = sum / (row.length - 1);
        }
    }
}
