package fleetflow.app;

import fleetflow.m1.StringAlgorithms;
import fleetflow.m2.SuffixStructures;
import fleetflow.m3.OptimizationDP;
import fleetflow.m4.FlowAlgorithms;
import fleetflow.m5.ConstraintAlgorithms;
import fleetflow.m6.RandomParallelAlgorithms;
import java.util.*;

/** Run this class to see a small demonstration of every FleetFlow module. */
public class FleetFlowDemo {
    public static void main(String[] args) {
        System.out.println("=== FleetFlow: Intelligent Logistics Management Platform ===");
        System.out.println("\nM1 String algorithms");
        System.out.println("KMP shipment ID offset: " + StringAlgorithms.kmpIndexOf("PKG-2026-AB12", "AB12"));
        System.out.println("Z function: " + Arrays.toString(StringAlgorithms.zFunction("ABABAB")));
        System.out.println("Rabin-Karp package matches: " + StringAlgorithms.rabinKarp("PKG-A PKG-B PKG-A", "PKG-A"));
        System.out.println("Aho-Corasick statuses: " + StringAlgorithms.ahoCorasick("Loaded delayed delivered", Arrays.asList("loaded","delayed","delivered")));

        System.out.println("\nM2 Suffix structures");
        String docs="dock address dock shipment"; int[] sa=SuffixStructures.suffixArray(docs);
        System.out.println("Suffix array: " + Arrays.toString(sa));
        System.out.println("LCP/Kasai: " + Arrays.toString(SuffixStructures.lcpKasai(docs,sa)));
        System.out.println("Substring 'dock' at: " + SuffixStructures.findSubstring(docs,"dock"));
        System.out.println("Longest repeated substring: " + SuffixStructures.longestRepeatedSubstring(docs));
        System.out.println("SA-IS-compatible entry point: " + Arrays.toString(SuffixStructures.sais("fleet")));

        System.out.println("\nM3 Dynamic programming");
        System.out.println("Address edit distance: " + OptimizationDP.levenshtein("12 Main Street","12 Mian Street"));
        System.out.println("Damerau-Levenshtein: " + OptimizationDP.damerauLevenshtein("Main","Mian"));
        System.out.println("Small route cycle cost: " + OptimizationDP.bitmaskTsp(new int[][]{{0,10,15},{10,0,20},{15,20,0}}));
        System.out.println("Matrix-chain cost: " + OptimizationDP.matrixChainCost(new int[]{10,30,5,60}));
        System.out.println("Optimal BST expected cost: " + OptimizationDP.optimalBst(new double[]{.3,.2},new double[]{.1,.1,.3}));

        System.out.println("\nM4 Network flow and matching");
        FlowAlgorithms.Network net=new FlowAlgorithms.Network(4);net.addEdge(0,1,3);net.addEdge(0,2,2);net.addEdge(1,2,1);net.addEdge(1,3,2);net.addEdge(2,3,3);
        System.out.println("Dinic maximum flow: " + net.dinic(0,3));
        List<List<Integer>> drivers=Arrays.asList(Arrays.asList(0,1),Arrays.asList(0),Arrays.asList(1));
        System.out.println("Driver-to-task matching: " + Arrays.toString(FlowAlgorithms.bipartiteMatching(drivers,2)));
        System.out.println("Min-cut source side: " + Arrays.toString(net.minCutSourceSide(0)));

        System.out.println("\nM5 Constraints and approximation");
        boolean[] assignment=ConstraintAlgorithms.solveSat(2,new int[][]{{1,2},{-1,2},{1,-2}});
        System.out.println("SAT assignment: " + Arrays.toString(assignment));
        boolean[][] clique=ConstraintAlgorithms.threeSatToClique(new int[][]{{1,-2,3},{-1,2,4}});
        System.out.println("3-SAT reduction graph vertices: " + clique.length + "; complement vertices: " + ConstraintAlgorithms.complement(clique).length);
        int[][] conflicts={{0,1},{1,2},{2,3},{0,3}};Set<Integer> cover=ConstraintAlgorithms.vertexCover2Approx(4,conflicts);
        System.out.println("2-approx vertex cover: " + cover + "; valid=" + ConstraintAlgorithms.isVertexCover(4,conflicts,cover));

        System.out.println("\nM6 Randomized and parallel algorithms");
        Random random=new Random(42);Integer[] ids={42,7,19,7,100,3};RandomParallelAlgorithms.randomizedQuickSort(ids,Integer::compare,random);
        System.out.println("Sorted shipment ranks: " + Arrays.toString(ids));
        System.out.println("Reservoir sample: " + RandomParallelAlgorithms.reservoirSample(Arrays.asList("EV1","EV2","EV3","EV4"),2,random));
        System.out.println("Miller-Rabin 1,000,003 prime? " + RandomParallelAlgorithms.millerRabin(1_000_003L,8,random));
        System.out.println("Blelloch exclusive scan: " + Arrays.toString(RandomParallelAlgorithms.blellochScan(new long[]{3,1,4,2})));
        System.out.println("Parallel shipment sum: " + RandomParallelAlgorithms.parallelReduce(new long[]{10,20,30,40},2));
        System.out.println("Brent lower bound: " + RandomParallelAlgorithms.brentLowerBound(1000,120,8));
        System.out.println("\nDemo complete. See README.md for algorithm notes and limitations.");
    }
}
