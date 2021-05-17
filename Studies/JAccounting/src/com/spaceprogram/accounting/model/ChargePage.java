package com.spaceprogram.accounting.model;

import com.crossdb.sql.DeleteQuery;
import com.crossdb.sql.WhereCondition;
import com.spaceprogram.accounting.AccountingHelper;
import com.spaceprogram.util.ServletUtils;
import com.spaceprogram.accounting.basic.Charge;
import com.spaceprogram.accounting.basic.ChargeLine;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import com.spaceprogram.util.ServletUtils;
import net.sf.hibernate.Hibernate;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 8
 * @author 2003
 *         Time: 8:30:15 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public class ChargePage extends CompanyCheck {
    Integer id;
    Integer customerKey;
    Charge charge;

    boolean rec = false;
    boolean dupe = false;

    Integer chargeNumber;
    private boolean showChargeLines = false;
    private Collection chargeLinesToShow;
    private int numberOfLines = 1;
    private Integer clid;
    private Integer productKey;

    protected String perform2() throws Exception {
        String formaction = request.getParameter("formaction");
        if (formaction != null) {
            String redir = SUCCESS;
            /* if (customerKey == null) {
                 addError("customerKey", "You must choose a customer.");
                 return ERROR;
             }*/
            Session sess = getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = sess.beginTransaction();
                // do some work
    
                Connection conn = sess.connection();
                //int cid = getCid().intValue();
                //int uid = user.getId().intValue();
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Charge charge;
                if (id.intValue() > 0) {
                    charge = (Charge) sess.load(Charge.class, id); //AccountingPersistenceManager.getCharge(factory, conn, cid, uid, id.intValue());
                } else
                    charge = new Charge();
                if (formaction.equals("charge")) {
                    // then update main charge info
                    //charge = new Charge();
                    //charge.setId(id);
                    charge.setCompanyKey(cid);
                    charge.setUpdated(new Date());
                    charge.setChargeNumber(chargeNumber);
                    charge.setChargeDate(sdf.parse(request.getParameter("chargeDate")));

                    charge.setMemo(request.getParameter("memo"));
                    charge.setCustomerKey(customerKey);
                    charge.setCurrency(request.getParameter("currency"));
                    //AccountingPersistenceManager.saveCharge(factory, conn, cid, uid, charge);
                    if (id.intValue() == 0) {
                        id = (Integer) sess.save(charge);
                    }
                    getCtx().setViewParam("id", charge.getId());

                } else if (formaction.equals("delcharge")) {
                    //AccountingPersistenceManager.deleteCharge(factory, conn, cid, uid, charge);

                    sess.delete(charge);

                    getCtx().setViewParam("id", charge.getCustomerKey());
                    redir = "deleted";

                } else if (formaction.equals("complete")) {
                    sess.close();
                    return "completed";
                } else if (formaction.equals("delrec")) {
                    // delete recurrence
                    int recid = ServletUtils.getIntParameter(request, "recid", 0);
                    DeleteQuery dq = factory.getDeleteQuery();
                    dq.setTable("Recurrence");
                    dq.addWhereCondition("id", WhereCondition.EQUAL_TO, recid);
                    dq.execute(conn);
                    sess.close();
                    getCtx().setViewParam("id", charge.getId());
                    return "completed";
                } else if (formaction.equals("changecust")) {
                    // change charge to different customer

                    //net.sf.hibernate.Transaction tx = sess.beginTransaction();
                    Charge c = (Charge) sess.load(Charge.class, id);
                    if (c.getCompanyKey().equals(cid)) {

                        c.setCustomerKey(customerKey);
                    }

                    getCtx().setViewParam("id", charge.getId());
                }

                tx.commit();

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            } finally {
                sess.close();
            }


            return redir;
        }

        //boolean rec = ServletUtils.getBooleanParameter(request, "rec");
        if (rec) { // then dupe this charge and set date to latest date + rec charge
            Session sess = getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = sess.beginTransaction();


                System.out.println("DUPING CHARGE");
                //Charge charge;
                charge = AccountingPersistenceManager.getCharge(sess, id); //(Charge) sess.load(Charge.class, id); //AccountingPersistenceManager.getCharge(factory, conn, cid, uid, id.intValue());

                //todo: MAKE DUPE CHARGE ACTUALLY DUPE THEN SAVE THE CHARGE WITH ALL CHARGELINES AND UPDATE THE RECURRENCE TO POINT TO NEW ONE
                charge = AccountingHelper.dupeCharge(sess, cid, charge, new Date(Long.parseLong(request.getParameter("d"))));

                id = charge.getId();// just to be safe


                addMessage("Creating new Charge from recurrence.");
                showChargeLines = true;
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            } finally {
                sess.close();
            }
        }

        //boolean dupe = ServletUtils.getBooleanParameter(request, "dupe");
        if (dupe) {
            Session sess = getSessionFactory().openSession();
            Transaction tx = null;
            try {
                tx = sess.beginTransaction();
                //Charge charge;
                charge = (Charge) sess.load(Charge.class, id);
                charge = AccountingHelper.dupeChargeNonRecurrence(sess, cid, charge, new Date());

                id = charge.getId(); // just to be safe

                addMessage("Duplicated charge.");
                showChargeLines = true;
                tx.commit();
            } catch (Exception e) {
                if (tx != null) tx.rollback();
                throw e;
            } finally {
                sess.close();
            }
        }

        String clformaction = request.getParameter("clformaction");
        if (clformaction != null) {
            chargeLinesToShow = new ArrayList();
            //int clid = ServletUtils.getIntParameter(request, "clid", 0);
            if (clformaction.equals("chargeline")) {

                if (request.getParameter("prodSubmit") != null) {
                    // then add new chargeline to list.
                    ChargeLine cl = ChargeLine.getDefault();
                    cl.setProductKey(new Integer(request.getParameter("productKey")));
                    chargeLinesToShow.add(cl);
                    numberOfLines = 0;
                }
                if (request.getParameter("save") != null) {
                    // then save to db
                    Session sess = getSessionFactory().openSession();
                    Transaction tx = null;
                    try {
                        tx = sess.beginTransaction();
                        Charge charge = (Charge) sess.load(Charge.class, id);
                        ChargeLine cl;
                        if (clid.intValue() == 0) { // then never saved to db yet, so make new one and save it
                            cl = ChargeLine.getDefault();
                            //chargeLinesToShow.add(cl);
                            charge.addChargeLine(cl);
                        } else {
                            cl = charge.getChargeLine(clid);
                        }
                        //cl.setChargeKey(id);
                        cl.setProduct(AccountingPersistenceManager.getProduct(sess, productKey)); //factory, conn, ServletUtils.getIntParameter(request, "productKey", 0)));
                        cl.setDescription(request.getParameter("description"));
                        cl.setRate(ServletUtils.getBigDecimalParameter(request, "rate", "0.0"));
                        cl.setQuantity(ServletUtils.getBigDecimalParameter(request, "quantity", "0.0"));
                        cl.setClassKey(ServletUtils.getIntParameter(request, "classKey", 0));
                        cl.setTaxable(ServletUtils.getBooleanParameter(request, "taxable"));
                        cl.setTransactionDate(charge.getChargeDate());
                        cl.setCurrency(charge.getCurrency());
                        cl.setCompanyKey(cid);





                        //AccountingPersistenceManager.saveChargeLine(factory, conn, cid, uid, cl);

                        tx.commit();
                    } catch (Exception e) {
                        if (tx != null) tx.rollback();
                        throw e;
                    } finally {
                        sess.close();
                    }
                }
            } else if (clformaction.equals("delcl")) {
                // delete chargeline
                Session sess = getSessionFactory().openSession();
                Transaction tx = null;
                try {
                    tx = sess.beginTransaction();
                    //AccountingPersistenceManager.deleteChargeLine(factory, conn, cid, uid, charge.getChargeLine(clid));
                    sess.delete("from " + ChargeLine.class.getName() + " cl where cl.id = ?",
                            clid,
                            Hibernate.INTEGER);

                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    throw e;
                } finally {
                    sess.close();
                }


            }

        }


        return SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

    public boolean isRec() {
        return rec;
    }

    public void setRec(boolean rec) {
        this.rec = rec;
    }

    public boolean isDupe() {
        return dupe;
    }

    public void setDupe(boolean dupe) {
        this.dupe = dupe;
    }

    public Integer getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(Integer chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public boolean isShowChargeLines() {
        return showChargeLines;
    }

    public void setShowChargeLines(boolean showChargeLines) {
        this.showChargeLines = showChargeLines;
    }

    //@ pre true;
    //@ post true;
    public Collection getChargeLinesToShow() {
        return chargeLinesToShow;
    }

    //@ pre true;
    public void setChargeLinesToShow(Collection chargeLinesToShow) {
        this.chargeLinesToShow = chargeLinesToShow;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    public void setNumberOfLines(int numberOfLines) {
        this.numberOfLines = numberOfLines;
    }

    public Integer getClid() {
        return clid;
    }

    public void setClid(Integer clid) {
        this.clid = clid;
    }

    public Integer getProductKey() {
        return productKey;
    }

    public void setProductKey(Integer productKey) {
        this.productKey = productKey;
    }

    public String getChargeRealm() {
        return AccountingPersistenceManager.APP_ID_CHARGE + "";
    }

    public Charge getCharge() {
        return charge;
    }
}
