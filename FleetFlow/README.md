# FleetFlow – Intelligent Logistics Management Platform

Java/Eclipse starter project demonstrating the six requested DSA modules. The code uses only the Java standard library (Java 8+).

## Modules

- **M1 – String algorithms:** KMP shipment-ID search, Z-function, Rabin–Karp package-code search, Aho–Corasick multi-status matching.
- **M2 – Suffix structures:** suffix array, Kasai LCP, substring search and longest repeated substring. `sais` is currently a compatible entry point backed by prefix-doubling; it is **not** a linear-time SA-IS implementation. `suffixTreeContains` exposes suffix-tree-style query behavior using the suffix array, not a pointer-based suffix tree.
- **M3 – Dynamic programming:** Levenshtein and Damerau–Levenshtein address correction, bitmask TSP for small route sets, matrix-chain multiplication, optimal BST.
- **M4 – Network flow:** Ford–Fulkerson (DFS augmenting paths), Edmonds–Karp (BFS), Dinic, residual min-cut, bipartite matching.
- **M5 – NP-completeness and approximation:** brute-force SAT solver for small instances, 3-SAT-to-CLIQUE graph construction, graph complement for CLIQUE/INDEPENDENT-SET relationships, vertex-cover conversion and 2-approximation.
- **M6 – Randomized and parallel algorithms:** randomized QuickSort, reservoir sampling, Miller–Rabin primality test, Blelloch exclusive scan, parallel reduction and Brent lower bound.

## Project structure

```text
FleetFlow/
  README.md
  src/fleetflow/app/FleetFlowDemo.java
  src/fleetflow/m1/StringAlgorithms.java
  src/fleetflow/m2/SuffixStructures.java
  src/fleetflow/m3/OptimizationDP.java
  src/fleetflow/m4/FlowAlgorithms.java
  src/fleetflow/m5/ConstraintAlgorithms.java
  src/fleetflow/m6/RandomParallelAlgorithms.java
```

## Run in Eclipse

1. Extract the ZIP.
2. In Eclipse choose **File → Import → Existing Projects into Workspace** and select the extracted `FleetFlow` folder. If Eclipse does not recognize it, choose **File → New → Java Project**, name it `FleetFlow`, and copy the `src` folder into it.
3. Open `src/fleetflow/app/FleetFlowDemo.java`.
4. Right-click → **Run As → Java Application**.

Alternatively, from the project root run:

```bash
javac -d out $(find src -name '*.java')
java -cp out fleetflow.app.FleetFlowDemo
```

On Windows Command Prompt, compile with `javac -d out` followed by the listed Java source paths, or run from Eclipse.

## Important notes

- This is an algorithm demonstration, not a production-ready logistics backend. It does not yet connect to a database, maps/geocoding provider, fleet telematics, or a web API.
- The TSP bitmask DP is exponential and deliberately limited to 20 stops; practical routing needs heuristics/optimization services for larger cases.
- SAT brute force is exponential and limited to 25 variables for demonstrations.
- Max-flow implementations mutate the residual graph; create a fresh `Network` for each algorithm comparison.
- Miller–Rabin uses `BigInteger` modular arithmetic to avoid overflow in modular multiplication. It is probabilistic for general inputs; the fixed small-prime check handles common cases.
- The reservoir sampler and randomized sort accept a `Random` instance so tests can be repeatable with a fixed seed.
