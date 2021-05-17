/**
 * This will be the main class we will use to keep it sort of 3 tier where this is the middle tier
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Feb 22, 2003
 * Time: 6:34:31 PM
 * @version 0.1
 */
package com.spaceprogram.accounting.impl.hibernate;

import java.sql.SQLException;

import net.sf.hibernate.Session;

import com.spaceprogram.accounting.AccountingManager;

/*@nullable_by_default@*/
public class AccountingManagerHibernateImpl implements AccountingManager {

    Session sess;
    private int companyKey;

    public AccountingManagerHibernateImpl(int companyKey, Session sess) {
        this.sess = sess;
        this.companyKey = companyKey;
    }



    public int getAccountIdFromInternalCode(int companyKey, int internalAccountCode) {
        // look up account id

        return 0;
    }

    /**
     * done with manager, so close everything up and commit it all
     */
    public void close() throws SQLException, Exception {
        sess.close();
    }

}
