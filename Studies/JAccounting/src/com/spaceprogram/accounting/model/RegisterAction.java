package com.spaceprogram.accounting.model;

import com.spaceprogram.usersystem.UserManager;
import com.spaceprogram.usersystem.impls.hibernate.UserManagerHibernateImpl;
import com.spaceprogram.usersystem.impls.hibernate.UserImpl;
import com.spaceprogram.contacts.Contact;
import com.spaceprogram.contacts.PhoneNumber;
import com.spaceprogram.contacts.StreetAddress;
import com.spaceprogram.contacts.impls.DefaultContactImpl;
import com.spaceprogram.contacts.impls.PhoneNumberImpl;
import com.spaceprogram.contacts.impls.EmailAddressImpl;
import com.spaceprogram.contacts.impls.StreetAddressImpl;

import javax.servlet.http.Cookie;

/**
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Oct 6, 2003
 * Time: 12:09:11 PM
 * @version 0.1
 */
public class RegisterAction extends Register {

    /**
     * the subclasses should implement this, not perform()
     */
    protected String perform() throws Exception {
        super.perform();




        //  todo: move all this into RegisterAction
        String formaction = request.getParameter("formaction");

        if (formaction != null) {
            String returnto = request.getParameter("returnto"); // blank goes to default

            if (formaction.equals("billing")) {


                title = request.getParameter("bill_title");
                firstname = request.getParameter("bill_firstname");
                middlename = request.getParameter("bill_middlename");
                lastname = request.getParameter("bill_lastname");
                email_address = request.getParameter("bill_email_address");
                phone = request.getParameter("bill_phone");
                // fax = request.getParameter("bill_fax");
                address_1 = request.getParameter("bill_address_1");
                address_2 = request.getParameter("bill_address_2");
                address_3 = request.getParameter("bill_address_3");
                // unit_number = request.getParameter("bill_unit_number");
                city = request.getParameter("bill_city");
                province = request.getParameter("bill_province");
                postcode = request.getParameter("bill_postal_code");
                country = request.getParameter("bill_country");



                // set country and province cookies here
/*	Cookie uc = com.spaceprogram.util.CookieUtils.getLongLivedCookie("usercountry", country);
	response.addCookie(uc);
	uc = com.spaceprogram.util.CookieUtils.getLongLivedCookie("userprovince", province);
	response.addCookie(uc);*/

                //String hear_about_us = request.getParameter("hear_about_us");
                /*if(hear_about_us.equals("Other")){
                    hear_about_us = request.getParameter("hear_about_us_other");
                }
                // TO MAKE GENERIC THESE COULD JUST BE ADDED TO A LIST BASED ON ALL THE OTHER
                // QUESTIONS THEY HAVE IN THE DBASE
                String receive_scar = request.getParameter("receive_scar");
                if(receive_scar.equals("Other")){
                    receive_scar = request.getParameter("receive_scar_other");
                }*/
                String notes = ""; //"Heard about us by: " + hear_about_us + "\n\nReceived Scar by: " + receive_scar;


                username = request.getParameter("username");
                password = request.getParameter("password");

                UserManager userManager = UserManagerHibernateImpl.getSingletonInstance();

                com.spaceprogram.usersystem.User u = userManager.getUser(username);
                if (u != null) { // username already taken
                    addError("main","Login name is taken, choose another name.");
                    return ERROR;
                }
                else {


                    u = new UserImpl(username, password);
                    Contact c = new DefaultContactImpl(firstname, lastname);
                    c.setSalutation(title);
                    c.setMiddlename(middlename);
                    c.addEmailAddress(new EmailAddressImpl(email_address));
                    PhoneNumber pn = new PhoneNumberImpl(phone);
                    pn.setLabel("Primary");
                    c.addPhoneNumber(pn);
                    StreetAddress sa = new StreetAddressImpl(address_1, address_2, address_3, city, province, postcode, country);
                    sa.setLabel("Billing");
                    c.addStreetAddress(sa);

                    u.setContact(c);

                    userManager.saveUser(u);




                    //com.spaceprogram.users.User newuser = //Integer.parseInt(com.spaceprogram.util.CookieUtils.getCookieValue(request.getCookies(), "uid", "0"));
                    // need to create user
                    //com.thinkvirtual.User.createNewUserOnly2(factory, conn, username, password, 0, 1);

                    /*if(newuser == null){
                        // error occurred
                        errormsg = "Login name is taken, choose another name.";



                    }
                    else{*/




                    Cookie uc = com.spaceprogram.util.CookieUtils.getLongLivedCookie("username", username);
                    response.addCookie(uc);

                    //int user_id = u.getId();


                    //int newid = com.thinkvirtual.contacts.Contact.insertContact(factory, conn, user_id, title, firstname, middlename, lastname, "");
                    // update if uid > 0
                    /*if(user_id > 0){
                        UpdateQuery uq = factory.getUpdateQuery();
                        uq.setTable("Contacts");
                        uq.addColumn("contact_is_user", user_id);
                        uq.addWhereCondition("contact_id", WhereCondition.EQUAL_TO, newid);
                        uq.execute(conn);
                    }*/


                    //cb_id = newid;

                    //int oid = Integer.parseInt(com.spaceprogram.util.CookieUtils.getCookieValue(request.getCookies(), "oid", "0"));
                    /*ShoppingCart cart = (ShoppingCart)(session.getAttribute("shopping_cart"));
                    if(cart == null){
                        cart = new ShoppingCart(store_id);
                        session.setAttribute("shopping_cart", cart);
                    }
                    cart.setContactKey(factory, conn, cb_id);*/

                    // map contact to order
//com.thinkvirtual.contacts.ContactMapping.insertContactMapping(factory, conn, cb_id, app_id, cart.getOrderID());

                    // now address for billing
                    /*com.thinkvirtual.contacts.Address.insertAddress(factory, conn, user_id, com.thinkvirtual.ThinkVirtual.APP_CONTACTS, newid, "Billing", address_1, address_2, address_3, city, province, postcode, country);

                    if(!email_address.equals("")){
                        com.thinkvirtual.email.EmailAddress.insertEmail(factory,conn, user_id, user_id, com.thinkvirtual.ThinkVirtual.APP_CONTACTS, newid, email_address, false);
                    }
                    if(!phone.equals("")){
                        PhoneNumber.insertPhoneNumber(factory,conn, user_id, com.thinkvirtual.ThinkVirtual.APP_CONTACTS, newid, "Primary", phone, true);
                    }*/
                    /*if(!fax.equals("")){
                        PhoneNumber.insertPhoneNumber(factory,conn, uid, com.thinkvirtual.ThinkVirtual.APP_CONTACTS, newid, "Fax", fax, false);
                    }*/
                    /*
                    SHOULD GO INTO NEW CONTACT AND NEW ADDRESS (BILLING).

                    query1 = "INSERT INTO CustomerBilling ( bill_store_key, bill_title, bill_firstname, bill_middlename, bill_lastname, bill_email_address, "
                        + " bill_phone, bill_fax, bill_address, bill_unit_number, bill_city, bill_province, bill_postal_code, bill_country, bill_notes "
                        + " ) VALUES ( "
                        + store.getID() + ", "
                        + "'" + title + "', "
                        + "'" + firstname + "', "
                        + "'" + middlename + "', "
                        + "'" + lastname + "', "
                        + "'" + email_address + "', "
                        + "'" + phone + "', "
                        + "'" + fax + "', "
                        + "'" + address + "', "
                        + "'" + unit_number + "', "
                        + "'" + city + "', "
                        + "'" + province + "', "
                        + "'" + postal_code + "', "
                        + "'" + country + "', "
                        + "'" + notes + "' )";
                    q.executeStatement(query1);
                    */
                    // now get the billing id to match with the order
                    /*query1 = "SELECT @@identity as newid FROM CustomerBilling ";
                    rs = q.executeQuery(query1);
                    rs.next();
                    int newid = rs.getInt("newid");
                    rs.close();*/
                    /*String same_ship_address = request.getParameter("same_ship_address");
                    if(same_ship_address.equalsIgnoreCase("no")){
                        pool.free(conn);
                        String redir = "customer_form_shipping.jsp?cb_id=" + newid;
                        if(returnto != null){
                            redir += "&returnto=" + returnto;
                        }
                        response.sendRedirect(redir);
                        return;
                    }*/

                    // add user to session

                    session.setMaxInactiveInterval(60 * 60 * 1); // change the last number to set the hours to stay logged in.


                    session.setAttribute("user", u);

                    //conn.close();
                    //response.sendRedirect("main.m");

                }
            }
        }
        return SUCCESS;
    }
}

