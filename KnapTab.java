import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class KnapTab {
    public static void main(String[] args) throws IOException {
        System.out.println("0/1 Knapsack Solver:");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter the data file name: ");
        String filename = scanner.nextLine().trim();

        System.out.println("Processing...");

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
        List<Integer> idsList = new ArrayList<>(); // Creating an empty list to store the ID of the items
        List<Integer> weightsList = new ArrayList<>(); // Creating an empty list to store the weight of each item
        List<Integer> valuesList = new ArrayList<>(); // Creating an empty list to store the profit of each item

        for (int i = 1; i < lines.size(); i++) { // Starting at the second line and looping through every line
            String[] parts = lines.get(i).split("\\s+"); // Splitting the raw data into sections where the spaces are
            if (parts.length != 3)
                throw new IllegalArgumentException("Illegal format at line "+(i+1)+"\n"+"Expected: id weight value"); // Invalid line format throws exception
            idsList.add(Integer.parseInt(parts[0])); // Adding the item stored in part 0 of the sentence to IDS
            weightsList.add(Integer.parseInt(parts[1])); // Adding the item stored in part 1 of the sentence to weights
            valuesList.add(Integer.parseInt(parts[2])); // Adding the item stored in part 2 of the sentence to values
        }  

        //Converting the lists to arrays to put in method
        int n = idsList.size();
        int[] ids = new int[n];
        int[] weights = new int[n];
        int[] values = new int[n];

        for(int i = 0; i < n; i++){
            ids[i]= idsList.get(i);
            weights[i] = weightsList.get(i);
            values[i] = valuesList.get(i);
        }

        //Calling knapsack method to build DP table
        int[][] table = knapsack(weights, values, capacity);

        //Printing according to requirements
        System.out.println("Done!");
        System.out.println("Result:");
        System.out.println("============================================");
        System.out.println("Total value: " +result(table, capacity, weights.length));
        System.out.println("Selected ID List: " +findItems(table, weights, ids, capacity));
        System.out.println("============================================");
        
        System.out.println("Outputting dynamic_table.txt");
        PrintWriter output = new PrintWriter(new FileWriter("dynamic_table.txt"));
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                output.print(table[i][j] + "\t");
            }
            output.println();
        }
        output.flush();
        output.close();
        System.out.println("Done!");
        System.out.println("End of processing.");
        
    }

    //Build the Knapsack DP table and returns a 2D array.
    public static int[][] knapsack(int[] weight, int[] value, int capacity){
        int n = weight.length;
        int[][] table = new int[n+1][capacity+1];

        for (int i = 1; i <= n ; i++){
            for (int j = 0; j <= capacity ; j++){
                if (weight[i-1] > j) {
                    table[i][j] = table[i-1][j];
                } else {
                    table[i][j] = Math.max(table[i-1][j], value[i-1] + table[i-1][j-weight[i-1]]);
                }
            }
        }

        return table;
    }

    // Private method to printTable I tested before hardcoding for output
    private static void printTable(int[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
    }

    // Method which returns a string of selected items from the DP table using weights and ids arrays
    public static String findItems(int[][] table, int[] weight, int[] ids, int capacity) {
        int n = weight.length;
        int w = capacity;

        List<Integer> selected = new ArrayList<>();

        for (int i = n; i > 0; i--) {
            if (table[i][w] != table[i - 1][w]) {
                selected.add(ids[i - 1]);   // store actual ID
                w -= weight[i - 1];         // reduce capacity
            }
        }

        // Reverse list because we traced backwards
        Collections.reverse(selected);

        // Build output string
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < selected.size(); i++) {
            sb.append(selected.get(i));
            if (i < selected.size() - 1) {
                sb.append(", ");
            }
        }

        return sb.toString();
}

    //Returns max possible value in knapsack table
    public static int result(int[][] table, int capacity, int n){
        return table[n][capacity];
    }
}
