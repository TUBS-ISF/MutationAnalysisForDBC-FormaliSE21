/**
 * A product / service.
 *
 * maps to an Income account.
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 9, 2002
 * Time: 12:36:19 AM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.math.BigDecimal;

public class Product {
    Integer id;
    Integer companyKey;

    String name;
    String description;
    /**
     * product id or serial number
     */
    String code;
    /**
     * price per unit
     */
    BigDecimal rate;

    /**
     * income account
     */
    Integer accountKey;

    boolean taxable;


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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Integer getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(Integer accountKey) {
        this.accountKey = accountKey;
    }

    public boolean isTaxable() {
        return taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public static Product getDefault() {
         Product p = new Product();
        p.setName("");
        p.setDescription("");
        p.setRate(new BigDecimal("0.0"));
        p.setCode("");
        return p;
    }



    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


}
