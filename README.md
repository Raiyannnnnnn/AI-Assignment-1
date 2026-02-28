# CPSC 371 Assignment 1 — Knapsack Problem

## Overview

This assignment contains three Java programs, each solving a variation of the knapsack problem using dynamic programming.

| File | Problem |
|------|---------|
| `Part_1.java` | 0-1 Knapsack — each item used at most once |
| `Part2.java` | General (Unbounded) Knapsack — each item can be used any number of times |
| `Part3.java` | 0-1 Knapsack with Constraints — total weight must be odd, total value must be even |

---

## Input File Format

All three parts read from a `.txt` file with the following format:

```
<capacity>
<itemID> <weight> <price>
<itemID> <weight> <price>
...
```

- The first line is a single integer representing the knapsack capacity.
- Each subsequent line defines one item with three fields separated by spaces or tabs.

**Example (`data.txt`):**
```
5
1 2 3
2 3 4
3 4 5
4 5 6
```

---

## How to Compile and Run

Make sure you have Java installed. Open a terminal in the project directory and run the commands below.

### Part I — 0-1 Knapsack

```bash
javac Part_1.java
java Part_1
```

When prompted, enter the path to your data file:
```
Please enter the data file name: data.txt
```

---

### Part II — General (Unbounded) Knapsack

```bash
javac Part2.java
java Part2
```

When prompted, enter the path to your data file:
```
Please enter the data file name: data.txt
```

---

### Part III — 0-1 Knapsack with Constraints

```bash
javac Part3.java
java Part3
```

When prompted, enter the path to your data file:
```
Please enter the data file name: data.txt
```

---

## Output

Each program produces two outputs:

**Console** — displays the result in this format:
```
<Problem Title>
Please enter the data file name: data.txt
Processing...
Done!
Result:
============================================
Total Value: 7
Item ID List: 1, 2
============================================
Outputting dynamic_table.txt...
Done!
End of Processing.
```

**`dynamic_table.txt`** — written to the directory where you run the program:
- **Part I:** Full 2D DP table (rows = items 0 to n, columns = capacity 0 to W)
- **Part II:** Full 2D DP table using the unbounded recurrence
- **Part III:** All 4 parity-combination tables (even/odd weight × even/odd value), with the target constraint table `(odd weight, even value)` clearly labeled

### Part III — No Valid Solution

If no combination of items satisfies both constraints simultaneously, the console will display:
```
No valid solution exists for the given input data.
```
The DP table is still written to `dynamic_table.txt`.

---

## Requirements

- Java 8 or higher
- Data file must be in the same directory as the program, or provide the full path when prompted
