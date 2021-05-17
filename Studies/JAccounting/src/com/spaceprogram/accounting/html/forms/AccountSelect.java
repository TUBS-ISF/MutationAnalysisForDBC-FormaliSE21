/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 4, 2002
 * Time: 2:20:22 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.io.PrintWriter;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.spaceprogram.accounting.basic.Account;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;

/*@nullable_by_default@*/
public class AccountSelect extends DefaultSelect {

private int internalIdToSelect = -1;


    public AccountSelect(Session sess, Integer cid) throws HibernateException {
        selectName = "accountKey";
        addOptionToStart(new Option(0, "Choose Account..."));
        // fill in products list
        refresh(sess, cid);
    }

    public void refresh(Session sess, Integer cid) throws HibernateException {
        optionables = AccountingPersistenceManager.getAccounts(sess, cid);
    }
    public void write(PrintWriter out){
            out.print("<select name=\"" + selectName + "\">\n");
            for (int j = 0; j < startOptions.size(); j++) {
                Option option = (Option) startOptions.get(j);


                out.print("<option value=\"" + option.getId() + "\"");
                if(option.isSelected()){
                    out.print(" selected");
                    }
                out.print(">" + option.getName());

            }
            for (int i = 0; i < optionables.size(); i++) {
                Account account = (Account) optionables.get(i);
                int aid = account.getId().intValue();
                out.print("<option value=\"" + aid + "\"");
                if(selected == aid || internalIdToSelect == account.getInternalId()) out.print(" selected");
                out.print(">" + account.getName());
                if(internalIdToSelect == account.getInternalId()){
                    out.print(" (recommended)");
                }
            }
            out.print("</select>");


        }



    public List getAccounts() {
        return optionables;
    }

    public void setAccounts(List accounts) {
        this.optionables = accounts;
    }
     public void setSelectedByInternalId(int internalId){
        this.internalIdToSelect = internalId;

    }
}
