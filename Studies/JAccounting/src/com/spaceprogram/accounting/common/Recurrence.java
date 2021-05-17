/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 9, 2002
 * Time: 1:30:15 PM
 * 
 */
package com.spaceprogram.accounting.common;

import java.util.Date;
import java.util.Calendar;

public class Recurrence {
	/*@spec_public@*/Long id;
    /*@spec_public@*/String realm;
    /*@spec_public@*/String itemKey;
    /**
     * types
     * d daily
     * w
     * m
     * y
     */
    char type;

    int x; // used for every X days/months/weeks
    /*@spec_public@*/int y; // used for day of week/day of month
    int z; // used for month of year

    Date startDate;
    Date endDate;

    /*@spec_public@*/int countdown = -1; // used for countdown for how many uses, -1 = infinite

    /**
     * chargeDate + rec span
     */
    Date nextDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    //@ pre y > 0;
    public int getY() {
        return y;
    }

    //@ pre true;
    public void setY(int y) {
        this.y = y;
    }

    //@ pre true;
    public int getZ() {
        return z;
    }

    //@ pre true;
    public void setZ(int z) {
        this.z = z;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    //@ pre countdown >= 0;
    public int getCountdown() {
        return countdown;
    }

    public void setCountdown(int countdown) {
        this.countdown = countdown;
    }

    //@ pre true;
    //@ post true;
    public static Recurrence getDefault(String realm, String itemKey) {
        Recurrence ret = new Recurrence();
        ret.setType('m');
        ret.setX(1);
        ret.setStartDate(new Date());
        ret.setRealm(realm);
        ret.setItemKey(itemKey);
        return ret;
    }

    /**
     * this has to be called before any use of getNextDate()
     * @param chargeDate
     * @return
     */
    public Date getNextDate(Date chargeDate) {

        // set it first
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(chargeDate);
         if(type == 'd'){
                   cal2.add(Calendar.DAY_OF_YEAR, x);
               }
               else if(type == 'w'){
                   cal2.add(Calendar.WEEK_OF_YEAR, x);
                   cal2.set(Calendar.DAY_OF_WEEK, y);
               }
               else if(type == 'm'){
                   cal2.add(Calendar.MONTH, x);
                   cal2.set(Calendar.DAY_OF_MONTH, y);
               }
               else if(type == 'y'){
                   cal2.add(Calendar.YEAR, 1); // can't set X yet, so just use 1
                   cal2.set(Calendar.DAY_OF_MONTH, y);
                   cal2.set(Calendar.MONTH, z);
               }

        nextDate = cal2.getTime();
        return nextDate;

    }
    
    public Date getNextDate(){
        return nextDate;
    }

    public void setNextDate(Date nextDate) {
        this.nextDate = nextDate;
    }

}


