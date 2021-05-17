/**
 * For JournalEntry distribution lines
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 3, 2002
 * Time: 10:57:06 AM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.Date;
import java.math.BigDecimal;

/*@ nullable_by_default@*/
public class Transaction implements Cloneable {

    Integer id;
    Integer companyKey; // mapped to current company records
    Integer journalEntryKey = new Integer(0);
    Integer accountKey = new Integer(0);
    BigDecimal amount;

    String memo;

    Integer customerKey = new Integer(0); // mapped to client/vendor/employee or whatever
    int classKey;

    //boolean taxable;


    /**
     * example: payment method
     */
    int method;

    //int refNumber;

    Date transactionDate;

    String currency;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompanyKey() {
        return companyKey;
    }

    public void setCompanyKey(Integer companyKey) {
        this.companyKey = companyKey;
    }

    public Integer getJournalEntryKey() {
        return journalEntryKey;
    }

    public void setJournalEntryKey(Integer journalEntryKey) {
        this.journalEntryKey = journalEntryKey;
    }

    public Integer getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(Integer accountKey) {
        this.accountKey = accountKey;
    }

    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }



    public int getClassKey() {
        return classKey;
    }

    public void setClassKey(int classKey) {
        this.classKey = classKey;
    }

   /* public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }*/

    public static Transaction getDefaultTransaction() {
        Transaction t = new Transaction();
        t.setTransactionDate(new Date());
        t.setMemo("");
        return t;
    }

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    /*public int getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(int refNumber) {
        this.refNumber = refNumber;
    }*/
	/*@ non_null @*/
    public String toString(){
        return "accountKey=" + accountKey + " companyKey=" + companyKey + "journalEntryKey=" + journalEntryKey + "Amount="+amount + " memo:" + memo;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

      public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
	
	//@ also
    //@ signals_only CloneNotSupportedException;
	/*@ non_null @*/
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
