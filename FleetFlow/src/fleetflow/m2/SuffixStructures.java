package fleetflow.m2;

import java.util.*;

/** M2: Suffix-array based document indexing, LCP/Kasai and substring queries. */
public final class SuffixStructures {
    private SuffixStructures() {}
    /** Prefix-doubling suffix array, O(n log^2 n) with comparator sorting. */
    public static int[] suffixArray(String s) {
        int n=s.length(); Integer[] sa=new Integer[n]; int[] rank=new int[n], next=new int[n];
        for(int i=0;i<n;i++){sa[i]=i;rank[i]=s.charAt(i);}
        for(int k=1;k<n;k*=2){final int step=k;final int[] rr=rank;Arrays.sort(sa,(a,b)->{if(rr[a]!=rr[b])return Integer.compare(rr[a],rr[b]);int ra=a+step<n?rr[a+step]:-1, rb=b+step<n?rr[b+step]:-1;return Integer.compare(ra,rb);});
            next[sa[0]]=0;for(int i=1;i<n;i++){int a=sa[i-1],b=sa[i];boolean diff=rank[a]!=rank[b]||(a+k<n?rank[a+k]:-1)!=(b+k<n?rank[b+k]:-1);next[b]=next[a]+(diff?1:0);}System.arraycopy(next,0,rank,0,n);if(n==0||rank[sa[n-1]]==n-1)break;if(k>n/2)break;}
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray();
    }
    /** Kasai LCP array: lcp[i] = LCP(sa[i], sa[i-1]); lcp[0]=0. */
    public static int[] lcpKasai(String s,int[] sa){int n=s.length();int[] rank=new int[n],lcp=new int[n];for(int i=0;i<n;i++)rank[sa[i]]=i;int h=0;for(int i=0;i<n;i++){int r=rank[i];if(r==0)continue;int j=sa[r-1];while(i+h<n&&j+h<n&&s.charAt(i+h)==s.charAt(j+h))h++;lcp[r]=h;if(h>0)h--;}return lcp;}
    public static List<Integer> findSubstring(String text,String pattern){List<Integer> out=new ArrayList<>();if(pattern.isEmpty()){for(int i=0;i<=text.length();i++)out.add(i);return out;}int[] sa=suffixArray(text);for(int pos:sa)if(pos+pattern.length()<=text.length()&&text.startsWith(pattern,pos))out.add(pos);Collections.sort(out);return out;}
    /** Longest repeated substring using suffix array and LCP. */
    public static String longestRepeatedSubstring(String s){if(s.isEmpty())return "";int[] sa=suffixArray(s),lcp=lcpKasai(s,sa);int best=0,at=0;for(int i=1;i<lcp.length;i++)if(lcp[i]>best){best=lcp[i];at=sa[i];}return s.substring(at,at+best);}
    /** SA-IS entry point for integer alphabets. Uses a robust suffix-array fallback; replace with induced sorting for strict linear-time requirements. */
    public static int[] sais(String text){return suffixArray(text);}
    /** Compact suffix-trie-style query wrapper backed by a suffix array. */
    public static boolean suffixTreeContains(String text,String query){return !findSubstring(text,query).isEmpty();}
}
