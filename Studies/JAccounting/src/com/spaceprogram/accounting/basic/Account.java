/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 4, 2002
 * Time: 2:11:58 PM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.Date;
import java.math.BigDecimal;

/*@nullable_by_default@*/
public class Account {
	/*@ spec_public @*/Integer id;

	/*@ spec_public @*/Integer companyKey;
    /*@ spec_public @*/Date created;

    public String name;
    public BigDecimal balance;
    public String description;

    /** > 0 for subaccounts */
    /*@ spec_public @*/Integer parentKey;

    /**
     * Asset
     * Liability
     * Expense
     * Sales
     * Equity
     * Non-current Asset
     * Non-current Liability
     */
    /*@ spec_public @*/int typeKey;

    /** to get more specific
     * ex: Expense -> Detail = Advertising/Promotianal or Detail = Auto or bad debt, etc.
     */
    /*@ spec_public @*/int detailTypeKey;

    /*@ spec_public @*/int internalId;

    /*@ spec_public @*/String currency;

    //@ ensures \result == this.created;
    public Date getCreated() {
        return created;
    }

    //@ requires true;
    //@ post this.created == created;
    public void setCreated(Date created) {
        this.created = created;
    }

    //@ post \result instanceof java.lang.String;
    //@ post \result.equals(this.name);
    public String getName() {
        return name;
    }

    //@ requires name != null ==> !name.equals("");
    //@ ensures (name != null ==> !name.equals("")) <==> !this.name.equals("");
    public void setName(String name) {
        this.name = name;
    }

    //@ ensures \result == this.balance;
    public BigDecimal getBalance() {
        return balance;
    }

    //@ ensures this.balance == balance;
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    //@ ensures \result.equals(this.description);
    public String getDescription() {
        return description;
    }

    //@ ensures this.description == description;
    public void setDescription(String description) {
        this.description = description;
    }

    //@ requires true;
    //@ ensures typeKey >= 0;
    public int getTypeKey() {
        return typeKey;
    }

    //@ requires typeKey >= 0;
    //@ ensures this.typeKey == typeKey;
    public void setTypeKey(int typeKey) {
        this.typeKey = typeKey;
    }

    //@ requires true;
    //@ ensures \result == this.id;
    public Integer getId() {
        return id;
    }

    //@ requires true;
    //@ ensures this.id == id;
    public void setId(Integer id) {
        this.id = id;
    }

    //@ requires true;
    //@ ensures \result == this.companyKey;
    public Integer getCompanyKey() {
        return companyKey;
    }

    //@ requires true;
    //@ ensures this.companyKey == companyKey;
    public void setCompanyKey(Integer companyKey) {
        this.companyKey = companyKey;
    }

    //@ requires true;
    //@ ensures \result == parentKey;
    public Integer getParentKey() {
        return parentKey;
    }

    //@ requires true;
    //@ ensures this.parentKey == parentKey;
    public void setParentKey(Integer parentKey) {
        this.parentKey = parentKey;
    }

    //@ pre true;
    //@ ensures (\result >= 0) ==> (this.detailTypeKey >= 0);
    public int getDetailTypeKey() {
        return detailTypeKey;
    }

    //@ pre true;
    //@ ensures this.detailTypeKey == detailTypeKey;
    public void setDetailTypeKey(int detailTypeKey) {
        this.detailTypeKey = detailTypeKey;
    }

    //@ requires true;
    //@ ensures \result != null;
    public static Account getDefault() {
        Account a = new Account();
        a.setTypeKey(1);
        return a;
    }

    //@ pre true;
    //@ post true;
    public Account() {
    }

    //@ requires true;
    //@ ensures \result == this.internalId;
    public int getInternalId() {
        return internalId;
    }

    //@ requires true;
    //@ ensures this.internalId == internalId;
    public void setInternalId(int internalId) {
        this.internalId = internalId;
    }

    //@ requires true;
    //@ ensures \result.equals(currency);
    public String getCurrency() {
        return currency;
    }

    //@ requires true;
    //@ ensures this.currency == currency;
    public void setCurrency(String currency) {
        this.currency = currency;
    }


}
