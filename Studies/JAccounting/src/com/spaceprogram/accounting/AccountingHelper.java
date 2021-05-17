/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 9, 2002
 * Time: 10:30:11 PM
 * 
 */
package com.spaceprogram.accounting;

import com.spaceprogram.accounting.basic.Charge;
import com.spaceprogram.accounting.basic.ChargeLine;
import com.spaceprogram.accounting.basic.Payment;
import com.spaceprogram.accounting.basic.Transaction;
import com.spaceprogram.accounting.common.Recurrence;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import java.util.*;

/*@nullable_by_default@*/
public class AccountingHelper {
   /* public static Charge dupeCharge(SQLFactory factory, Connection conn, int cid, int uid, Charge charge, Date newdate) throws SQLException {


        charge = dupeChargeNonRecurrence(factory, conn, cid, uid, charge, newdate);
        
        // now save recurrence if exists
        Recurrence rec = charge.getRecurrence();
        if(rec != null){
            rec.setItemKey("" + charge.getId()); // just to be sure, cause could have duped
            AccountingPersistenceManager.saveRecurrence(factory, conn, cid, uid, rec);
        }
  

        return charge;


    }*/
	
	//@ pre sess != null;
	//@ post true;
    public static Charge dupeCharge(Session sess, Integer cid, Charge charge, Date newdate) throws HibernateException, CloneNotSupportedException {

       Integer chargeId = charge.getId();

        charge = dupeChargeNonRecurrence(sess, cid, charge, newdate);

        // now update recurrence
        Recurrence rec = AccountingPersistenceManager.getRecurrence(sess, AccountingPersistenceManager.APP_ID_CHARGE+"", chargeId.toString());//charge.getRecurrence();

        if(rec != null){
            rec.setItemKey("" + charge.getId()); // just to be sure, cause could have duped
            //AccountingPersistenceManager.saveRecurrence(factory, conn, cid, uid, rec);
            // hibernate should save this automatically now
        }
       return charge;

    }

    //@ pre true;
    //@ post cid != null;
    public static Charge dupeChargeNonRecurrence(Session sess, Integer cid, Charge chargeOld, Date newdate) throws HibernateException, CloneNotSupportedException {
        // trying to make a copy like in this thread: http://forum.hibernate.org/viewtopic.php?t=925326&highlight=clone
        Charge charge = (Charge) chargeOld.clone();
        charge.setId(null);
        charge.setChargeDate(newdate);
        charge.setInvoiceKey(null);


       Set  chargeLines = charge.getChargeLines();
        Set newChargeLines = new HashSet();
        charge.setChargeLines(newChargeLines);
        
       Iterator iter = chargeLines.iterator();
        // for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
             ChargeLine newChargeLine = dupeChargeLine(chargeLine, newdate);

             newChargeLines.add(newChargeLine);
        }
        charge.setChargeNumber(getNextChargeNumber(sess, cid));
        sess.save(charge);
        return charge;

    }


    /**
     * make a dupe from a click, not from a recurrence
     * @param newdate
     * @return
     *
     */
    /*public static Charge dupeChargeNonRecurrence(SQLFactory factory, Connection conn, int cid, int uid, Charge charge, Date newdate) throws SQLException {
        // todo: clone this so in case i start using hiber for it, it will work.
        charge.setId(0); // so it will be added again
        charge.setChargeDate(newdate);
        charge.setInvoiceKey(0);
       Set  chargeLines = charge.getChargeLines();
       Iterator iter = chargeLines.iterator();
        // for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            dupeChargeLine(chargeLine, newdate);
        }
        charge.setChargeNumber(getNextChargeNumber(factory, conn, cid, uid));
        AccountingPersistenceManager.saveCharge(factory, conn, cid, uid, charge);
        return charge;
    }*/

    private static ChargeLine dupeChargeLine(ChargeLine chargeLineOld, Date newdate) throws CloneNotSupportedException {
        ChargeLine chargeLine = (ChargeLine) chargeLineOld.clone();
        chargeLine.setId(null);

        // gotta do it to transactions too
        Transaction tOld = chargeLine.getTransaction();
        Transaction t = (Transaction) tOld.clone();
        t.setId(null);
        t.setTransactionDate(newdate);
        chargeLine.setTransaction(t);
        return chargeLine;

    }

    public static Integer getNextInvoiceNumber(Session sess, Integer cid) throws HibernateException {
        Integer ret;
        //SelectQuery sq = factory.getSelectQuery();
        List x = sess.find("select max(inv.invoiceNumber) from Invoice inv where inv.companyKey = ?",
                cid,
                Hibernate.INTEGER);

        if(x.size() > 0){
            //Object[] row = (Object[]) x.get(0);
            ret = (Integer)x.get(0);
            ret = new Integer(ret.intValue() + 1);
        }
        else ret = new Integer(1);

        return ret;
    }



    public static Integer getNextChargeNumber(Session sess, Integer cid) throws HibernateException {
        Integer ret;
        List rs = sess.find("select max(charge.chargeNumber) from " + Charge.class.getName()
        + " charge where charge.companyKey = ?", cid, Hibernate.INTEGER);
        if(rs.size() > 0){
            ret = (Integer) rs.get(0);
            if(ret != null){
                ret = new Integer(ret.intValue() + 1);
            }
            else ret = new Integer(1);
        }
        else ret = new Integer(1);
        return ret;
    }

    public static Integer getNextPaymentNumber(Session sess, Integer cid) throws  HibernateException {


        Integer ret;
        List rs = sess.find("select max(p.paymentNumber) from " + Payment.class.getName()
        + " p where p.companyKey = ?", cid, Hibernate.INTEGER);
        if(rs.size() > 0){
            ret = (Integer) rs.get(0);
            ret = new Integer(ret.intValue() + 1);
        }
        else ret = new Integer(1);
        return ret;
    }







}
