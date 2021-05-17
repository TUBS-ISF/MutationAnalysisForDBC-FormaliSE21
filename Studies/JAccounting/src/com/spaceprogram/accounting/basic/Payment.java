/**
 * 
 * @author Travis reeder
 * Date: Jan 9, 2003
 * Time: 3:29:27 PM
 * @version 0.1
 */
package com.spaceprogram.accounting.basic;

import java.util.*;
import java.math.BigDecimal;

/*@nullable_by_default@*/
public class Payment {
    Integer id;
    Date created;
    Date updated;

    Integer companyKey;
    Integer customerKey;

    Integer paymentNumber;
    String referenceNumber;  // for a number that is on the payment cheque?
    Date paymentDate;
    BigDecimal amount;
    /**
     * account where payment is to be deposited
     */
    Integer depositAccountKey;

     JournalEntry journalEntry;
    /**
     * distribution lines that have a key to the transactions in the journalEntry
     * as well as which invoice they are being applied to.
     *
     * full of PaymentDistribution objects
     */
    Set distributionLines;

    //String memo;

    public Payment() {

    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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

    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

    public Integer getPaymentNumber() {
        return paymentNumber;
    }

    public void setPaymentNumber(Integer paymentNumber) {
        this.paymentNumber = paymentNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
        journalEntry.setEntryDate(paymentDate);
    }

    public JournalEntry getJournalEntry() {
        journalEntry.setCompanyKey(companyKey);
        return journalEntry;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    public Set getDistributionLines() {
        return distributionLines;
    }

    public void setDistributionLines(Set distributionLines) {
        this.distributionLines = distributionLines;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static Payment getDefault(Integer cid) {
        Payment p = new Payment();
        p.setJournalEntry(JournalEntry.getDefaultJournalEntry());
        p.setCompanyKey(cid);
        p.getJournalEntry().setCompanyKey(cid);
        p.setDistributionLines(new HashSet());

        p.setAmount(new BigDecimal(0.0));
        p.setPaymentDate(new Date());
        p.setReferenceNumber("");
        p.setMemo("");
        return p;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public String getMemo() {
        return journalEntry.getMemo();
    }

    public void setMemo(String memo) {
        journalEntry.setMemo(memo);
    }

    public Integer getDepositAccountKey() {
        return depositAccountKey;
    }

    public void setDepositAccountKey(Integer depositAccountKey) {
        this.depositAccountKey = depositAccountKey;
    }

    public void addDistribution(PaymentDistribution pd) {
        Transaction t = pd.getTransaction();
        //t.setAccountKey(depositAccountKey);
        t.setJournalEntryKey(journalEntry.getId());
        distributionLines.add(pd);
        journalEntry.addTransaction(pd.getTransaction());
    }

    /**
     * this is so we don't have duplicate transactions, one in the JE and one in each distro line
     */
    public void matchDistributionLinesToTransactions() {
        // iterate through distro lines and find matching trans
        Collection transactions = journalEntry.getTransactions();
        Iterator iter = distributionLines.iterator();
        while (iter.hasNext()) {
            PaymentDistribution paymentDistribution = (PaymentDistribution) iter.next();
            // now find transaction
            Iterator iter2 = transactions.iterator();
            while (iter2.hasNext()) {
                Transaction transaction = (Transaction) iter2.next();
                if(transaction.getId().equals( paymentDistribution.getTransactionKey())){
                    // found, so match and break
                    System.out.println("MATCHED! trans: " + transaction.getId() + " pd: " + paymentDistribution.getId());

                    paymentDistribution.setTransaction(transaction);
                    break;
                }
            }

        }
    }


}
