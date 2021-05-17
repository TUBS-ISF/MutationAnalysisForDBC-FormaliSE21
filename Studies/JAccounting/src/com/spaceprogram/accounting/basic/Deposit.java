/**
 * For making deposits
 *
 * Deposit To: Asset Account  - so set one line to debit Asset account in Journal Entry transaction
 * From: customer/vendor on each line so addTransaction after creating deposit, same as JournalEntry
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 6, 2002
 * Time: 2:59:02 PM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.Date;

public class Deposit  {
    int id;
    //int accountKey;


    /** underlying journal entry */
    JournalEntry journalEntry;

    /**
     * This is the transaction that will be added to the JournalEntry for the account that the money is being deposited too
     */
    Transaction cashDebit;

    Account depositTo;



    /**
     * new deposit to accountKey account
     * @param depositTo
     */
    public Deposit(Account depositTo, Date date) {
        //super();
        this.depositTo = depositTo;
        //this.accountKey = accountKey;

        // make journal entry
        journalEntry = new JournalEntry();
        journalEntry.setEntryDate(date);
        cashDebit = Transaction.getDefaultTransaction();
        cashDebit.setAccountKey(depositTo.getId());
        //cashDebit.setMemo("Opening Balance");
       journalEntry.addTransaction(cashDebit);
        //journalEntry.setMemo("Opening Balance entry for " + depositTo.getName());
    }

    public JournalEntry getJournalEntry() {

        // update cashDebit transaction value, then return
        cashDebit.setAmount(journalEntry.getCreditTotal());

          return journalEntry;
      }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Account getDepositTo() {
        return depositTo;
    }

    public void setDepositTo(Account depositTo) {
        this.depositTo = depositTo;
    }

    public void setMemo(String s) {
        journalEntry.setMemo(s);
        cashDebit.setMemo(s);
    }

    public void addTransaction(Transaction t) {
        journalEntry.addTransaction(t);
    }


}
