
 
package com.spaceprogram.text;

/**
 *
 * @author  prophecy
 * @version
 */
public class TimeFormat extends Object {
    private boolean showSeconds;

    /**
	Could have vars to set whether to show seconds,
	to format it differently, etc.
	maybe like 4 hours, 5 minutes kind of thing.
	Similar to DateFormat or simpledateformat
	 */
	
	public TimeFormat(){
	}
	
	public String format(long length){
		long secs = length / 1000; // 1000 milliseconds per second
		long mins = (secs / 60) % 60;
		long hours = (secs / 60) / 60;
		String ret = hours + ":" + com.spaceprogram.util.StringUtils.padInt((int)mins, 2);
        if(showSeconds){
            ret += ":" + com.spaceprogram.util.StringUtils.padInt((int)secs, 2);
        }
		return ret;
	}
	public static void main(/*@nullable@*/String args[]){
		long x = 3960001;
		System.out.println(new TimeFormat().format(x));
	}

    public void showSeconds(boolean b) {
        showSeconds = b;
    }

}
