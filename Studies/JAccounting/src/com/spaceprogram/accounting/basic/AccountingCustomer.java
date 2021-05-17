/**
 * will be a map from this table to Customers table.
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 5, 2002
 * Time: 12:59:10 PM
 * 
 */
package com.spaceprogram.accounting.basic;



public class AccountingCustomer {

    /*@spec_public@*/int id;
    Customer customer;
    /*@spec_public@*/int type;

    public AccountingCustomer() {
        customer = new Customer();
    }

    //@ requires true;
    //@ ensures \result >= 0;
    //@ ensures \result == id;
    public /*@pure@*/int getId() {
        return id;
    }

    //@ requires id >= 0;
    //@ ensures this.id == id;
    public void setId(int id) {
        this.id = id;
    }

    //@ requires true;
    //@ ensures \result >= 0;
    //@ ensures \result == type;
    public int getType() {
        return type;
    }

    //@ requires true;
    //@ ensures this.type == type;
    public void setType(int type) {
        this.type = type;
    }

    //@ requires true;
    public Customer getCustomer() {
        return customer;
    }

    //@ ensures true;
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

  

}
