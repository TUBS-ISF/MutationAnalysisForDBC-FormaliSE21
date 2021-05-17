/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 5, 2002
 * Time: 2:36:12 PM
 * 
 */
package com.spaceprogram.accounting.basic;

/*@nullable_by_default@*/
public class AccountType {
    /*@spec_public@*/int id;
    String name;
    String description;

    public AccountType(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    //@ post \result == this.id;
    public int getId() {
        return id;
    }

    //@ post this.id == id;
    public void setId(int id) {
        this.id = id;
    }

    //@ pre true;
    //@ post true;
    public String getName() {
        return name;
    }

    //@ pre true;
    //@ post true;
    public void setName(String name) {
        this.name = name;
    }

    //@ pre true;
    //@ post true;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
