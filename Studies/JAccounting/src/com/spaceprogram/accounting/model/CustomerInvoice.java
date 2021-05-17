package com.spaceprogram.accounting.model;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 17
 * @author 2003
 *         Time: 12:59:40 PM
 * @version 0.1
 */
public class CustomerInvoice extends CompanyCheck{
    Integer id;
    protected String perform2() throws Exception {
        return SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
