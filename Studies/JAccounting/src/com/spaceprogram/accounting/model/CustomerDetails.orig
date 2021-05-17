package com.spaceprogram.accounting.model;


import com.spaceprogram.accounting.basic.Customer;
import com.spaceprogram.contacts.EmailAddress;
import com.spaceprogram.contacts.PhoneNumber;
import com.spaceprogram.contacts.StreetAddress;
import com.spaceprogram.contacts.impls.EmailAddressImpl;
import com.spaceprogram.contacts.impls.PhoneNumberImpl;
import com.spaceprogram.contacts.impls.StreetAddressImpl;
import com.spaceprogram.util.ServletUtils;
import net.sf.hibernate.Session;

import java.util.List;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 16
 * @author 2003
 *         Time: 3:32:09 PM
 * @version 0.1
 */
public class CustomerDetails extends CompanyCheck {
    Integer id;
    String formaction;
    protected String perform2() throws Exception {
        if (formaction != null) {
            String realm = "companies";
            Session sess = getSessionFactory().openSession();
            net.sf.hibernate.Transaction tx = null;
            try {
                tx = sess.beginTransaction();

                if (formaction.equals("phone")) {
                    // could check pn_id here to see if greater than 0, if so update
                    
                    int pnid = ServletUtils.getIntParameter(request, "pnid", 0);
                    PhoneNumber pn;
                    if (pnid > 0) {
                        pn = (PhoneNumber) sess.load(PhoneNumberImpl.class, new Integer(pnid));
                    } else {
                        pn = new PhoneNumberImpl();
                    }
                    //PhoneNumber.insertPhoneNumber(q, uid2, app_id, customer_id, request.getParameter("label"), request.getParameter("number"));
                    //pn.setAppKey(app_key);
                    //pn.setItemKey(customer_id);
                    pn.setRealm(realm);
                    pn.setContactKey(id);
                    pn.setLabel(request.getParameter("label"));
                    pn.setNumber(request.getParameter("number"));

                    if (pnid == 0) {
                        sess.save(pn);
                    }
                    // redir += "&item_id=" + customer_id;

                } else if (formaction.equals("address")) {

                    int aid = ServletUtils.getIntParameter(request, "address_id", 0);
                    StreetAddress a;
                    if (aid > 0) {
                        //Address.updateAddress(q, uid2, aid, request.getParameter("name"), request.getParameter("address_1"), request.getParameter("address_2"), request.getParameter("address_3"), request.getParameter("city"), request.getParameter("province"), request.getParameter("postcode"), request.getParameter("country"));
                        a = (StreetAddress) sess.load(StreetAddressImpl.class, new Integer(aid));

                    } else {
                        //Address.insertAddress(q, uid2, app_key, customer_id, request.getParameter("name"), request.getParameter("address_1"), request.getParameter("address_2"), request.getParameter("address_3"), request.getParameter("city"), request.getParameter("province"), request.getParameter("postcode"), request.getParameter("country"));
                        a = new StreetAddressImpl();
                    }
                    //a.setAppKey(app_key);
                    a.setRealm(realm);
                    a.setContactKey(id);
                    a.setLabel(request.getParameter("name"));
                    a.setStreet1(request.getParameter("address_1"));
                    a.setStreet2(request.getParameter("address_2"));
                    a.setStreet3(request.getParameter("address_3"));
                    a.setCity(request.getParameter("city"));
                    a.setProvince(request.getParameter("province"));
                    a.setPostcode(request.getParameter("postcode"));
                    a.setCountry(request.getParameter("country"));

                    if (aid == 0) {
                        sess.save(a);
                    }
                    //redir += "&item_id=" + customer_id;
                } else if (formaction.equals("email")) {
                    Integer aid = new Integer(request.getParameter("email_id"));

                    Customer customer = (Customer) sess.load(Customer.class, id);

                    EmailAddress ea = null;
                    if (aid.intValue() > 0) {
                        //EmailAddress.updateEmail(q, uid2, aid, app_id_companies, customer_id, request.getParameter("email"), false);
                        // probably a more direct way to do this
                        List emailAddresses = customer.getEmailAddresses();
                            for (int i = 0; i < emailAddresses.size(); i++) {
                                EmailAddress ea2 = (EmailAddress) emailAddresses.get(i);
                                if(ea2.getId().intValue() == aid.intValue()){
                                    ea = ea2;
                                    break;
                                }
                            }
                        //ea = (EmailAddress) sess.load(EmailAddress.class, new Integer(aid));
                    } else {
                        //EmailAddress.insertEmail(q, uid2, app_id_companies, customer_id, request.getParameter("email"), false);
                        ea = new EmailAddressImpl();
                    }

                    ea.setRealm(realm); // todo: can take this line out soon because this is set when adding to the company in usersys
                    
                    //ea.setContactKey(id);
                    ea.setEmail(request.getParameter("email"));

                    if (aid.intValue() == 0) {
                        customer.getCompany().addEmailAddress(ea);
                    }
                    //redir += "&item_id=" + customer_id;
                } else if (formaction.equals("delete2")) {
                    String temp2 = request.getParameter("pn_id");
                    if (temp2 != null) {
                        // then delete phone number
                        //PhoneNumber.deletePhoneNumber(q, uid2, Integer.parseInt(temp2));
                        sess.delete("from pn in class " + PhoneNumberImpl.class
                                + " where pn.id = " + temp2);
                    }
                    temp2 = request.getParameter("email_id");
                    if (temp2 != null) {
                        //EmailAddress.deleteEmailAddress(q, uid2, Integer.parseInt(temp2));
                        sess.delete("from ea in class " + EmailAddressImpl.class
                                + " where ea.id = " + temp2);
                    }
                    //redir += "&item_id=" + customer_id;

                } else if (formaction.equals("makeprimary")) {
                     Customer customer = (Customer) sess.load(Customer.class, id);
                    //int aid = ServletUtils.getIntParameter(request, "address_id", 0);
                    String temp2 = request.getParameter("email_id");
                    if (temp2 != null) {
                        Integer toChange = new Integer(temp2);
                        //PhoneNumber.makePrimary(factory, conn, uid, app_key, customer_id, Integer.parseInt(temp2));
                        // todo: move to ContactManager.makePrimary or something because this should be available anywhere
                        List pns = customer.getPhoneNumbers();
                        for (int i = 0; i < pns.size(); i++) {
                            PhoneNumber phoneNumber = (PhoneNumber) pns.get(i);
                            if(phoneNumber.getId().equals(toChange)){
                                phoneNumber.setIsPrimary(true);
                            }
                            else {
                                phoneNumber.setIsPrimary(false);
                            }
                        }
                    }
                    temp2 = request.getParameter("email_id");
                    if (temp2 != null) {
                        Integer toChange = new Integer(temp2);
                        //PhoneNumber.makePrimary(factory, conn, uid, app_key, customer_id, Integer.parseInt(temp2));
                        // todo: move to ContactManager.makePrimary or something because this should be available anywhere
                        List pns = customer.getEmailAddresses();
                        for (int i = 0; i < pns.size(); i++) {
                            EmailAddress phoneNumber = (EmailAddress) pns.get(i);
                            if(phoneNumber.getId().equals(toChange)){
                                phoneNumber.setIsDefault(true);
                            }
                            else {
                                phoneNumber.setIsDefault(false);
                            }
                        }
                    }

                }

                tx.commit();
            } catch (Exception e) {
                //if (tx!=null) tx.rollback(); // breaks mysql
                throw e;
            } finally {
                sess.close();

            }
            getCtx().setViewParam("id", id);
            return "submitted";
        }

        if (id == null || id.intValue() <= 0) {
            return "norecord";
        }


        return SUCCESS;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }
}
