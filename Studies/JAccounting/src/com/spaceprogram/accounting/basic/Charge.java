/**
 * for invoice charge lines, just a wrapper f
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 9, 2002
 * Time: 12:41:48 AM
 * 
 */
package com.spaceprogram.accounting.basic;


import com.spaceprogram.accounting.common.Recurrence;

import java.util.*;
import java.math.BigDecimal;

public class Charge implements Cloneable {
    Integer id;
    Date created;
    Date updated;

    Integer companyKey;
    Integer customerKey;
    Integer invoiceKey;

    Integer chargeNumber;


    Date chargeDate;

    Set chargeLines = new HashSet();

    String memo;

    Recurrence recurrence;
    /**
     * NOT PERSISTENT
     * used to apply to underlying transactions.
     */
    private String currency;

    public Charge() {
        created = new Date();

    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getInvoiceKey() {
        return invoiceKey;
    }

    public void setInvoiceKey(Integer invoiceKey) {
        this.invoiceKey = invoiceKey;
    }

    public Integer getChargeNumber() {
        return chargeNumber;
    }

    public void setChargeNumber(Integer chargeNumber) {
        this.chargeNumber = chargeNumber;
    }

    public Date getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(Date chargeDate) {
        this.chargeDate = chargeDate;
    }

    public Set getChargeLines() {
        return chargeLines;
    }

    public void setChargeLines(Set chargeLines) {
        this.chargeLines = chargeLines;
    }

    public static Charge getDefault() {
        Charge ret =  new Charge();
        ret.setChargeDate(new Date());
        ret.setMemo("");


        return ret;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public /*@ nullable @*/ChargeLine getChargeLine(Integer clid) {
        Iterator iter = chargeLines.iterator();
//        for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            if(chargeLine.getId().equals(clid)){
                return chargeLine;
            }
        }
        return null;

    }

    /**
     * before tax
     * @return
     */
    public BigDecimal getSubTotal(){

        BigDecimal total = new BigDecimal("0.0");

       Iterator iter = chargeLines.iterator();
//        for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            total = total.add(chargeLine.getAmount());
        }

        return total;
    }

    /**
     * total after tax
     * @param taxes
     */
    public BigDecimal getTotal(List taxes) {
          BigDecimal total = new BigDecimal("0.0");

        Iterator iter = chargeLines.iterator();
//        for (int i = 0; i < chargeLines.size(); i++) {
         while(iter.hasNext()){
             ChargeLine chargeLine = (ChargeLine) iter.next();//(ChargeLine) chargeLines.get(i);
            total = total.add(chargeLine.getAmount());
            if(chargeLine.isTaxable()){
                for (int j = 0; j < taxes.size(); j++) {
                    Tax tax = (Tax) taxes.get(j);
                     total = total.add(chargeLine.getAmount().multiply(tax.getPercent()));
                }

            }
        }

        return total;

    }

    /*public void setRecurrence(Recurrence recurrence) {
        this.recurrence = recurrence;

    }

    public Recurrence getRecurrence() {
        return recurrence;
    }*/

    public void setCurrency(String parameter) {
        this.currency = parameter;
    }

    public String getCurrency() {
        return currency;
    }

    public void addChargeLine(ChargeLine cl) {
        chargeLines.add(cl);

    }
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
