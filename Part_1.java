import java.util.*;
import java.io.*;

/*
 * CPSC 371 Assignment 1, Part I: Simple 0-1 Knapsack Problem
 * Solves the 0-1 knapsack problem using dynamic programming
 */

public class Part_1 {

    public static void main(String[] args) throws IOException {
        String title = "0-1 Knapsack Problem";
        System.out.println(title);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the data file name: ");
        String filename = scanner.nextLine().trim();

        // Parse input file
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        // While there are lines to read, after trimming, if the line is not empty then
        // that line is added to the lines list
        while ((line = reader.readLine()) != null) {

            line = line.trim();
            if (!line.isEmpty())
                lines.add(line);
        }
        reader.close();

        int capacity = Integer.parseInt(lines.get(0)); // Capacity holds the capacity of the knapsack which is the first
                                                       // line in the file and turns it into an integer
        List<String> ids = new ArrayList<>(); // Creating an empty list to store the ID of the items
        List<Integer> weights = new ArrayList<>(); // Creating an empty list to store the weight of each item
        List<Integer> values = new ArrayList<>(); // Creating an empty list to store the profit of each item

        for (int i = 1; i < lines.size(); i++) { // Starting at the second line and looping through every line
            String[] parts = lines.get(i).split("\\s+"); // Splitting the raw data into sections where the spaces are
            if (parts.length != 3)
                continue; // skip malformed lines, if a line has 2 indexes instead of 3 we skip it
            ids.add(parts[0]); // Adding the item stored in part 0 of the sentence to IDS
            weights.add(Integer.parseInt(parts[1])); // Adding the item stored in part 1 of the sentence to weights
            values.add(Integer.parseInt(parts[2])); // Adding the item stored in part 2 of the sentence to values
        }

        int n = ids.size(); // Total number of items parsed
        System.out.println("Processing...");

        // Build DP table: dp[i][w] = max value using first i items with capacity w
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) { // looping through items (row by row)
            int wi = weights.get(i - 1);
            int vi = values.get(i - 1);
            for (int w = 0; w <= capacity; w++) { // looping through every possible capacity from 0 to max
                if (wi > w) { // If the item is too heavy to fit in capacity w, we take the value from the row
                              // above
                    dp[i][w] = dp[i - 1][w];
                } else { // // choosing best between skipping the item or taking it
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - wi] + vi);
                }
            }
        }

        // Backtracking through the DP table to find chosen items
        List<String> chosen = new ArrayList<>();
        int w = capacity;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                chosen.add(ids.get(i - 1));
                w -= weights.get(i - 1);
            }
        }
        Collections.reverse(chosen); // reversing the list so the items appear in order

        int totalValue = dp[n][capacity];

        String itemListStr = chosen.isEmpty() ? "(none)" : String.join(", ", chosen);

        // Java 8-safe separator (works in all versions)
        String separator = new String(new char[44]).replace('\0', '=');

        // Print result to console
        System.out.println("Done!");
        System.out.println("Result:");
        System.out.println(separator);
        System.out.println("Total Value: " + totalValue);
        System.out.println("Item ID List: " + itemListStr);
        System.out.println(separator);
        System.out.println("Outputting dynamic_table.txt...");
        System.out.println("Done!");
        System.out.println("End of Processing.");

        // Build DP table string
        StringBuilder tableBuilder = new StringBuilder();
        for (int[] row : dp) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0)
                    tableBuilder.append(" ");
                tableBuilder.append(row[j]);
            }
            tableBuilder.append("\n");
        }

        // Write DP table to dynamic_table.txt
        PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table.txt"));
        writer.print(tableBuilder);
        writer.close();

        scanner.close();
    }
}
