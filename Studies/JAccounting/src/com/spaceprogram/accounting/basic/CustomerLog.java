/**
 * 
 * @author Travis reeder
 * Date: Jan 21, 2003
 * Time: 5:58:57 PM
 * @version 0.1
 */
package com.spaceprogram.accounting.basic;

/*@nullable_by_default@*/
public class CustomerLog {
    int id;
    int customerKey;
    /**
     * 10 = invoice sent
     * 20 = payment received
     *
     */
    int activityType;
    int itemKey; // ie: invoice id or payment id
    String description; // of what happened, ex: "Invoice sent.

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //@ post \result > 0;
    public int getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(int customerKey) {
        this.customerKey = customerKey;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public int getItemKey() {
        return itemKey;
    }

    public void setItemKey(int itemKey) {
        this.itemKey = itemKey;
    }
}
