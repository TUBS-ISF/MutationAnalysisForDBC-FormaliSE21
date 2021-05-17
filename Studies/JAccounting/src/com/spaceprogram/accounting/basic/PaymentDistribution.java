/**
 * a distribution line for payments that maps to which invoice they paid to
 *
 *
 *
 * @author Travis reeder
 * Date: Jan 9, 2003
 * Time: 7:27:31 PM
 * @version 0.1
 */
package com.spaceprogram.accounting.basic;

public class PaymentDistribution {
    Integer id;
    Integer paymentKey;
    Integer invoiceKey;
    Transaction transaction; // these are NOT persisted, they will just be pointers into the journal entry
    Integer transactionKey; // reference to trans that will be persisted
    Integer companyKey;

    public PaymentDistribution() {

    }

    public static PaymentDistribution getDefault(){
        PaymentDistribution pd = new PaymentDistribution();
        pd.setTransaction(new Transaction());
        return pd;
    }


    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
        transactionKey = transaction.getId();
    }




    public Integer getTransactionKey() {
        if(transaction != null){
            transactionKey = transaction.getId();
        }
        return transactionKey;
    }

    public void setTransactionKey(Integer transactionKey) {
        this.transactionKey = transactionKey;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaymentKey() {
        return paymentKey;
    }

    public void setPaymentKey(Integer paymentKey) {
        this.paymentKey = paymentKey;
    }

    public Integer getInvoiceKey() {
        return invoiceKey;
    }

    public void setInvoiceKey(Integer invoiceKey) {
        this.invoiceKey = invoiceKey;
    }

    public Integer getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Integer companyKey) {
        this.companyKey = companyKey;
    }
}
