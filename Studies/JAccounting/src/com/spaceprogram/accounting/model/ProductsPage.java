package com.spaceprogram.accounting.model;

import com.spaceprogram.accounting.basic.Product;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import java.math.BigDecimal;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 8
 * @author 2003
 *         Time: 8:25:53 PM
 * @version 0.1
 */
public class ProductsPage extends CompanyCheck {
    Integer id;
    String formaction;

    protected String perform2() throws Exception {

        if (formaction != null) {
            if (formaction.equals("productupdate")) {
                Session sess = getSessionFactory().openSession();
                Transaction tx = null;
                try {
                    tx = sess.beginTransaction();

                    Product product;
                    if(id.intValue() > 0){
                        product = (Product) sess.load(Product.class, id);
                    }
                    else {
                        product = new Product();
                        product.setCompanyKey(cid);
                    }

                    product.setName(request.getParameter("name"));
                    product.setDescription(request.getParameter("description"));
                    product.setRate(new BigDecimal(request.getParameter("rate")));
                    product.setCode(request.getParameter("code"));
                    if (request.getParameter("taxable") != null) {
                        product.setTaxable(true);
                    } else
                        product.setTaxable(false);
                    product.setAccountKey(new Integer(request.getParameter("accountKey")));

                    if(id.intValue() == 0){
                        sess.save(product);
                    }

                    //AccountingPersistenceManager.saveProduct(factory, conn, model.getCid().intValue(), model.getUser().getId().intValue(), product);
                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) tx.rollback();
                    throw e;
                } finally {
                    sess.close();
                }
                return "submitted";


            }
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
