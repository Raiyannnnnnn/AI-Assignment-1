import java.util.*;
import java.io.*;

/*
 * CPSC 371 Assignment 1, Part III: Knapsack Problem with Constraints
 * Solves the 0-1 knapsack problem using dynamic programming with the following constraints:
 * 1. The total weight of the selected items must be an odd number
 * 2. The total price of the selected items must be an even number
 */

public class Part3 {

    public static void main(String[] args) throws IOException {
        System.out.println("0-1 Knapsack Problem with Constraints");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the data file name: ");
        String filename = scanner.nextLine().trim();

        // Parse input file
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty())
                lines.add(line);
        }
        reader.close();

        int capacity = Integer.parseInt(lines.get(0));
        List<String> ids = new ArrayList<>();
        List<Integer> weights = new ArrayList<>();
        List<Integer> values = new ArrayList<>();

        for (int i = 1; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\s+");
            if (parts.length != 3)
                continue;
            ids.add(parts[0]);
            weights.add(Integer.parseInt(parts[1]));
            values.add(Integer.parseInt(parts[2]));
        }

        int n = ids.size();
        System.out.println("Processing...");

        // dp[i][w][wp][vp] = max value using first i items with capacity w,
        // where wp = parity of total weight of selected items (0=even, 1=odd)
        // and vp = parity of total value of selected items (0=even, 1=odd)
        // -1 means this parity combination is unreachable
        int[][][][] dp = new int[n + 1][capacity + 1][2][2];
        for (int[][][] a : dp)
            for (int[][] b : a)
                for (int[] c : b)
                    Arrays.fill(c, -1);

        // Base case: 0 items selected => weight=0 (even), value=0 (even), valid at any
        // capacity
        for (int w = 0; w <= capacity; w++)
            dp[0][w][0][0] = 0;

        // Fill DP table
        for (int i = 1; i <= n; i++) {
            int wi = weights.get(i - 1);
            int vi = values.get(i - 1);
            for (int w = 0; w <= capacity; w++) {
                for (int wp = 0; wp < 2; wp++) {
                    for (int vp = 0; vp < 2; vp++) {
                        // Option 1: skip item i
                        if (dp[i - 1][w][wp][vp] != -1)
                            dp[i][w][wp][vp] = dp[i - 1][w][wp][vp];

                        // Option 2: take item i (if it fits)
                        if (wi <= w) {
                            int prevWp = wp ^ (wi % 2);
                            int prevVp = vp ^ (vi % 2);
                            if (dp[i - 1][w - wi][prevWp][prevVp] != -1) {
                                int candidate = dp[i - 1][w - wi][prevWp][prevVp] + vi;
                                if (candidate > dp[i][w][wp][vp])
                                    dp[i][w][wp][vp] = candidate;
                            }
                        }
                    }
                }
            }
        }

        // Find best valid solution: odd total weight (wp=1) and even total value (vp=0)
        int bestValue = -1;
        int bestW = -1;
        for (int w = 0; w <= capacity; w++) {
            if (dp[n][w][1][0] > bestValue) {
                bestValue = dp[n][w][1][0];
                bestW = w;
            }
        }

        String separator = new String(new char[44]).replace('\0', '=');
        System.out.println("Done!");
        System.out.println("Result:");
        System.out.println(separator);

        if (bestValue == -1) {
            System.out.println("No valid solution exists for the given input data.");
            System.out.println(separator);
            System.out.println("Outputting dynamic_table.txt...");
            System.out.println("Done!");
            System.out.println("End of Processing.");
            writeDynamicTable(dp, n, capacity);
            scanner.close();
            return;
        }

        // Backtrack to find chosen items
        // At each item i: if dp[i][w][wp][vp] != dp[i-1][w][wp][vp], item i was taken
        List<String> chosen = new ArrayList<>();
        int w = bestW;
        int wp = 1, vp = 0;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w][wp][vp] != dp[i - 1][w][wp][vp]) {
                chosen.add(ids.get(i - 1));
                w -= weights.get(i - 1);
                wp ^= (weights.get(i - 1) % 2);
                vp ^= (values.get(i - 1) % 2);
            }
        }
        Collections.reverse(chosen);

        String itemListStr = chosen.isEmpty() ? "(none)" : String.join(", ", chosen);
        System.out.println("Total Value: " + bestValue);
        System.out.println("Item ID List: " + itemListStr);
        System.out.println(separator);
        System.out.println("Outputting dynamic_table.txt...");
        System.out.println("Done!");
        System.out.println("End of Processing.");

        writeDynamicTable(dp, n, capacity);
        scanner.close();
    }

    // Writes all 4 parity slices of the DP table to dynamic_table.txt
    // Each slice is a 2D table (rows = items 0..n, cols = capacity 0..W)
    // -1 means that parity state is unreachable with those items/capacity
    private static void writeDynamicTable(int[][][][] dp, int n, int capacity) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter("dynamic_table.txt"));

        int[][] parities = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
        String[] labels = {
                "wp=0 vp=0 (even weight, even value):",
                "wp=0 vp=1 (even weight, odd value):",
                "wp=1 vp=0 (odd weight, even value) [target]:",
                "wp=1 vp=1 (odd weight, odd value):"
        };

        for (int p = 0; p < 4; p++) {
            int wp = parities[p][0];
            int vp = parities[p][1];
            writer.println(labels[p]);
            for (int i = 0; i <= n; i++) {
                for (int w = 0; w <= capacity; w++) {
                    if (w > 0)
                        writer.print(" ");
                    writer.print(dp[i][w][wp][vp]);
                }
                writer.println();
            }
            if (p < 3)
                writer.println();
        }

        writer.close();
    }
}
