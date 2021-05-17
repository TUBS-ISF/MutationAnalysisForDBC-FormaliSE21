// This is a mutant program.
// Author : ysma

public class Debug2
{

/*@
@ normal_behavior
@ requires true; 
@ ensures \result>=0 ==> a[\result]==x;
@*/
    public static  int linearSearch( int[] a, int x )
    {
        int i = a.length - 1;
/*@ loop_invariant !(\exists int q; q >= i+1 && q < a.length; a[q]==x) && i>=-1 && i<a.length;
		@ decreases i+1;
		  @*/
        while (i >= 0 && a[i] != x) {
            i = i - 1;
        }
        return i;
    }

/*@
@ normal_behavior
@ requires A.length > 0;
@ ensures (\forall int q; q >= 0 & q < A.length; A[\result]>=A[q]);
@*/
    public static  int maxElement( int[] A )
    {
        int i = 0;
        int j = 1;
/*@ loop_invariant (\forall int q; q >= 0 && q < j; A[i]>=A[q]) && i>=0 && i<A.length && j>0 && j<=A.length;
		@ decreases A.length - j;
		  @*/
        while (j != A.length) {
            if (A[j] > A[i]) {
                i = j;
            } else {
                if (A[j] <= A[i]) {
                    ;
                }
            }
            j = j + 1;
        }
        return i;
    }

/*@
@ normal_behavior
@ requires n >= 0 && n<6;
@ ensures \result==Helper.factorial(n);
@*/
    public static  int fac( int n )
    {
        int f = 0;
        if (n == 0) {
            f = 1;
        } else {
            if (n == 1) {
                f = 1;
            } else {
                if (n >= 2) {
                    int tmp = n - 1;
                    f = n * Helper.factorial( tmp );
                }
            }
        }
        return f;
    }

/*@
	@ normal_behavior
	@ requires A.length > 0 && (\forall int i; i>=0 & i<A.length; A[i] == 0 || A[i] == 1 || A[i] == 2);
	@ ensures (\forall int q; q >= 1 && q < \result.length; \result[q-1]<=\result[q]);
	@*/
    public static  int[] DutchFlag( int[] A )
    {
        int wb = 0;
        int wt = 0;
        int bb = A.length;
        return A;
    }

}
