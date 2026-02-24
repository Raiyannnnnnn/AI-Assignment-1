public class KnapTab {
    public static void main(String[] args) {
        int[] weight = { 2, 5, 3 ,4, 1};
        int[] value = { 15, 14, 10, 22, 19};
        int capacity = 9;

        var table = knapsack(weight, value, capacity);
        printTable(table);
        System.out.println(" Max value = " + table[weight.length][capacity]);
        var selectedItems = findItems(table, weight, capacity);
        System.out.println("Selected items (1 for selected, 0 for not selected):");
        for (int i = 0; i < selectedItems.length; i++) {
            System.out.println("Item " + (i + 1) + ": " + selectedItems[i]);
        }  
    }

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

    public static void printTable(int[][] table) {
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[i].length; j++) {
                System.out.print(table[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static int[] findItems(int[][] table, int[] weight, int capacity) {
        int n = weight.length;
        int[] selectedItems = new int[n];
        int w = capacity;

        for (int i = n; i > 0; i--) {
            if (table[i][w] != table[i-1][w]) {
                selectedItems[i-1] = 1; // Mark item as selected
                w -= weight[i-1]; // Reduce remaining capacity
            }
        }

        return selectedItems;
    }
}
