package fleetflow.m5;

import java.util.*;

/** M5: Small-instance SAT solving, reductions and approximation for conflict constraints. */
public final class ConstraintAlgorithms {
    private ConstraintAlgorithms() {}
    /** Literal encoding: positive x is +x (1-based); negative x is -x. */
    public static boolean[] solveSat(int variables,int[][] clauses){if(variables<0||variables>25)throw new IllegalArgumentException("brute-force SAT demo supports 0..25 variables");long limit=1L<<variables;for(long mask=0;mask<limit;mask++){boolean ok=true;for(int[] clause:clauses){boolean sat=false;for(int lit:clause){int v=Math.abs(lit)-1;if(v<0||v>=variables)throw new IllegalArgumentException("literal outside variable range");boolean value=((mask>>v)&1)==1;if(lit<0)value=!value;if(value){sat=true;break;}}if(!sat){ok=false;break;}}if(ok){boolean[] a=new boolean[variables];for(int i=0;i<variables;i++)a[i]=((mask>>i)&1)==1;return a;}}return null;}
    /** 3-SAT to CLIQUE: one vertex per literal occurrence; connect vertices from different clauses unless contradictory. */
    public static boolean[][] threeSatToClique(int[][] clauses){int n=0;for(int[] c:clauses){if(c.length!=3)throw new IllegalArgumentException("each clause must have 3 literals");n+=3;}int[] lit=new int[n],clauseId=new int[n];for(int c=0,k=0;c<clauses.length;c++)for(int x:clauses[c]){lit[k]=x;clauseId[k++]=c;}boolean[][] g=new boolean[n][n];for(int i=0;i<n;i++)for(int j=i+1;j<n;j++)if(clauseId[i]!=clauseId[j]&&lit[i]!=-lit[j])g[i][j]=g[j][i]=true;return g;}
    /** Graph complement: CLIQUE in G is INDEPENDENT-SET in complement(G). */
    public static boolean[][] complement(boolean[][] g){int n=g.length;boolean[][] c=new boolean[n][n];for(int i=0;i<n;i++)for(int j=0;j<n;j++)if(i!=j)c[i][j]=!g[i][j];return c;}
    /** Given independent set S, V\S is a vertex cover; given cover C, V\C is independent. */
    public static Set<Integer> complementVertexSet(int vertexCount,Set<Integer> selected){Set<Integer> out=new LinkedHashSet<>();for(int i=0;i<vertexCount;i++)if(!selected.contains(i))out.add(i);return out;}
    /** Standard 2-approximation: choose both endpoints of each uncovered edge. */
    public static Set<Integer> vertexCover2Approx(int n,int[][] edges){Set<Integer> cover=new LinkedHashSet<>();for(int[] e:edges){if(e.length<2)continue;int u=e[0],v=e[1];if(!cover.contains(u)&&!cover.contains(v)){cover.add(u);cover.add(v);}}return cover;}
    public static boolean isVertexCover(int n,int[][] edges,Set<Integer> cover){for(int[] e:edges)if(!cover.contains(e[0])&&!cover.contains(e[1]))return false;return true;}
}
