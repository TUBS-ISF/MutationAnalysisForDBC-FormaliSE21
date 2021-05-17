/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 3, 2002
 * Time: 2:07:24 PM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.math.BigDecimal;

public class Tax implements Cloneable {

    Integer id;
    Integer companyKey;  
    String name;
    BigDecimal percent;
    /**
     * account that tax will go into
     * ex: GST Payable or Tax Payable
     */
    Integer payableAccountKey;
    /**
     * account that tax will go into to get tax back
     * ex: Tax Receivable (or evne just to the same account as above)
     *
     * if == 0, then you don't deal with receivables, like in the USA,
     * GST might be the only one
     */
   Integer receivableAccountKey;

    /**
     * if 0, then it's a default tax for the company
     */
    Integer invoiceKey;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }



    public static Tax getDefault() {
        Tax t = new Tax();
        t.setName("");
        return t;
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

    public Integer getPayableAccountKey() {
        return payableAccountKey;
    }

    public void setPayableAccountKey(Integer payableAccountKey) {
        this.payableAccountKey = payableAccountKey;
    }

    public Integer getReceivableAccountKey() {
        return receivableAccountKey;
    }

    public void setReceivableAccountKey(Integer receivableAccountKey) {
        this.receivableAccountKey = receivableAccountKey;
    }

    public Integer getInvoiceKey() {
        return invoiceKey;
    }

    public void setInvoiceKey(Integer invoiceKey) {
        this.invoiceKey = invoiceKey;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
