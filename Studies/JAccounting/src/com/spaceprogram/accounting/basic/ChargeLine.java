/**
 * A charge has a bunch of ChargeLines that are the actual charges
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 14, 2002
 * Time: 3:46:29 PM
 * 
 */
package com.spaceprogram.accounting.basic;

import java.util.Date;
import java.math.BigDecimal;
/*@ nullable_by_default@*/
public class ChargeLine implements Cloneable{
    Integer id;
    Integer chargeKey;
     BigDecimal quantity;
     BigDecimal rate;
    Product product;
    Integer productKey;
    
    boolean taxable;

    // underlying transaction for journal entry
    Transaction transaction;

     public  BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity( BigDecimal quantity) {
        this.quantity = quantity;

    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;


    }
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
        this.productKey = product.getId();
        if(transaction != null){
        transaction.setAccountKey(product.getAccountKey());
        }
    }


    public boolean isTaxable() {
       return this.taxable;
    }

    public void setTaxable(boolean taxable) {
        this.taxable = taxable;
    }

    public Transaction getTransaction() {
        transaction.setAmount(quantity.multiply(rate));
        transaction.setAccountKey(product.getAccountKey());
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
    public void setDescription(String memo){
        transaction.setMemo(memo);
    }
    public String getDescription(){
        return transaction.getMemo();
    }
     public int getClassKey() {
        return transaction.getClassKey();
    }

    public void setClassKey(int classKey) {
        transaction.setClassKey(classKey);
    }


    public static ChargeLine getDefault() {
        ChargeLine ret =  new ChargeLine();
        ret.setTransaction(Transaction.getDefaultTransaction());
        ret.setTaxable(true);
        return ret;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getChargeKey() {
        return chargeKey;
    }

    public void setChargeKey(Integer chargeKey) {
        this.chargeKey = chargeKey;
    }

    public Integer getProductKey() {
        return productKey;
    }

    public void setProductKey(Integer productKey) {
        this.productKey = productKey;
    }

    public void setTransactionDate(Date chargeDate) {
        transaction.setTransactionDate(chargeDate);
    }
    public Date getTransactionDate(){
        return transaction.getTransactionDate();

    }
    public BigDecimal getAmount(){
        return rate.multiply(quantity);
    }

    public void setCurrency(String currency) {
        transaction.setCurrency(currency);
    }
    public String getCurrency(){
        return transaction.getCurrency();
        
    }

    public void setCompanyKey(Integer cid) {
        transaction.setCompanyKey(cid);
    }

	/*@ non_null @*/
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
