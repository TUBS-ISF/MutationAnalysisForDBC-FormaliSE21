package com.spaceprogram.accounting.model;

import com.crossdb.sql.SelectQuery;
import com.crossdb.sql.WhereCondition;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.Tax;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import com.spaceprogram.accounting.html.reports.CustomerInvoice;
import com.spaceprogram.util.ServletUtils;
import com.spaceprogram.util.Mailer;
import com.spaceprogram.util.WebappProperties;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 7
 * @author 2003
 *         Time: 5:10:52 PM
 * @version 0.1
 */
public class InvoicePage extends CompanyCheck {
    private String formaction;
    Integer id;
    Integer customerKey;
    Integer invoiceNumber;

    protected String perform2() throws Exception {
        if (formaction != null) {
            if (formaction.equals("invoice")) {

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
                Invoice invoice;
                Session sess = getSessionFactory().openSession();
                net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();
                    if (id.intValue() > 0) {
                        invoice = (Invoice) sess.load(Invoice.class, id);
                        customerKey = invoice.getCustomerKey();
                    } else {
                        invoice = Invoice.getDefault();
                        invoice.setCustomerMessage("Thank you for your business!");
                    }

                    // then get invoice details and save
                    //invoice = new Invoice();
                    //invoice.setId(id);
                    invoice.setInvoiceDate(sdf.parse(request.getParameter("invoiceDate")));
                    invoice.setDueDate(sdf.parse(request.getParameter("dueDate")));
                    invoice.setInvoiceNumber(invoiceNumber);
                    invoice.setCustomerMessage(request.getParameter("customerMessage"));
                    invoice.setMemo(request.getParameter("memo"));
                    invoice.setToBePrinted(ServletUtils.getBooleanParameter(request, "toBePrinted"));
                    invoice.setToBeSent(ServletUtils.getBooleanParameter(request, "toBeSent"));
                    invoice.setCustomerKey(customerKey);
                    invoice.setSendToEmailKey(ServletUtils.getIntParameter(request, "sentToEmail", 0));
                    invoice.setBillTo(request.getParameter("billTo"));
                    invoice.setTemplateKey(ServletUtils.getIntParameter(request, "templateKey", 0));
                    invoice.setCompanyKey(cid);

                    invoice.setAmount(invoice.getTotal());
                    invoice.setUpdated(new Date());

                    // AccountingPersistenceManager.saveInvoice(factory, conn, cid, uid, invoice);

                    if (id.intValue() == 0) {
                        invoice.setCreated(new Date());
                        invoice.setAmountPaid(new BigDecimal(0.0));
                        sess.save(invoice);
                    }


                    // now save chosen taxes
                    /**
                     * there is a couple things that can happen here:
                     * 1. chose a default tax to add to invoice so it should be duped
                     * 2. an already duped tax just resubmitting, so ignore it
                     * 3. entered a new tax just for this invoice
                     *
                     * todo: should delete ones that were unclicked
                     * todo: save tax amount to receivable and/or payable accounts as transactions
                     */
                    String[] taxesForInvoice = request.getParameterValues("tax");
                    if (taxesForInvoice != null) {
                        List newtaxes = new ArrayList();
                        List taxes = invoice.getTaxes();
                        for (int i = 0; i < taxesForInvoice.length; i++) {
                            boolean alreadyAttached = false;
                            String s = taxesForInvoice[i];
                            Integer taxKey = new Integer(s);
                            // now dupe the tax or take the freshly added one

                            // go through tax list and see if already there
                            for (int j = 0; j < taxes.size(); j++) {

                                Tax tax = (Tax) taxes.get(j);
                                if (tax.getId().intValue() == taxKey.intValue()) {
                                    // then already attached
                                    alreadyAttached = true;
                                    break;
                                }
                            }

                            // out.print("Already: " + alreadyAttached + " taxKey=" + taxKey);out.flush();
                            if (alreadyAttached) {
                                // then already attached, so do nothing


                            } else {
                                Tax toDupe = (Tax) sess.load(Tax.class, taxKey); // load up a default tax
                                // not attached so must be a default tax so dupe it
                                //out.print("making new tax "+ invoice.getId()); out.flush();
                                Tax newTax = (Tax) toDupe.clone();
                                //newTax.setInvoiceKey(invoice.getId());
                                newTax.setId(null);
                                //sess.save(newTax);
                                newtaxes.add(newTax);
                                sess.evict(toDupe);

                            }
                            // word 


                            //invoice.addTax(toDupe);
                        }
                        //sess.flush();
                        // now remove all the ones that would have been unchecked which would be all the ones that did not get set above...

                        // so we'll rifle through this and compare with incoming checkboxes
                        boolean allgood = false;
                        for (int i = 0; i < taxes.size(); i++) {
                            allgood = false; // initialize for each tax
                            Tax tax = (Tax) taxes.get(i);
                            for (int j = 0; j < taxesForInvoice.length; j++) {
                                String s = taxesForInvoice[j];
                                Integer taxKey = new Integer(s);
                                // so now if taxKey matches the tax id, then break out, if not, then remove the tax
                                if (taxKey.equals(tax.getId())) {
                                    // then there is a matching checkbox
                                    allgood = true;
                                    break;
                                }
                            }
                            if (!allgood) {
                                // then kill this tax
                                sess.delete(taxes.remove(i));
                                i--;
                                //sess.delete(tax);
                            }

                        }
                        //sess.flush();
                        // now add the new taxes back in
                        taxes.addAll(newtaxes);
                    } else {// no taxes selected so delete any that are attached
                        List taxes = invoice.getTaxes();
                        for (int i = 0; i < taxes.size(); i++) {
                            // Tax tax = (Tax) taxes.get(i);
                            //sess.delete(tax);
                            sess.delete(taxes.remove(i));
                            i--;

                        }

                        // sess.flush();

                    }
                    //out.print("<br>\nAfter all taxes, tax list size=" + invoice.getTaxesForInvoices().size());out.flush();







                    // now update charges
                    // update all charges that are part of the invoice to not be part of the invoice
                    AccountingPersistenceManager.removeChargesFromInvoice(sess, invoice);
                    // then go through new values and update to be part of invoice
                    String params[] = request.getParameterValues("charges");
                    if (params != null) {
                        for (int i = 0; i < params.length; i++) {
                            String param = params[i];
                            //
                            AccountingPersistenceManager.addChargeToInvoice(sess, invoice, new Integer(param));
                        }
                    }

                    // check to see if needs sending
                    if (invoice.isToBeSent()) {
                        if (request.getParameter("savesend") != null) {
                            // then send
                            String sendToEmailStr = request.getParameter("sendToEmail");
                            CustomerInvoice customerInvoice = new CustomerInvoice();
                            String customerInvoiceStr = customerInvoice.createInvoice(factory, sess, cid, invoice);
                            // get fromEmail
                            String fromEmail = AccountingPersistenceManager.getPreferences(factory, sess.connection(), cid).getFromEmail();
//out.print(fromEmail); out.flush();
                            String compname = null;
                            Connection conn = sess.connection();

                            SelectQuery sq = factory.getSelectQuery();
                            sq.addTable("Companies");
                            sq.addWhereCondition("company_id", WhereCondition.EQUAL_TO, getCid().intValue());
                            ResultSet rs = sq.execute(conn);
                            if (rs.next()) {
                                compname = rs.getString("company_name");
                            }
                            rs.close();

                            WebappProperties props = WebappProperties.getSingletonInstance();
                            String bcc = props.getProperty("bccEmail"); // todo, put this in preferences table instead
                            if (bcc == null) bcc = "";
                            Mailer mailer = new Mailer(props.getProperty("smtpServer"), sendToEmailStr, fromEmail, "", bcc, "Invoice from " + compname);
                            //mailer.setContentType(Mailer.CTYPE_HTML);
                            mailer.setLogin(props.getProperty("smtpUsername"), props.getProperty("smtpPassword"));

                            mailer.setBody(invoice.getCustomerMessage());
                            mailer.addAttachmentFromString(customerInvoiceStr, "text/html", "Invoice_" + invoice.getInvoiceNumber() + ".html");
                            mailer.send();


                        }
                    }


                    tx.commit();
                } catch (Exception e) {
                    try {
                        tx.rollback();
                    } catch (HibernateException e1) {
                        e1.printStackTrace();
                    }
                    throw e;
                } finally {
                    sess.close();
                }


                getCtx().setViewParam("id", invoice.getId());
                return "submitted";

            } else if (formaction.equals("delinvoice")) {
                Session sess;
                sess = getSessionFactory().openSession();
                net.sf.hibernate.Transaction tx = null;
                try {
                    tx = sess.beginTransaction();
                    // do some work
                    Invoice invoice = (Invoice) sess.load(Invoice.class, id);
                    sess.delete(invoice);

                    tx.commit();
                } catch (Exception e) {
                    if (tx != null) {
                        try {
                            tx.rollback();
                        } catch (HibernateException e1) {
                            e1.printStackTrace();
                        }
                    }
                    throw e;
                } finally {
                    sess.close();
                }

                getCtx().setViewParam("id=", customerKey);
                return "deleted";
            }
        }

        return SUCCESS;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

    public Integer getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(Integer invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }
}
