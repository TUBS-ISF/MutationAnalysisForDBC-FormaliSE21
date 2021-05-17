/*
 * ArrayUtils.java
 *
 * Created on July 24, 2000, 1:32 PM
 */
 
package com.spaceprogram.util;

import java.util.StringTokenizer;
/**
 *
 * @author  prophecy
 * @version
 */
/*@nullable_by_default@*/
public class ArrayUtils extends Object {

	//@ pre intarray != null;
	//@ post true;
	public static int getMaxIntArrayIndex(int intarray[]){
		boolean higher_than_zero = false;
		int highest = 0;
		if(intarray[0] > 0){
			higher_than_zero = true;
		}
		for(int i = 0; i < intarray.length; i++){
			if(intarray[i] > intarray[highest]){
				highest = i;
				higher_than_zero = true;
			}
		}
		if(!higher_than_zero){
			highest = -1;
		}
		return highest;
	}
	public static void fillIntArray(int intarray[], int filler){
		for(int i = 0; i < intarray.length; i++){
			intarray[i] = filler;
		}
	}
	//@ post \result instanceof java.lang.String;
	public static String arrayToString(int sarray[]){
		String s = "";
		for(int i = 0; i < sarray.length; i++){
			s += sarray[i];
			if(i != sarray.length-1){
				s += ",";
			}
		}
		return s;
	}
	//@ post !\result.equals("");
	public static String arrayToString(String sarray[]){
		String s = "";
		for(int i = 0; i < sarray.length; i++){
			s += sarray[i];
			if(i != sarray.length-1){
				s += ",";
			}
		}
		return s;
	}
	//@ pre true;
	public static int[] stringArrayToIntArray(String sarray[]){
		int ia[] = new int[sarray.length];
		for(int i = 0; i < sarray.length; i++){
			ia[i] = Integer.parseInt(sarray[i]);
		}
		return ia;
	}
	/**
	 Takes comma delimited string of ints and returns them as an array of ints
	*/
	public static int[] intStringToArray(String s){
		StringTokenizer st = new StringTokenizer(s, ",");
		int numtokens = st.countTokens();
		int ia[] = new int[numtokens];
		int counter = 0;
		while(st.hasMoreTokens()){
			ia[counter] = Integer.parseInt(st.nextToken());
			counter++;
		}
		return ia;
		
	}
	
	/**
	String array passed in along with the string to find.
	 */
	public static boolean contains(String[] a, String n){
		if(a != null){
			for(int i = 0; i < a.length; i++){
				if(a[i].equals(n)){
					return true;
				}
			}
		}
		return false;
	}
		
	
		

}

