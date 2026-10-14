package fleetflow.m3;

import java.util.*;

/** M3: Dynamic programming for address correction, small routing and lookup planning. */
public final class OptimizationDP {
    private OptimizationDP() {}
    public static int levenshtein(String a,String b){int[][] d=new int[a.length()+1][b.length()+1];for(int i=0;i<=a.length();i++)d[i][0]=i;for(int j=0;j<=b.length();j++)d[0][j]=j;for(int i=1;i<=a.length();i++)for(int j=1;j<=b.length();j++)d[i][j]=Math.min(Math.min(d[i-1][j]+1,d[i][j-1]+1),d[i-1][j-1]+(a.charAt(i-1)==b.charAt(j-1)?0:1));return d[a.length()][b.length()];}
    public static int damerauLevenshtein(String a,String b){int n=a.length(),m=b.length(),max=n+m;int[][] d=new int[n+2][m+2];d[0][0]=max;for(int i=0;i<=n;i++){d[i+1][0]=max;d[i+1][1]=i;}for(int j=0;j<=m;j++){d[0][j+1]=max;d[1][j+1]=j;}Map<Character,Integer> last=new HashMap<>();for(int i=1;i<=n;i++){int db=0;for(int j=1;j<=m;j++){int i1=last.getOrDefault(b.charAt(j-1),0),j1=db,cost=1;if(a.charAt(i-1)==b.charAt(j-1)){cost=0;db=j;}d[i+1][j+1]=Math.min(Math.min(d[i][j]+cost,Math.min(d[i+1][j]+1,d[i][j+1]+1)),d[i1][j1]+(i-i1-1)+1+(j-j1-1));}last.put(a.charAt(i-1),i);}return d[n+1][m+1];}
    /** Minimum Hamiltonian cycle cost; dist[i][j], start vertex 0. n should be <= 20. */
    public static int bitmaskTsp(int[][] dist){int n=dist.length;if(n==0)return 0;if(n>20)throw new IllegalArgumentException("Bitmask DP supports at most 20 stops");int size=1<<n,inf=Integer.MAX_VALUE/4;int[][] dp=new int[size][n];for(int[] row:dp)Arrays.fill(row,inf);dp[1][0]=0;for(int mask=1;mask<size;mask++){if((mask&1)==0)continue;for(int u=0;u<n;u++)if(dp[mask][u]<inf)for(int v=0;v<n;v++)if((mask&(1<<v))==0){int nm=mask|1<<v;dp[nm][v]=Math.min(dp[nm][v],dp[mask][u]+dist[u][v]);}}int ans=inf;for(int u=1;u<n;u++)ans=Math.min(ans,dp[size-1][u]+dist[u][0]);return n==1?0:ans;}
    public static long matrixChainCost(int[] dims){int n=dims.length-1;if(n<=1)return 0;long[][] dp=new long[n][n];for(int len=2;len<=n;len++)for(int i=0;i+len<=n;i++){int j=i+len-1;dp[i][j]=Long.MAX_VALUE;for(int k=i;k<j;k++)dp[i][j]=Math.min(dp[i][j],dp[i][k]+dp[k+1][j]+(long)dims[i]*dims[k+1]*dims[j+1]);}return dp[0][n-1];}
    /** Optimal BST expected search cost. p are successful-search weights; q unsuccessful weights. */
    public static double optimalBst(double[] p,double[] q){int n=p.length;if(q.length!=n+1)throw new IllegalArgumentException("q must have p.length+1 entries");double[][] e=new double[n+2][n+1],w=new double[n+2][n+1];for(int i=1;i<=n+1;i++){e[i][i-1]=q[i-1];w[i][i-1]=q[i-1];}for(int len=1;len<=n;len++)for(int i=1;i<=n-len+1;i++){int j=i+len-1;e[i][j]=Double.POSITIVE_INFINITY;w[i][j]=w[i][j-1]+p[j-1]+q[j];for(int r=i;r<=j;r++)e[i][j]=Math.min(e[i][j],e[i][r-1]+e[r+1][j]+w[i][j]);}return e[1][n];}
}
