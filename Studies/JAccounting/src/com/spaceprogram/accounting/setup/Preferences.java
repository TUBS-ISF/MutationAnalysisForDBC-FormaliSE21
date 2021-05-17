/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 8, 2002
 * Time: 5:10:15 PM
 * 
 */
package com.spaceprogram.accounting.setup;
public class Preferences {

    int id;
    int companyKey;
    /**
     * email to send invoices from
     */
    String fromEmail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(int companyKey) {
        this.companyKey = companyKey;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public static Preferences getDefault() {
        return new Preferences();
    }

}
