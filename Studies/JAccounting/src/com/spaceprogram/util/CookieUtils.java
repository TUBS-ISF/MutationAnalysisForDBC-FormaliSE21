/*
 * CookieUtils.java
 *
 * Created on May 16, 2000, 1:29 PM
 */

package com.spaceprogram.util;

import javax.servlet.http.Cookie;
/**
 *
 * @author  prophecy
 * @version
 */
public class CookieUtils extends Object {
	public static final int SECONDS_PER_DAY = 60*60*24;
	public static final int SECONDS_PER_WEEK = 60*60*24*7;
	public static final int SECONDS_PER_MONTH = 60*60*24*30;
	public static final int SECONDS_PER_YEAR = 60*60*24*365;
	

	public static String getCookieValue(Cookie[] cookies,
										String cookieName,
										String defaultValue) {
		if(cookies == null){
			return defaultValue;
		}
	    for(int i=0; i<cookies.length; i++) {
			Cookie cookie = cookies[i];
			if (cookieName.equals(cookie.getName()))
				return(cookie.getValue());
	    }
	    return(defaultValue);
	}
	public static Cookie getLongLivedCookie(String name, String value) {
		Cookie c = new Cookie(name, value);
		c.setMaxAge(SECONDS_PER_YEAR);
		return c;
	}
	
	//@ pre seconds >= 0;
	public static Cookie getCookie(String name, String value, int seconds){
		Cookie c = new Cookie(name, value);
		c.setMaxAge(seconds);
		return c;
	}
	
	//@ post !name.equals("");
	public static Cookie getDeleteCookie(String name){
		Cookie c = new Cookie(name, "null");
		c.setMaxAge(0);
		return c;
	}
	
}
