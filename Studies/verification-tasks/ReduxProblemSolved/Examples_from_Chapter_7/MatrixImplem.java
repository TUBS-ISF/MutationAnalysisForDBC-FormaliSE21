package Examples_from_Chapter_7;
public class MatrixImplem { // Listing 7.10
	
    //@ public model int[][] matrix;
	
    private int x;
    private int y;
    private int[] matrix_implem;
    /*@ represents matrix \such_that 
      @ (\forall int i; i >= 0 && i < x; 
      @   (\forall int j; j >= 0 && j < y;
      @     matrix[i][j] == matrix_implem[x * j + i])); 
      @*/

    /*@ ensures
      @ (\forall int i; i >= 0 && i < x; 
      @   (\forall int j; j >= 0 && j < y;
      @     matrix[i][j] == 0));
      @*/
    public MatrixImplem(int x, int y) {
        this.x = x;
        this.y = y;
        matrix_implem = new int [x * y];
    }
	
    //@ ensures \result == matrix[i][j];
    public /*@ pure @*/ int get (int i, int j) {
        return matrix_implem[x * j + i];
    }
	
    /*@ ensures \result >= 0 && \result < x
      @     ==> matrix[\result][coordY(elem)] == elem;
      @*/
    public /*@ pure @*/ int coordX (int elem) {
        for (int i = 0; i < matrix_implem.length; i++)
            if (matrix_implem[i] == elem)
                return i % x;
        return -1;
    }

    /*@ ensures \result >= 0 && \result < y
      @     ==> matrix[coordX(elem)][\result] == elem;
      @*/
    public /*@ pure @*/ int coordY (int elem) {
        for (int i = 0; i < matrix_implem.length; i++)
            if (matrix_implem[i] == elem)
                return i / x;
        return -1;
    }
}
