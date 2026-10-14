package fleetflow.m6;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.ToLongFunction;

/** M6: Randomized algorithms and parallel aggregation for logistics data. */
public final class RandomParallelAlgorithms {
    private RandomParallelAlgorithms() {}
    public static <T> void randomizedQuickSort(T[] a,Comparator<? super T> cmp,Random random){quick(a,0,a.length-1,cmp,random);}
    private static <T> void quick(T[]a,int lo,int hi,Comparator<? super T> c,Random r){if(lo>=hi)return;int p=lo+r.nextInt(hi-lo+1);T pivot=a[p];int i=lo,j=hi;while(i<=j){while(c.compare(a[i],pivot)<0)i++;while(c.compare(a[j],pivot)>0)j--;if(i<=j){T t=a[i];a[i++]=a[j];a[j--]=t;}}if(lo<j)quick(a,lo,j,c,r);if(i<hi)quick(a,i,hi,c,r);}
    /** Uniform sample of k items from a stream, O(k) memory. */
    public static <T> List<T> reservoirSample(Iterable<T> stream,int k,Random random){if(k<0)throw new IllegalArgumentException("k must be nonnegative");List<T> sample=new ArrayList<>(k);long i=0;for(T item:stream){if(i<k)sample.add(item);else if(k>0){long j=nextLongBounded(random,i+1);if(j<k)sample.set((int)j,item);}i++;}return sample;}
    private static long nextLongBounded(Random r,long bound){long x;do{x=r.nextLong()>>>1;}while(x-(x%bound)+(bound-1)<0);return x%bound;}
    public static boolean millerRabin(long n,int rounds,Random random){if(n<2)return false;for(int p:new int[]{2,3,5,7,11,13,17,19,23,29,31,37}){if(n%p==0)return n==p;}long d=n-1;int s=0;while((d&1)==0){d>>=1;s++;}BigInteger N=BigInteger.valueOf(n), nm1=N.subtract(BigInteger.ONE);for(int i=0;i<rounds;i++){long raw=2+Math.floorMod(random.nextLong(),n-3);BigInteger a=BigInteger.valueOf(raw);BigInteger x=a.modPow(BigInteger.valueOf(d),N);if(x.equals(BigInteger.ONE)||x.equals(nm1))continue;boolean witness=true;for(int r=1;r<s;r++){x=x.multiply(x).mod(N);if(x.equals(nm1)){witness=false;break;}}if(witness)return false;}return true;}
    /** Exclusive prefix sum (Blelloch scan concept); returns length n+1 with leading zero. */
    public static long[] blellochScan(long[] input){int n=input.length,size=1;while(size<n)size<<=1;long[] a=new long[size];System.arraycopy(input,0,a,0,n);for(int d=1;d<size;d<<=1)for(int i=0;i<size;i+=d<<1)a[i+(d<<1)-1]+=a[i+d-1];if(size>0)a[size-1]=0;for(int d=size>>1;d>=1;d>>=1)for(int i=0;i<size;i+=d<<1){long t=a[i+d-1];a[i+d-1]=a[i+(d<<1)-1];a[i+(d<<1)-1]+=t;}return Arrays.copyOf(a,n);}
    public static long parallelReduce(long[] values,int threads){if(threads<1)throw new IllegalArgumentException("threads must be >= 1");if(values.length==0)return 0;ExecutorService pool=Executors.newFixedThreadPool(threads);try{List<Future<Long>> fs=new ArrayList<>();int chunk=(values.length+threads-1)/threads;for(int t=0;t<threads;t++){final int from=t*chunk,to=Math.min(values.length,from+chunk);if(from>=to)break;fs.add(pool.submit(()->{long sum=0;for(int i=from;i<to;i++)sum+=values[i];return sum;}));}long total=0;for(Future<Long> f:fs)total+=f.get();return total;}catch(Exception e){throw new RuntimeException("Parallel reduction failed",e);}finally{pool.shutdown();}}
    /** Brent's theorem lower bound: T_P >= max(T_1/P, T_infinity). */
    public static double brentLowerBound(double totalWork,double criticalPath,int processors){if(totalWork<0||criticalPath<0||processors<1)throw new IllegalArgumentException();return Math.max(totalWork/processors,criticalPath);}
}
