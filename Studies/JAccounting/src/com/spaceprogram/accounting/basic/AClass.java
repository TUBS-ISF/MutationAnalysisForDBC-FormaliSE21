/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 4, 2002
 * Time: 2:47:49 PM
 * 
 */
package com.spaceprogram.accounting.basic;

/*@nullable_by_default@*/
public class AClass extends Object {
    private /*@spec_public@*/int id;
    private /*@spec_public@*/int companyKey;
    private /*@spec_public@*/String name;


    public AClass() {
    }

    public AClass(int id, int companyKey, String name) {

        this.id = id;
        this.companyKey = companyKey;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    //@ pre id >= 0;
    public void setId(int id) {
        this.id = id;
    }
    
    //@ pre true;
    public int getCompanyKey() {
        return companyKey;
    }

    //@ pre true;
    public void setCompanyKey(int companyKey) {
        this.companyKey = companyKey;
    }

    //@ post !\result.equals("");
    public String getName() {
        return name;
    }

    //@ pre true;
    public void setName(String name) {
        this.name = name;
    }

    public static AClass getDefault() {
            AClass ac = new AClass();
        ac.setName("");
        return ac;
    }
}
