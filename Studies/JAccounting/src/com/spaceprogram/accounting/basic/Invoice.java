/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 3, 2002
 * Time: 11:10:29 AM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.*;
import java.math.BigDecimal;

/*@nullable_by_default@*/
public class Invoice  {

    Integer id;
    Date created;
    Date updated;
    Integer companyKey;
    Integer customerKey;
    /** Name, address etc in text box for printing/sending */
    String billTo;
    Integer invoiceNumber;
    Date invoiceDate;
    Date dueDate;


    String customerMessage;

    boolean toBePrinted;
    boolean toBeSent;
    /**
     * Email address to send this invoice too
     */
    int sendToEmailKey;

    int templateKey;

    /** Contains a list of all taxes that are to be charged on this invoice */
    List taxes;

    JournalEntry journalEntry;
    /**
     * charge lines that have a key to the transactions in the journalEntry
     */
    Set charges;

    BigDecimal amount;
    /**
     * This is a running total of how much of this invoice has been paid.  When this
     * gets to the invoiceAmount, then the invoice will be flagged as PAID
     */
    BigDecimal amountPaid;

    boolean paid;

    public Invoice() {
        charges = new HashSet();
    }

    public static Invoice getDefault() {

        Invoice i = new Invoice();
        Calendar cal = Calendar.getInstance();

        i.setInvoiceDate(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        i.setDueDate(cal.getTime());

        i.setJournalEntry(JournalEntry.getDefaultJournalEntry());
        i.setCustomerMessage("");

        i.taxes = new ArrayList();


        return i;
    }


    public String getBillTo() {
        return billTo;
    }

    public void setBillTo(String billTo) {
        this.billTo = billTo;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getCustomerMessage() {
        return customerMessage;
    }

    public void setCustomerMessage(String customerMessage) {
        this.customerMessage = customerMessage;
    }

    public boolean isToBePrinted() {
        return toBePrinted;
    }

    public void setToBePrinted(boolean toBePrinted) {
        this.toBePrinted = toBePrinted;
    }

    public boolean isToBeSent() {
        return toBeSent;
    }

    public void setToBeSent(boolean toBeSent) {
        this.toBeSent = toBeSent;
    }

    public List getTaxes() {
        return taxes;
    }

    public void setTaxes(List taxes) {
        this.taxes = taxes;
    }

    /**
     * Has to set up the journal entry to be correct
     *
     * Debit A/R for entire invoice amount
     * Credit each account that each product beIntegers too - ie: all the chargeline transactions go the
     * appropriate product account.
     * and possibly some goes to the tax accounts?
     * @return
     */
    public JournalEntry getJournalEntry() {
      /*  double total = 0.0;
        // get transactions
        for (int i = 0; i < charges.size(); i++) {
            Charge charge = (Charge) charges.get(i);
            List chargeLines = charge.getChargeLines();
            for (int j = 0; j < chargeLines.size(); j++) {
                ChargeLine chargeLine = (ChargeLine) chargeLines.get(j);
                Transaction t = chargeLine.getTransaction();
                total += t.getAmount();
                journalEntry.addTransaction(t);

            }
        }
        amount = total;
      // do A/R in persistenceMAnager?*/
        journalEntry.setCompanyKey(companyKey);


        return journalEntry;
    }

    public void setJournalEntry(JournalEntry journalEntry) {
        this.journalEntry = journalEntry;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(Date invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public int getSendToEmailKey() {
        return sendToEmailKey;
    }

    public void setSendToEmailKey(int sendToEmailKey) {
        this.sendToEmailKey = sendToEmailKey;
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

    public void setMemo(String memo){
        journalEntry.setMemo(memo);
    }
    public String getMemo(){
        return journalEntry.getMemo();
    }

    public Collection getCharges() {
        return charges;
    }

    public void setCharges(Set charges) {
        this.charges = charges;
    }
    public void addCharge(Charge charge){
        charges.add(charge);
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
       // journalEntry.setCompanyKey(companyKey);
    }

    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

    /**
     *
     * @return total non taxable + (total taxable * taxes)
     */
    public BigDecimal getTotal() {
         //NEED TO DO THIS, IT'S ALREADY ON INVOICE.JSP, SO MAYBE JUST DO WHAT IT'S DOING IN HERE?
        BigDecimal total = new BigDecimal("0.0");
        Iterator iter = charges.iterator();
        while(iter.hasNext()){
        //for (int i = 0; i < charges.size(); i++) {
            Charge charge = (Charge)iter.next();
            total = total.add(charge.getTotal(taxes));
        }

        


        return total;
    }

    public int getTemplateKey() {
        return templateKey;
    }

    public void setTemplateKey(int templateKey) {
        this.templateKey = templateKey;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * by tax id
     * @param t
     */
    public void addTax(Tax t) {
        taxes.add(t);
    }

}
