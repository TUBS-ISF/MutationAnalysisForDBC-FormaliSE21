/**
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Feb 22, 2003
 * Time: 7:02:01 PM
 * @version 0.1
 */
package com.spaceprogram.accounting.impl.hibernate;

import java.sql.SQLException;

import com.spaceprogram.accounting.AccountingManager;
import com.spaceprogram.accounting.AccountingManagerFactory;

public class AccountingManagerFactoryHibernateImpl implements AccountingManagerFactory {

    net.sf.hibernate.SessionFactory hsessions;

    public AccountingManagerFactoryHibernateImpl(net.sf.hibernate.SessionFactory hsessions) {
        this.hsessions = hsessions;
    }

    public AccountingManager getAccountingManager(int companyKey) throws SQLException, Exception {
        return (AccountingManager)new AccountingManagerHibernateImpl(companyKey, hsessions.openSession());
    }
}
