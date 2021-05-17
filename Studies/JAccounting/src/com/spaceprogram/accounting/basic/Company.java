
package com.spaceprogram.accounting.basic;




/**
 *
 * @author  prophecy
 * @version
 */
/*@nullable_by_default@*/
public class Company implements java.io.Serializable  {
	
	/*@spec_public@*/Integer id;
	/*@spec_public@*/String name;
    //String displayName;
	/*@spec_public@*/boolean active;
	//int edition;
	
	//@ pre true;
	//@ post active == true;
	public Company(){
		active = true;
	}

	//@ post \result != null;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    //@ pre !name.equals("");
    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
