package com.spaceprogram.util;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 16
 * @author 2003
 *         Time: 4:09:40 PM
 * @version 0.1
 */

public class SecurityUtils {


    public static String generateRandomSequence(int length, String prefix) {
        String ret = prefix; // could pass in a prefix here
        double x = Math.pow(10, length);
        int randomnum = (int) (Math.random() * x);
        ret = ret + randomnum;
        return ret;
    }

    public static void main(/*@nullable@*/String args[]) {
        System.out.println(SecurityUtils.generateRandomSequence(10, "pre"));
    }


}
