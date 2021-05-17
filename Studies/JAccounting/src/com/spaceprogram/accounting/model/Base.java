package com.spaceprogram.accounting.model;

import com.spaceprogram.accounting.basic.AClass;
import com.spaceprogram.accounting.basic.Account;
import com.spaceprogram.accounting.basic.Charge;
import com.spaceprogram.accounting.basic.ChargeLine;
import com.spaceprogram.accounting.basic.Customer;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.InvoiceTemplate;
import com.spaceprogram.accounting.basic.JournalEntry;
import com.spaceprogram.accounting.basic.Payment;
import com.spaceprogram.accounting.basic.PaymentDistribution;
import com.spaceprogram.accounting.basic.Product;
import com.spaceprogram.accounting.basic.Tax;
import com.spaceprogram.accounting.basic.Transaction;
import com.spaceprogram.accounting.common.Recurrence;
import com.spaceprogram.usersystem.User;
import com.spaceprogram.usersystem.UserManager;
import com.spaceprogram.usersystem.impls.hibernate.UserManagerHibernateImpl;
import com.spaceprogram.util.WebappProperties;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.cfg.Configuration;
import org.infohazard.maverick.ctl.ThrowawayBean2;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.util.*;


/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Aug 29
 * @author 2003
 *         Time: 1:42:22 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public abstract class Base extends ThrowawayBean2 {

    String realm = "accounting";

    User user = null;
    String sitename = null;
    HttpSession session;
    HttpServletRequest request;

    com.crossdb.sql.SQLFactory factory;
    protected DataSource pool;
    HttpServletResponse response;

    PageElements pageElements = new PageElements();

    /**
     */
    protected Map errors;
    protected static UserManager userManager;

    static SessionFactory sessionFactory;
    private static boolean initializing;
    private List messages;

    /**
     */
    public boolean hasErrors() {
        return (this.errors != null);
    }

    /**
     * @return a map of String field name to String message, which
     *         will be empty if no errors have been reported.
     */
    public Map getErrors() {
        if (this.errors == null)
            return Collections.EMPTY_MAP;
        else
            return this.errors;
    }

    /**
     */
    //@ pre !field.equals("");
    //@ pre !message.equals("");
    public void addError(String field, String message) {
        if (this.errors == null)
            this.errors = new HashMap();

        this.errors.put(field, message);
    }
    
    //@ pre !msg.equals("");
    protected void addMessage(String msg){
        if(messages == null){
            messages = new ArrayList();
        }
        messages.add(msg);
    }


    protected void init() throws Exception {

        request = super.getCtx().getRequest();
        session = super.getCtx().getRequest().getSession();
        response = super.getCtx().getResponse();

        ServletContext context = super.getCtx().getServletContext();

        viewSetup();

        // should probably fix these initializing loops if they cause an issue
        while (initializing) {

        }
        if (userManager == null) {
            initializing = true;
            try {
                userManager = UserManagerHibernateImpl.getSingletonInstance();
            } catch (HibernateException e) {
                e.printStackTrace();
            } finally {
                initializing = false;
            }

        }
        while (initializing) {

        }
        if (sessionFactory == null) {
            initializing = true;
            try {
                UserManagerHibernateImpl umh = (UserManagerHibernateImpl) userManager;
                Configuration ds = umh.getHibernateConfiguration()//new Configuration()
                        .addClass(Payment.class)
                        .addClass(PaymentDistribution.class)
                        .addClass(Transaction.class)
                        .addClass(JournalEntry.class)
                        /*.addClass(EmailAddress.class)*/
                        .addClass(Customer.class)
                        //.addClass(AccountingCustomer.class)
                        //.addClass(PhoneNumber.class)
                        /*.addClass(Address.class)*/
                        .addClass(Invoice.class)
                        .addClass(Charge.class)
                        .addClass(ChargeLine.class)
                        .addClass(Recurrence.class)
                        .addClass(Tax.class)
                        .addClass(Product.class)
                        .addClass(Account.class)
                        .addClass(InvoiceTemplate.class)
                        .addClass(AClass.class)
                        ;

                sessionFactory = ds.buildSessionFactory();
                umh.setHibernateSessionFactory(sessionFactory);
            } catch (HibernateException e) {
                e.printStackTrace();
            } finally {
                initializing = false;
            }

        }

/*

        pool = (DataSource) (context.getAttribute("conn-pool"));
        if(pool == null){
            System.out.println("Creating New Connection Pool");
            Class.forName("com.mysql.jdbc.Driver");
            PoolConfig pc = new PoolConfig();
            pc.setIdleConnectionTestPeriod(60 * 60);
            DataSource unpooled = DataSources.unpooledDataSource("jdbc:mysql://127.0.0.1:3306/crmshark",
                                                 "root",
                                                 "k00lmoed33");
                    pool = DataSources.pooledDataSource( unpooled, pc);

            /*pool = new com.spaceprogram.jdbc.ConnectionPool("org.gjt.mm.mysql.Driver",  "jdbc:mysql://127.0.0.1:3306/webstats",
									"root", "k00lmoed33",
                                    //"root", "spank3rama",
									  1,
									  20, true);*


			context.setAttribute("conn-pool", pool);




		}
        */
        WebappProperties props = WebappProperties.getSingletonInstance();
        Class factory_class = Class.forName(props.getProperty("crossdbDriver"));

        factory = (com.crossdb.sql.SQLFactory) (factory_class.newInstance());



        //return perform2();
    }

    protected void viewSetup() {
        pageElements.setSectionTitle("Accounting");
        pageElements.setSectionColor("green");
        pageElements.setMainPage("overview.m");
        pageElements.addNavLink(new Link("Accounts", "accounts.m"));
        // pageElements.addNavLink(new Link("Taxes", "taxes.jsp"));
        // pageElements.addNavLink(new Link("Classes", "classes.jsp"));
        // TODO: Should use submenu structure
        //pageElements.addNavLink(new Link("Client Overview", "client_overview.jsp"));
        pageElements.addNavLink(new Link("Customers", "customers.m"));
        // pageElements.addNavLink(new Link("Enter Charges", "charges.jsp"));
        //pageElements.addNavLink(new Link("Charge List", "charge_list.jsp"));
        // pageElements.addNavLink(new Link("Create Invoice", "invoice.jsp"));
        // pageElements.addNavLink(new Link("Invoice List", "invoice_list.jsp"));
        // pageElements.addNavLink(new Link("Invoice Templates", "invoice_templates.jsp"));
        //pageElements.addNavLink(new Link("Receive Payments", "payments.jsp"));
        //pageElements.addNavLink(new Link("Products/Services List", "products.jsp"));


        pageElements.addNavLink(new Link("Vendors", "vendors.m"));
        //pageElements.addNavLink(new Link("- Enter Bills", "charges.jsp"));
        //pageElements.addNavLink(new Link("- Pay Bills", "invoice.jsp"));
        //pageElements.addNavLink(new Link("- Vendor List", "invoice.jsp"));

        //pageElements.addNavLink(new Link("Banking Overview", "banking_overview.jsp"));
        //   pageElements.addNavLink(new Link("- Write Cheques", "cheques.jsp"));
        //   pageElements.addNavLink(new Link("- Spend Cash", "invoice.jsp"));
        //   pageElements.addNavLink(new Link("- Make Deposits", "invoice.jsp"));
        //pageElements.addNavLink(new Link("- Transfer Funds", "invoice.jsp"));
        pageElements.addNavLink(new Link("Journal Entry", "journalEntry.m"));
// SAME AS ACCOUNTS NOW I BELIEVE pageElements.addNavLink(new Link("Registers", "registers.jsp"));
        pageElements.addNavLink(new Link("Reports", "reports.m"));

        // pageElements.addNavLink(new Link("Reports", "banking_overview.jsp"));
        pageElements.addNavLink(new Link("Admin", "admin.m"));
    }



    public User getUser() {
        return user;
    }

    public String getSitename() {
        return sitename;
    }

    /* public java.sql.Connection getConnection() throws HibernateException {
         return sessionFactory.openSession().connection();
     }*/

    public com.crossdb.sql.SQLFactory getFactory() {
        return factory;
    }

    public String getRealm() {
        return realm;
    }

    public PageElements getPageElements() {
        return pageElements;
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static UserManager getUserManager() {
        return userManager;
    }

    protected abstract String perform() throws Exception;


}
