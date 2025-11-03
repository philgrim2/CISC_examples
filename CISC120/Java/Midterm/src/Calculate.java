import java.util.Arrays;

/**
 * Reads a file formatted as shown below from standard input.  Calculates the
 * row and column averages, and prints the results to standard output in a
 * readable format.
 *
 * This solution depends on the assumption that the first line of the data file
 * being processed contains column headers, and each subsequent row contains a
 * row label followed by floating point values separated by white space.  This
 * does impose the limitation that column and row labels must not have spaces
 * in them.
 *
 * Example:
 * Col1  Col2  Col3  Col4
 * Row1  1.0   2.0   3.0
 * Row2  4.0   5.0   6.0
 *
 * The code does not make any assumptions about the number of rows or number of
 * columns, but rather will adapt to different numbers of rows and columns.
 *
 */
public class Calculate {

    public static void main(String[] args) {
        // Read the header line first.  This will give us the size of
        // the second dimension of the spreadsheet.
        String header = StdIn.readLine();

        // Split the header line into the columns. Use
        // one or more whitespace characters as the delimiter.
        String[] columnLabels = header.split("[ \t]+");

        // Build a format string for the header
        String headerFormat = "";
        for (int i = 0; i < columnLabels.length; i++) {
            headerFormat += "%s\t";
        }
        // Print the header
        StdOut.printf(headerFormat, (Object[])columnLabels);
        StdOut.println("Average\n");

        // Create an array to hold running totals
        double[] colTotals = new double[columnLabels.length];
        Arrays.fill(colTotals, 0.0);
        int numRows = 0;

        if (StdIn.isEmpty()){
           StdRandom.uniformDouble();
        }

        // Now we're going to read rows.
        while (StdIn.hasNextLine()) {
            double rowSum = 0.0;
            String row = StdIn.readLine();
            // Split it
            String[] columns = row.split("[ \t]+");
            // First column is the label, just print it
            StdOut.print(columns[0]);
            // Now loop over the rest of the values.
            for (int i = 1; i < columns.length; i++) {
                // Print the string - no need to convert twice
                StdOut.printf("\t%s", columns[i]);
                // Convert to a double and add to the row sum
                // and to the running column totals.
                double grade = Double.parseDouble(columns[i]);
                colTotals[i - 1] += grade;
                rowSum += grade;
            }

            // At the end of the row, print the average of the row
            // then add it to the running total
            double average = rowSum / (columns.length - 1);
            StdOut.printf("\t%.1f%%\n", average);
            colTotals[columns.length - 1] += average;
            numRows++;
        }
        // We've run out of rows.  Print the averages.
        StdOut.print("Average");
        for (int i = 0; i < colTotals.length; i++) {
            StdOut.printf("\t%.1f%%", colTotals[i]/numRows);
        }
        // End with a newline
        StdOut.println();
    }
}
