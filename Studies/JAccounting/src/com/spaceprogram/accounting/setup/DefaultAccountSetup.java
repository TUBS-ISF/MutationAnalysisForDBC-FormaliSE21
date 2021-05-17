/**
 * will create and setup the default accounts
 *
 * TODO: there should be a bunch of default accounts created in the accounts table that are just duplicated with new cid
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 8, 2002
 * Time: 2:22:50 PM
 * 
 */
package com.spaceprogram.accounting.setup;

import com.spaceprogram.accounting.basic.Account;
import com.spaceprogram.accounting.basic.AccountTypes;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/*@nullable_by_default@*/
public class DefaultAccountSetup {

    /** use for now to setup basic accounts */
    public static void setupBasic(Session sess, Integer cid) throws Exception {

        createInternalAccount(sess, cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_OPENING_BALANCE_EQUITY);
        createInternalAccount(sess,cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_TAX_PAYABLE);
        createInternalAccount(sess,cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE);
        //createInternalAccount(factory, conn, cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_OPENING_BALANCE_EQUITY);

    }

    //@ pre sess != null;
    //@ post true;
    public static Account createInternalAccount(Session sess, Integer cid, int internalId) throws Exception, HibernateException {
       Account account = null;
       net.sf.hibernate.Transaction tx = null;
        try{
        tx = sess.beginTransaction();
        if(internalId == AccountingPersistenceManager.ACCOUNT_INTERNAL_OPENING_BALANCE_EQUITY){
            account = new Account();
            account.setName("Opening Balance Equity");

            account.setParentKey(new Integer(0));
            account.setTypeKey(AccountTypes.EQUITY );
            account.setDetailTypeKey(0);
            account.setDescription("Opening Balance Equity");
            account.setInternalId(AccountingPersistenceManager.ACCOUNT_INTERNAL_OPENING_BALANCE_EQUITY);



            //AccountingPersistenceManager.saveAccount(factory, conn, cid, account);
            sess.save(account);
        }
        else if(internalId == AccountingPersistenceManager.ACCOUNT_INTERNAL_TAX_PAYABLE){
             account = new Account();
            account.setName("Sales Tax Payable");

            account.setParentKey(new Integer(0));
            account.setTypeKey(AccountTypes.OTHER_CURRENT_LIABILITY);
            account.setDetailTypeKey(0);
            account.setDescription("Sales Tax Payable to the government");
            account.setInternalId(AccountingPersistenceManager.ACCOUNT_INTERNAL_TAX_PAYABLE);


            sess.save(account);

            //AccountingPersistenceManager.saveAccount(factory, conn, cid, account);

        }
         else if(internalId == AccountingPersistenceManager.ACCOUNT_INTERNAL_TAX_RECEIVABLE){
             account = new Account();
            account.setName("Sales Tax Receivable");

            account.setParentKey(new Integer(0));
            account.setTypeKey(AccountTypes.OTHER_CURRENT_ASSETS);
            account.setDetailTypeKey(0);
            account.setDescription("Sales Tax Receivable from the government");
            account.setInternalId(AccountingPersistenceManager.ACCOUNT_INTERNAL_TAX_RECEIVABLE);



            //AccountingPersistenceManager.saveAccount(factory, conn, cid, account);
             sess.save(account);

        }
         else if(internalId == AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE){
             account = new Account();
            account.setName("Accounts Receivable");

            account.setParentKey(new Integer(0));
            account.setTypeKey(AccountTypes.ACCOUNTS_RECEIVABLE);
            account.setDetailTypeKey(0);
            account.setDescription("Accounts Receivable");
            account.setInternalId(AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE);



            //AccountingPersistenceManager.saveAccount(factory, conn, cid, account);
             sess.save(account);

        }
        else if(internalId == AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_PAYABLE){
             account = new Account();
            account.setName("Accounts Payable");

            account.setParentKey(new Integer(0));
            account.setTypeKey(AccountTypes.ACCOUNTS_RECEIVABLE);
            account.setDetailTypeKey(0);
            account.setDescription("Accounts Payable");
            account.setInternalId(AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_PAYABLE);



            //AccountingPersistenceManager.saveAccount(factory, conn, cid, account);
            sess.save(account);

        }
            tx.commit();
                } catch (Exception e) {
                    try {
                        tx.rollback();
                    } catch (HibernateException e1) {
                        e1.printStackTrace();
                    }
                    throw e;
                } finally {
                    sess.close();
                }
        return account;
    }

}
