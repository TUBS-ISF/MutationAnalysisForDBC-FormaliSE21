package com.spaceprogram.accounting.model;

import com.crossdb.sql.SQLFactory;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.Payment;
import com.spaceprogram.accounting.basic.PaymentDistribution;
import com.spaceprogram.accounting.basic.Transaction;
import com.spaceprogram.accounting.common.Recurrence;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 20
 * @author 2003
 *         Time: 5:02:13 PM
 * @version 0.1
 */
public class PaymentPage extends CompanyCheck {
    Integer id;
    String formaction;
    Payment payment;
    Integer customerKey;

    protected String perform2() throws Exception {


        if (formaction != null) {
            if (formaction.equals("payment")) {
                // todo: I wonder if payment.journalEntry should change to many-to-one like invoice is instead of one-to-one?
                // then update main charge info
                Session sess = getSessionFactory().openSession();
                net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();


                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                    Integer jid = null;
                    if (id.intValue() > 0) {
                        payment = (Payment) sess.load(Payment.class, id);
                        jid = payment.getJournalEntry().getId();
                        // now to match up distro lines with je transactions
                        payment.matchDistributionLinesToTransactions();
                    } else {
                        payment = Payment.getDefault(cid);

                        jid = (Integer) sess.save(payment.getJournalEntry());

                    }
                    BigDecimal amountForPayment = new BigDecimal(request.getParameter("amount"));

                    payment.setCompanyKey(cid);
                    payment.setPaymentNumber(new Integer(request.getParameter("paymentNumber")));
                    payment.setPaymentDate(sdf.parse(request.getParameter("paymentDate")));
                    payment.setReferenceNumber(request.getParameter("referenceNumber"));
                    payment.setAmount(amountForPayment);

                    payment.setMemo(request.getParameter("memo"));
                    payment.setCustomerKey(customerKey);
                    payment.setDepositAccountKey(new Integer(request.getParameter("accountKey")));

                    System.out.println("PAYMENT: " + payment.getId());


                    // now do distribution lines applied to invoices
                    Integer arAccountKey = AccountingPersistenceManager.getAcountByInternalId(sess, cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_RECEIVABLE).getId();
                    BigDecimal totalPaymentToDistroLines = doDistributionLines(request, payment, cid, factory, sess, jid, arAccountKey);
                    BigDecimal amountForCredit = amountForPayment.subtract(totalPaymentToDistroLines); // credit whatever is leftover
                    // todo: deal with this credit!!!  put into A/P ?  or a Customer Credit account?
                    /**
                     * ok, going to add credit to A/P, so increase A/P,
                     * then when doing payments, you can apply the A/P (credit) to an invoice therefore decreasing A/R (-) and A/P (+)
                     */
                    // now do distribution line going into credit (A/P) account
                    if (amountForCredit.compareTo(new BigDecimal("0.0")) > 0) {
                        System.out.println("Applying Credit: " + amountForCredit);
                        Integer apAccountKey = AccountingPersistenceManager.getAcountByInternalId(sess, cid, AccountingPersistenceManager.ACCOUNT_INTERNAL_ACCOUNTS_PAYABLE).getId();
                        PaymentDistribution paymentDistributionForCredit = null;
                        Set pds = payment.getDistributionLines();
                        Iterator iter = pds.iterator();
                        while (iter.hasNext()) {
                            PaymentDistribution paymentDistribution = (PaymentDistribution) iter.next();
                            if ((paymentDistribution.getInvoiceKey() == null || paymentDistribution.getInvoiceKey().intValue() == 0) && paymentDistribution.getTransaction().getAccountKey().intValue() == apAccountKey.intValue()) {
                                // found - then already applied to this, so just change
                                paymentDistributionForCredit = paymentDistribution;
                            }
                        }

                        Transaction trans = null;
                        if (paymentDistributionForCredit == null) { // no match found, so make new
                            paymentDistributionForCredit = new PaymentDistribution();
                            trans = new Transaction();
                            paymentDistributionForCredit.setTransaction(trans);
                        } else {
                            trans = paymentDistributionForCredit.getTransaction();
                        }
                        trans.setAmount(amountForCredit.negate());
                        paymentDistributionForCredit.setCompanyKey(cid);
                        trans.setCompanyKey(cid);
                        trans.setMemo("Over payment applied to A/P");
                        trans.setCustomerKey(payment.getCustomerKey());
                        trans.setTransactionDate(payment.getPaymentDate());
                        trans.setAccountKey(apAccountKey);
                        trans.setJournalEntryKey(payment.getJournalEntry().getId());
                        paymentDistributionForCredit.setInvoiceKey(null);

                        payment.addDistribution(paymentDistributionForCredit);
                    }
                    // now finally add one more transaction for the credit to A/R (decrease)
                    Transaction arCredit = null;
                    Collection transactions = payment.getJournalEntry().getTransactions();
                    Iterator iterator = transactions.iterator();
                    while (iterator.hasNext()) {
                        Transaction t = (Transaction) iterator.next();
                        if (t.getAmount().compareTo(new BigDecimal(0.0)) > 0) {
                            // then this is the transaction into cash/bank
                            arCredit = t;
                            break;
                        }
                    }
                    if (arCredit == null) {
                        arCredit = new Transaction();
                    }
                    arCredit.setAmount(amountForPayment);
                    arCredit.setCompanyKey(cid);
                    arCredit.setMemo("Payment received");
                    arCredit.setCustomerKey(payment.getCustomerKey());
                    arCredit.setTransactionDate(payment.getPaymentDate());
                    arCredit.setAccountKey(payment.getDepositAccountKey());
                    arCredit.setJournalEntryKey(payment.getJournalEntry().getId());
                    payment.getJournalEntry().addTransaction(arCredit);


                    if (id.intValue() == 0) {

                        sess.save(payment, jid);
                    }







                    // 5. commit transaction
                    tx.commit();
                } catch (Exception e) {
                    //if (tx!=null) tx.rollback();
                    throw e;
                } finally {
                    sess.close();


                }

                getCtx().setViewParam("id", customerKey);

                return "submitted";
            } else if (formaction.equals("delpayment")) {
                //AccountingPersistenceManager.deleteCharge(factory, conn, cid, uid, payment);

                Session sess = getSessionFactory().openSession();
                net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();
                    // do some work
                    payment = (Payment) sess.load(Payment.class, id);
                    sess.delete(payment);
                    addMessage("Payment has been deleted");
                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    throw e;
                } finally {
                    sess.close();
                }
                getCtx().setViewParam("id", customerKey);

                return "deleted";

            } else if (formaction.equals("delrec")) {
                // delete recurrence
                //int recid = ServletUtils.getIntParameter(request, "recid", 0);
                /*DeleteQuery dq = factory.getDeleteQuery();
                dq.setTable("Recurrence");
                dq.addWhereCondition("id", WhereCondition.EQUAL_TO, recid);
                dq.execute(conn);*/
                Session sess = getSessionFactory().openSession();
                net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();
                    // do some work
                    Recurrence rec = (Recurrence) sess.load(Recurrence.class, new Integer(request.getParameter("recid")));
                    sess.delete(rec);

                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    throw e;
                } finally {
                    sess.close();
                }
                getCtx().setViewParam("id", id);

            }
        }
        return SUCCESS;
    }

    BigDecimal doDistributionLines(HttpServletRequest request, Payment payment, Integer cid, SQLFactory factory,
                                   Session sess, Integer journalEntryKey, Integer arAccountKey) throws SQLException, HibernateException, IOException {
        // get paid_ textboxes and distribute
        //Enumeration paramNames = request.getParameterNames();
        //while(paramNames.hasMoreElements()){
        //String pname = (String) paramNames.nextElement();

        // if(pname.startsWith("paid_")){
        // instead, just get ones that are checked off
        BigDecimal amountUsed = new BigDecimal("0.0");
        String[] checkedOff = request.getParameterValues("invoiceid");
        // todo: need to apply some to tax account too, ie: Tax Payable account
        if (checkedOff != null) {
            for (int i = 0; i < checkedOff.length; i++) {
                String s = checkedOff[i];


                // get invoice id from this
                Integer invoiceId = new Integer(s); //pname.substring(5));
                String temp = request.getParameter("paid_" + invoiceId);
                if (!(temp.equals(""))) {
                    BigDecimal paid = new BigDecimal(temp).setScale(2, BigDecimal.ROUND_HALF_UP);
                    if (paid.compareTo(new BigDecimal(0.0)) > 0) {
                        // check to see if distribution already exists
                        PaymentDistribution pd = null;
                        pd = lookUpDistribution(payment, invoiceId);
                        Transaction trans = null;
                        boolean newtrans = false;
                        if (pd == null) { // no match found, so make new
                            pd = new PaymentDistribution();
                            trans = new Transaction();
                            pd.setTransaction(trans);
                            newtrans = true;
                        } else {
                            trans = pd.getTransaction();
                        }
                        //System.out.println("TRANS: " + trans + " pd: " + pd.getId() + " transKey: " + pd.getTransactionKey());
                        pd.setCompanyKey(cid);
                        trans.setCompanyKey(cid);
                        trans.setAmount(paid.negate());

                        trans.setMemo("Recieved from Payment " + payment.getId());
                        trans.setCustomerKey(payment.getCustomerKey());
                        trans.setTransactionDate(payment.getPaymentDate());
                        trans.setAccountKey(arAccountKey);
                        trans.setJournalEntryKey(journalEntryKey);
                        if(newtrans){
                            pd.setTransactionKey((Integer) sess.save(trans));
                        }
                        pd.setInvoiceKey(invoiceId);



                        /*Integer jid = (Integer) sess.save(trans);
                        sess.save(pd,jid);*/
                        amountUsed = amountUsed.add(paid);

                        payment.addDistribution(pd);

                        // now apply to invoice
                        // add to amountPaid, if that equasl amount of invoice, then flag invoice as paid
                        Invoice invoice = (Invoice) sess.load(Invoice.class, invoiceId);
                        // todo: check the amount to make sure it's not more than the invoice, for now assuming this could not happen
                        /*if(paid > invoice.getAmount()){
                            throw new
                        }*/
                        invoice.setAmountPaid(paid);
                        //out.print("INVOICE: total: " + invoice.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP) + " paid: " + paid);
                        if (paid.compareTo(invoice.getTotal().setScale(2, BigDecimal.ROUND_HALF_UP)) == 0) {
                            // then fully paid
                            // out.print("paid!"); out.flush();
                            invoice.setPaid(true);
                        } else {
                            //out.print("NOT paid!"); out.flush();
                            invoice.setPaid(false);
                        }
                    }

                }


            }
        }
        return amountUsed;
    }

    PaymentDistribution lookUpDistribution(Payment payment, Integer invoiceId) {
        Set pds = payment.getDistributionLines();
        Iterator iter = pds.iterator();
        while (iter.hasNext()) {
            PaymentDistribution paymentDistribution = (PaymentDistribution) iter.next();
            if (paymentDistribution.getInvoiceKey().intValue() == invoiceId.intValue()) {
                // then already applied to this, so just change
                return paymentDistribution;
                /* todo: need to delete the distro because it may be a double, this is only going back i think?
                if(pd == null){
                     pd = paymentDistribution;
                 }
                 else{
                     // delete this because it's a double?
                     //sess.delete(paymentDistribution);
                 }*/
            }
        }
        return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }

    public Payment getPayment() {
        return payment;
    }


    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

}
