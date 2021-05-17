/*
 * Date.java
 *
 * Created on April 24, 2000, 8:20 PM
 */
 
package com.spaceprogram.util;

/** 
 *
 * @author  prophecy
 * @version 
 */

public class DateUtils extends Object {
	
	public static String[] month_strings = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
	public static String[] hour_strings = { "12 AM", "1 AM", "2 AM", "3 AM", "4 AM", "5 AM", "6 AM", "7 AM", "8 AM", "9 AM", "10 AM", "11 AM", "12 PM", "1 PM", "2 PM", "3 PM", "4 PM", "5 PM", "6 PM", "7 PM", "8 PM", "9 PM", "10 PM", "11 PM" };
	//@ public invariant hour_strings.length > 0;
	
	// months range from 0 to 11
	public static String monthAsString(int month){ 
		String ms = null;
		if(month > 11 || month < 0){
			return ms; // for efficencies sake
		}
		ms = month_strings[month];
		return ms;	
	}
	
	//@ post \result.length > 0;
	public static String[] getMonthStrings(){
		return month_strings;
	}
	
	
}

