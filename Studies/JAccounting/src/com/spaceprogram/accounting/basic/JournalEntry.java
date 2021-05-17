/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 3, 2002
 * Time: 10:37:13 AM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.*;
import java.math.BigDecimal;

/*@nullable_by_default@*/
public class JournalEntry {
    Integer id;
    Integer companyKey; // mapped to current company records
    Date entryDate;
    Integer entryNumber;

    String memo;

    Collection transactions;

    public JournalEntry() {
        entryDate = new Date();
        transactions = new HashSet();
        entryNumber = new Integer(0);

    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Collection getTransactions() {
        return transactions;
    }

    public void setTransactions(Collection transactions) {
        this.transactions = transactions;
    }

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

    public Integer getEntryNumber() {
        return entryNumber;
    }

    public void setEntryNumber(Integer entryNumber) {
        this.entryNumber = entryNumber;
    }

    public void addTransaction(Transaction trans) {
        transactions.add(trans);
    }

    public static JournalEntry getDefaultJournalEntry() {
        JournalEntry j = new JournalEntry();
        j.setMemo("");
        return j;
    }

    /**
     * Will get all negative values (ie: credits)
     * @return
     */
    public BigDecimal getCreditTotal() {
        BigDecimal ret = new BigDecimal("0.0");
        Iterator iter = transactions.iterator();
        //for (int i = 0; i < transactions.size(); i++) {
        while(iter.hasNext()){



            Transaction transaction = (Transaction) iter.next();
            if(transaction.amount != null){
                if(transaction.getAmount().compareTo(new BigDecimal(0.0)) < 0){
                    ret = ret.add(transaction.getAmount());
                }
            }
        }

        //}
        return ret;
    }
    /**
     *  opposite of creditTotal
     */
    public BigDecimal getDebitTotal() {
        BigDecimal ret = new BigDecimal("0.0");
        Iterator iter = transactions.iterator();
        //for (int i = 0; i < transactions.size(); i++) {
        while(iter.hasNext()){
            Transaction transaction = (Transaction)iter.next();
            if(transaction.getAmount().compareTo(new BigDecimal(0.0)) > 0){
                ret = ret.add(transaction.getAmount());
            }
        }
        return ret;
    }

}
