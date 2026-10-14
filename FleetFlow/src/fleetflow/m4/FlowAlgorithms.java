package fleetflow.m4;

import java.util.*;

/** M4: Capacity-constrained warehouse/distribution networks and driver-task assignment. */
public final class FlowAlgorithms {
    private FlowAlgorithms() {}
    public static final class Edge { public int to, rev, cap, original; Edge(int t,int r,int c){to=t;rev=r;cap=c;original=c;} }
    public static final class Network {
        public final List<List<Edge>> g; public Network(int n){g=new ArrayList<>();for(int i=0;i<n;i++)g.add(new ArrayList<>());}
        public void addEdge(int u,int v,int cap){if(cap<0)throw new IllegalArgumentException("capacity must be nonnegative");Edge a=new Edge(v,g.get(v).size(),cap),b=new Edge(u,g.get(u).size(),0);g.get(u).add(a);g.get(v).add(b);}
        public int fordFulkerson(int s,int t){int flow=0;int[] parentV=new int[g.size()],parentE=new int[g.size()];while(true){Arrays.fill(parentV,-1);boolean[] seen=new boolean[g.size()];if(!dfs(s,t,seen,parentV,parentE))break;int add=Integer.MAX_VALUE;for(int v=t;v!=s;v=parentV[v])add=Math.min(add,g.get(parentV[v]).get(parentE[v]).cap);for(int v=t;v!=s;v=parentV[v]){Edge e=g.get(parentV[v]).get(parentE[v]);e.cap-=add;g.get(v).get(e.rev).cap+=add;}flow+=add;}return flow;}
        private boolean dfs(int u,int t,boolean[] seen,int[] pv,int[] pe){if(u==t)return true;seen[u]=true;for(int i=0;i<g.get(u).size();i++){Edge e=g.get(u).get(i);if(e.cap>0&&!seen[e.to]){pv[e.to]=u;pe[e.to]=i;if(dfs(e.to,t,seen,pv,pe))return true;}}return false;}
        public int edmondsKarp(int s,int t){int flow=0,n=g.size();while(true){int[] pv=new int[n],pe=new int[n];Arrays.fill(pv,-1);Queue<Integer> q=new ArrayDeque<>();q.add(s);pv[s]=s;while(!q.isEmpty()&&pv[t]<0){int u=q.remove();for(int i=0;i<g.get(u).size();i++){Edge e=g.get(u).get(i);if(e.cap>0&&pv[e.to]<0){pv[e.to]=u;pe[e.to]=i;q.add(e.to);}}}if(pv[t]<0)return flow;int add=Integer.MAX_VALUE;for(int v=t;v!=s;v=pv[v])add=Math.min(add,g.get(pv[v]).get(pe[v]).cap);for(int v=t;v!=s;v=pv[v]){Edge e=g.get(pv[v]).get(pe[v]);e.cap-=add;g.get(v).get(e.rev).cap+=add;}flow+=add;}}
        public int dinic(int s,int t){int flow=0,n=g.size();int[] level=new int[n],it=new int[n];while(bfsLevel(s,t,level)){Arrays.fill(it,0);int f;while((f=send(s,t,Integer.MAX_VALUE,level,it))>0)flow+=f;}return flow;}
        private boolean bfsLevel(int s,int t,int[] level){Arrays.fill(level,-1);Queue<Integer>q=new ArrayDeque<>();q.add(s);level[s]=0;while(!q.isEmpty()){int u=q.remove();for(Edge e:g.get(u))if(e.cap>0&&level[e.to]<0){level[e.to]=level[u]+1;q.add(e.to);}}return level[t]>=0;}
        private int send(int u,int t,int f,int[] level,int[] it){if(u==t)return f;for(;it[u]<g.get(u).size();it[u]++){Edge e=g.get(u).get(it[u]);if(e.cap>0&&level[e.to]==level[u]+1){int x=send(e.to,t,Math.min(f,e.cap),level,it);if(x>0){e.cap-=x;g.get(e.to).get(e.rev).cap+=x;return x;}}}return 0;}
        /** Vertices reachable from source in residual graph after max-flow = source side of min-cut. */
        public boolean[] minCutSourceSide(int s){boolean[] seen=new boolean[g.size()];Queue<Integer>q=new ArrayDeque<>();q.add(s);seen[s]=true;while(!q.isEmpty()){int u=q.remove();for(Edge e:g.get(u))if(e.cap>0&&!seen[e.to]){seen[e.to]=true;q.add(e.to);}}return seen;}
    }
    /** Maximum bipartite matching; left vertices [0,left), right vertices [0,right). */
    public static int[] bipartiteMatching(List<List<Integer>> adj,int rightCount){int[] matchR=new int[rightCount];Arrays.fill(matchR,-1);for(int u=0;u<adj.size();u++)augment(u,adj,matchR,new boolean[rightCount]);int[] matchL=new int[adj.size()];Arrays.fill(matchL,-1);for(int v=0;v<rightCount;v++)if(matchR[v]>=0)matchL[matchR[v]]=v;return matchL;}
    private static boolean augment(int u,List<List<Integer>> adj,int[] matchR,boolean[] seen){for(int v:adj.get(u))if(v>=0&&v<matchR.length&&!seen[v]){seen[v]=true;if(matchR[v]<0||augment(matchR[v],adj,matchR,seen)){matchR[v]=u;return true;}}return false;}
}
