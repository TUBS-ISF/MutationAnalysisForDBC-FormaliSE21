package com.spaceprogram.accounting.basic;

import java.util.List;
/**
 *
 * @author  prophecy
 * @version
 */
/*@nullable_by_default@*/
public class Customer{
	/*@spec_public@*/Integer id;
    Integer companyKey; // company in system that owns this contact

   /* Date created;
    Date updated;*/
	/*String name;
    String description;
    String website;

    int type;
*/
    /**
     * one to one relationship with company from consys/usersys package
     */
    com.spaceprogram.contacts.Company company;


    public Customer() {
        company = new com.spaceprogram.contacts.impls.CompanyImpl();
    }
/*
	public Customer(Long i, String n){
		id = i;
		name = n;
	}
*/
    //@ pre id != null;
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
        return company.getName();
    }

    public void setName(String name) {
        company.setName(name);
    }

    public List getStreetAddresses() {
        return company.getStreetAddresses();
    }

    public void setStreetAddresses(List streetAddresses) {
        company.setStreetAddresses(streetAddresses);
    }

    public List getPhoneNumbers() {
        return company.getPhoneNumbers();
    }

    public void setPhoneNumbers(List phoneNumbers) {
        company.setPhoneNumbers(phoneNumbers);
    }

    public List getEmailAddresses() {
        return company.getEmailAddresses();
    }

    public void setEmailAddresses(List emailAddresses) {
        company.setEmailAddresses(emailAddresses);
    }

    public com.spaceprogram.contacts.Company getCompany() {
        return company;
    }

    public void setCompany(com.spaceprogram.contacts.Company company) {
        this.company = company;
    }


    public static Customer getDefault() {
        Customer cust = new Customer();
        cust.setName("");
        return cust;

    }

}
