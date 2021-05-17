/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 6, 2002
 * Time: 11:03:23 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;


/*@nullable_by_default@*/
public class Option implements Optionable{

    String name;
    /*@spec_public@*/int id;
    boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    //@ also
    //@ requires true;
    //@ ensures \result == id;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Option(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
