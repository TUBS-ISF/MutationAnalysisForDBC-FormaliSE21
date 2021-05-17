/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 5, 2002
 * Time: 12:54:11 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.io.PrintWriter;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.spaceprogram.accounting.basic.Customer;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;

/*@nullable_by_default@*/
public class CustomerSelect {

    String selectName = "accountKey";
    List obs;
    int selected = -1;

    public CustomerSelect(Session sess, Integer cid) throws HibernateException {
        // fill in products list
        refreshAccountList(sess, cid);
    }

    public void refreshAccountList(Session sess, Integer cid) throws HibernateException{
        obs = AccountingPersistenceManager.getCustomers(sess, cid);
    }

    public void write(PrintWriter out){
        out.print("<select name=\"" + selectName + "\">\n<option value=\"0\">Choose Customer...");
        for (int i = 0; i < obs.size(); i++) {
           Customer account = (Customer) obs.get(i);
            Integer aid = account.getId();

            out.print("<option value=\"" + aid + "\"");
            if(selected == aid.longValue()) out.print(" selected");
            out.print(">" + account.getName());
        }
        out.print("</select>");


    }

    public String getSelectName() {
        return selectName;
    }

    public void setSelectName(String selectName) {
        this.selectName = selectName;
    }


    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
