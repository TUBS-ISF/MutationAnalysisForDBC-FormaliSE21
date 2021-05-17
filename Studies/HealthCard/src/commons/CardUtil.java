package commons;

import javacard.framework.Util;

public class CardUtil {

    //@ assignable javacard.framework.Util;
	public static byte[] clone(byte[] bytes) {
    	byte[] cloned = new byte[bytes.length];
		System.arraycopy(bytes, (short)0, cloned, (short)0, (short)cloned.length);
		return cloned;
    }
	
	//@ requires from.length <= to.length;
	public static void arrayCopy(byte[] from, byte[] to) {
		System.arraycopy(from, (short)0, to, (short)0, (short)from.length);
	}

	//Counts the number of non null values of an object array.
    //@ assignable \nothing;
    public /*@ pure @*/ static short countNotNullObjects (Object[] array) {
    	short count = (short)0;
		for (short i = (short)0; i < (short)array.length;i++) {
			if (array[i] != null) {
				count += 1;
			}
		}
		return count;
    }
    
    /*//returns the last non null value of an object array.
    public static Object getLastNonNullObject(Object[] array){
    	return array[countNotNullObjects(array)-1];
    }*/

    //Returns false if position points to a null value or if position is out of bounds.
    //@ assignable \nothing;
    public /*@ pure @*/ static boolean validateObjectArrayPosition (Object[] array, short position) {
    	if(position < 0 || position >= countNotNullObjects(array))
			return false;
    	else 
    		return true;
    }
    
    public static void cleanField(byte[] field){
    	for(short i = 0; i < field.length; i++){
    		field[i] = (byte)0x00;
    	}
    }

}

