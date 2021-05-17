package openjml;
public class SumAndMax {
	
	/*@ requires a != null;
	  @ ensures \result == (\sum int i; 0<=i && i < a.length; a[i]); 
	  @*/
	static public int sum(int[] a) {
		int sum = 0;
		//@ loop_invariant sum == (\sum int j; 0<=j && j<a.length; a[j]);
		for (int i: a) {
			sum += i;
		}
		return sum;
	}
	
	/*@ requires a != null && a.length > 1;
	@ ensures \result == (\max int i; 0<=i && i < a.length; a[i]); // FIXME - cannot reason about \max
	@ ensures (\forall int i; 0<=i && i < a.length; a[i] <= \result);
	@ ensures (\exists int i; 0<=i && i < a.length; a[i] == \result);
	  @*/
	static public int max(int[] a) {
		int max = a[0];
		//@ loop_invariant max == (\max int j; 0<=j && j<a.length; a[j]);
		for (int i: a) {
			if (max < i) max = i;
		}
		return max;
	}
	
	
}