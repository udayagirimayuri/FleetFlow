package fleetflow.m1;

import java.util.*;

/** M1: String searching for shipment IDs, package codes and event/status keywords. */
public final class StringAlgorithms {
    private StringAlgorithms() {}

    public static int kmpIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = lps(pattern); int j = 0;
        for (int i = 0; i < text.length(); i++) {
            while (j > 0 && text.charAt(i) != pattern.charAt(j)) j = lps[j - 1];
            if (text.charAt(i) == pattern.charAt(j)) j++;
            if (j == pattern.length()) return i - j + 1;
        }
        return -1;
    }
    private static int[] lps(String p) {
        int[] a = new int[p.length()];
        for (int i=1, len=0; i<p.length();) {
            if (p.charAt(i)==p.charAt(len)) a[i++]=++len;
            else if (len>0) len=a[len-1]; else a[i++]=0;
        }
        return a;
    }
    public static int[] zFunction(String s) {
        int n=s.length(); int[] z=new int[n]; int l=0,r=0;
        for(int i=1;i<n;i++) { if(i<r) z[i]=Math.min(r-i,z[i-l]); while(i+z[i]<n && s.charAt(z[i])==s.charAt(i+z[i])) z[i]++; if(i+z[i]>r){l=i;r=i+z[i];} }
        if(n>0) z[0]=n; return z;
    }
    public static List<Integer> rabinKarp(String text, String pattern) {
        List<Integer> hits=new ArrayList<>(); int n=text.length(), m=pattern.length();
        if(m==0){for(int i=0;i<=n;i++)hits.add(i);return hits;} if(m>n)return hits;
        final long mod=1_000_000_007L, base=257; long target=0, window=0, power=1;
        for(int i=0;i<m;i++){target=(target*base+pattern.charAt(i))%mod;window=(window*base+text.charAt(i))%mod;if(i<m-1)power=power*base%mod;}
        for(int i=0;i<=n-m;i++){if(window==target && text.regionMatches(i,pattern,0,m))hits.add(i);if(i<n-m){window=(window-text.charAt(i)*power%mod+mod)%mod;window=(window*base+text.charAt(i+m))%mod;}}
        return hits;
    }
    /** Aho-Corasick multi-pattern search. Result maps each pattern to its start offsets. */
    public static Map<String,List<Integer>> ahoCorasick(String text, Collection<String> patterns) {
        class Node { Map<Character,Integer> next=new HashMap<>(); int fail; List<String> out=new ArrayList<>(); }
        List<Node> trie=new ArrayList<>(); trie.add(new Node());
        for(String p:patterns){if(p.isEmpty())continue;int v=0;for(char c:p.toCharArray()){Integer u=trie.get(v).next.get(c);if(u==null){u=trie.size();trie.get(v).next.put(c,u);trie.add(new Node());}v=u;}trie.get(v).out.add(p);}
        Queue<Integer> q=new ArrayDeque<>(); for(int u:trie.get(0).next.values()){trie.get(u).fail=0;q.add(u);}
        while(!q.isEmpty()){int v=q.remove();for(Map.Entry<Character,Integer> e:trie.get(v).next.entrySet()){char c=e.getKey();int u=e.getValue(), f=trie.get(v).fail;while(f!=0&&!trie.get(f).next.containsKey(c))f=trie.get(f).fail;if(trie.get(f).next.containsKey(c)&&trie.get(f).next.get(c)!=u)f=trie.get(f).next.get(c);trie.get(u).fail=f;trie.get(u).out.addAll(trie.get(f).out);q.add(u);}}
        Map<String,List<Integer>> result=new LinkedHashMap<>();for(String p:patterns)result.putIfAbsent(p,new ArrayList<>());int v=0;
        for(int i=0;i<text.length();i++){char c=text.charAt(i);while(v!=0&&!trie.get(v).next.containsKey(c))v=trie.get(v).fail;v=trie.get(v).next.getOrDefault(c,0);for(String p:trie.get(v).out)result.get(p).add(i-p.length()+1);}
        return result;
    }
}
