import java.util.*;
import java.io.*;

/*
To run this code, put the following in Main...
Part3 part3 = new Part3(filename);
part3.solve(filename);
*/

public class Part3 {

    private int capacity;
    private List<String> ids;
    private List<Integer> weights;
    private List<Integer> values;
    private int n;

    public Part3(String filename) throws IOException {
        ids = new ArrayList<>();
        weights = new ArrayList<>();
        values = new ArrayList<>();
        readData(filename);
    }

    // Read input file and parse capacity and items
    private void readData(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty())
                lines.add(line);
        }
        reader.close();

        capacity = Integer.parseInt(lines.get(0));

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\s+");
            if (parts.length != 3)
                continue;
            ids.add(parts[0]);
            weights.add(Integer.parseInt(parts[1]));
            values.add(Integer.parseInt(parts[2]));
        }

        n = ids.size();
    }

    // Build DP table
    private int[][] buildTable() {
        int[][] dp = new int[n + 1][capacity + 1];
        for (int i = 1; i <= n; i++) {
            int wi = weights.get(i - 1);
            int vi = values.get(i - 1);
            for (int w = 0; w <= capacity; w++) {
                if (wi > w) {
                    dp[i][w] = dp[i - 1][w];
                } else {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - wi] + vi);
                }
            }
        }
        return dp;
    }

    // Traceback from every capacity, keep best result that satisfies constraints
    // Constraint 1: total weight must be ODD
    // Constraint 2: total price must be EVEN
    public void solve(String filename) throws IOException {
        String title = "0-1 Knapsack Problem with Constraints (Odd Weight, Even Price)";
        String separator = new String(new char[44]).replace('\0', '=');

        int[][] dp = buildTable();

        int bestValue = -1;
        int bestWeight = -1;
        List<String> bestChosen = null;

        for (int endCap = 0; endCap <= capacity; endCap++) {
            List<String> chosen = new ArrayList<>();
            int totalWeight = 0;
            int totalPrice = 0;
            int w = endCap;

            for (int i = n; i >= 1; i--) {
                if (dp[i][w] != dp[i - 1][w]) {
                    chosen.add(ids.get(i - 1));
                    totalWeight += weights.get(i - 1);
                    totalPrice += values.get(i - 1);
                    w -= weights.get(i - 1);
                }
            }
            Collections.reverse(chosen);

            if (totalWeight % 2 == 1 && totalPrice % 2 == 0) {
                if (totalPrice > bestValue) {
                    bestValue = totalPrice;
                    bestWeight = totalWeight;
                    bestChosen = chosen;
                }
            }
        }

        // Print result
        System.out.println("Done!");
        System.out.println("Result:");
        System.out.println(separator);

        if (bestValue == -1) {
            System.out.println("No valid solution exists for the given input data.");
            System.out.println(separator);
            System.out.println("End of Processing.");
            return;
        }

        String itemListStr = bestChosen.isEmpty() ? "(none)" : String.join(", ", bestChosen);

        System.out.println("Total Value: " + bestValue);
        System.out.println("Total Weight: " + bestWeight);
        System.out.println("Item ID List: " + itemListStr);
        System.out.println(separator);
        System.out.println("Outputting dynamic_table.txt...");
        System.out.println("Done!");
        System.out.println("End of Processing.");

        // Write DP table to file
        StringBuilder tableBuilder = new StringBuilder();
        for (int[] row : dp) {
            for (int j = 0; j < row.length; j++) {
                if (j > 0)
                    tableBuilder.append(" ");
                tableBuilder.append(row[j]);
            }
            tableBuilder.append("\n");
        }

        PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table.txt"));
        writer.println(title);
        writer.println("Please enter the data file name: " + filename);
        writer.println("Processing...");
        writer.println("Done!");
        writer.println("Result:");
        writer.println(separator);
        writer.println("Total Value: " + bestValue);
        writer.println("Total Weight: " + bestWeight);
        writer.println("Item ID List: " + itemListStr);
        writer.println(separator);
        writer.println("Outputting dynamic_table.txt...");
        writer.println("Done!");
        writer.println("End of Processing.");
        writer.print(tableBuilder);
        writer.close();
    }
}
