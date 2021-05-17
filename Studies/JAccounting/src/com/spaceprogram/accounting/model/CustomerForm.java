package com.spaceprogram.accounting.model;

import com.spaceprogram.accounting.basic.Customer;
import net.sf.hibernate.Session;

import java.util.Date;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: 22-Dec-2003
 *         Time: 11:54:49 AM
 * @version 0.1
 */
public class CustomerForm extends CompanyCheck{
    Long id;
    String formaction;
    Customer customer;
    protected String perform2() throws Exception {
         if(formaction != null){
            if(formaction.equals("update")){
            // then we'll save/update this object.
              Session sess = getSessionFactory().openSession();
           net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();

                     Date now = new Date();
                    if(id.longValue() > 0){

                           customer = (Customer)sess.load(Customer.class, id);
                       }
                       else{
                           customer  = new Customer();
                        customer.setCompanyKey(cid);

                       }




                    customer.setName(request.getParameter("name"));






                    if(id.longValue() == 0){

                        sess.save(customer); // save main customer/company class


                        /*// deal with accounting vars
                          boolean acct_customer = ServletUtils.getBooleanParameter(request, "acct_customer");
                          boolean acct_vendor = ServletUtils.getBooleanParameter(request, "acct_vendor");
                          if(acct_customer){
                              //AccountingPersistenceManager.addCustomer(factory, conn, cid, uid, customer_id);
                              // todo: should look to see if already set as a customer
                              AccountingCustomer ac = new AccountingCustomer();
                              ac.setCustomer(customer);
                              ac.setType(AccountingPersistenceManager.CUSTOMER);
                              sess.save(ac);

                          }
                          if(acct_vendor){
                              //AccountingPersistenceManager.addVendor(factory, conn, cid, uid, customer_id);
                              // todo: should look to see if already set as a customer
                               AccountingCustomer ac = new AccountingCustomer();
                              ac.setCustomer(customer);
                              ac.setType(AccountingPersistenceManager.VENDOR);
                              sess.save(ac);
                          }*/



                    }

                    // 5. commit transaction
                    tx.commit();
                }
                catch (Exception e) {
                   //if (tx!=null) tx.rollback();
                    throw e;
                }
                finally {
                    sess.close();
                    //pool.free(conn);
                }
                response.sendRedirect("customer_details.jsp?id=" + customer.getId());

            }



             getCtx().setViewParam("id", id);
                return "submitted";
        }
        return SUCCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }
}

