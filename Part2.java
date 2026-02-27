import java.io.*;
import java.util.*;

/*
 * CPSC 371 Assignment 1, Part II: General (Unbounded) Knapsack Problem
 * Each item can be selected any number of times.
 */
public class Part2 {

    // Simple struct to hold item data
    static class Item {
        int id, weight, price;
        Item(int id, int weight, int price) {
            this.id = id;
            this.weight = weight;
            this.price = price;
        }
    }

    /**
     * Read knapsack data from file.
     * Line 1: capacity
     * Lines 2+: itemID weight price (space or tab separated)
     */
    static int capacity;
    static List<Item> items = new ArrayList<>();

    static void readData(String filepath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filepath));
        String line;

        // First non-empty line = capacity
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                capacity = Integer.parseInt(line);
                break;
            }
        }

        // Remaining lines = items
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s+");
            int id     = Integer.parseInt(parts[0]);
            int weight = Integer.parseInt(parts[1]);
            int price  = Integer.parseInt(parts[2]);
            items.add(new Item(id, weight, price));
        }
        br.close();
    }

    /**
      Solve using 1D DP (unbounded knapsack).
      dp[w]     = max value achievable with knapsack capacity w
      choice[w] = index of item last chosen to achieve dp[w] (-1 if none)
      Recurrence:
        for each w from 1..W:
          for each item i:
            if item.weight <= w and dp[w - item.weight] + item.price > dp[w]:
              dp[w] = dp[w - item.weight] + item.price
              choice[w] = i
     */
    static int[] dp;
    static int[] choice;

    static void solve() {
        int W = capacity;
        dp     = new int[W + 1];
        choice = new int[W + 1];
        Arrays.fill(choice, -1);

        for (int w = 1; w <= W; w++) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                if (item.weight <= w) {
                    int val = dp[w - item.weight] + item.price;
                    if (val > dp[w]) {
                        dp[w]     = val;
                        choice[w] = i;
                    }
                }
            }
        }
    }

    /*
    Backtrack through choice[] to find which items were selected.
    Since items are unbounded, the same item can appear multiple times.
     */
    static List<Integer> backtrack() {
        List<Integer> selected = new ArrayList<>();
        int w = capacity;
        while (w > 0 && choice[w] != -1) {
            Item item = items.get(choice[w]);
            selected.add(item.id);
            w -= item.weight;
        }
        return selected;
    }

    // Write the DP table to dynamic_table.txt
    static void writeDynamicTable(String filename) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(filename));
        pw.println("General (Unbounded) Knapsack Problem - DP Table");
        pw.println();
        pw.println("Index (capacity) : Max Value");
        for (int w = 0; w <= capacity; w++) {
            pw.printf("  dp[%3d] = %d%n", w, dp[w]);
        }
        pw.close();
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.println("General (Unbounded) Knapsack Problem");
        System.out.print("Please enter the data file name: ");
        String filepath = scanner.nextLine().trim();

        File file = new File(filepath);
        if (!file.exists()) {
            System.out.println("Error: File '" + filepath + "' not found.");
            return;
        }

        System.out.println("Processing...");
        readData(filepath);
        solve();
        List<Integer> selectedIds = backtrack();
        int totalValue = dp[capacity];

        System.out.println("Done!");
        System.out.println("\nResult:");
        System.out.println("============================================");
        System.out.println("Total Value: " + totalValue);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < selectedIds.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(selectedIds.get(i));
        }
        System.out.println("Item ID List: " + sb);
        System.out.println("============================================");

        System.out.println("\nOutputting dynamic_table.txt...");
        writeDynamicTable("dynamic_table.txt");
        System.out.println("Done!");

        System.out.println("\nEnd of Processing.");
        scanner.close();
    }
}
