package com.spaceprogram.accounting.model;

import com.crossdb.sql.SelectQuery;
import com.crossdb.sql.WhereCondition;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import com.spaceprogram.accounting.html.reports.CustomerStatement;
import com.spaceprogram.util.Mailer;
import com.spaceprogram.util.WebappProperties;
import net.sf.hibernate.Session;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Dec 3
 * @author 2003
 *         Time: 4:27:28 PM
 * @version 0.1
 */
public class Statement extends CompanyCheck {
    String formaction;
    Integer customerKey;

    protected String perform2() throws Exception {


        
        if (formaction != null) {
            if (formaction.equals("preview")) {
                return "preview";
            } else if (formaction.equals("send")) {
                String sendToEmailStr = request.getParameter("to");

                Session sess = getSessionFactory().openSession();

                CustomerStatement statement = new CustomerStatement();
                String customerInvoiceStr = statement.createStatement(sess, getCid(), getCustomerKey(), new Date());

                String fromEmail = AccountingPersistenceManager.getPreferences(factory, sess.connection(), cid).getFromEmail();

                // get company name
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
                Mailer mailer = new Mailer(props.getProperty("smtpServer"), sendToEmailStr, fromEmail, "", bcc, "Statement from " + compname);
                //mailer.setContentType(Mailer.CTYPE_HTML);

                mailer.setLogin(props.getProperty("smtpUsername"), props.getProperty("smtpPassword"));

                mailer.setBody("Statement for Account Attached.");
                mailer.addAttachmentFromString(customerInvoiceStr, "text/html", "statement.html");
                mailer.send();
                sess.close();

            }
        }


        return SUCCESS;
    }


    public Integer getCustomerKey() {
        return customerKey;
    }

    public void setCustomerKey(Integer customerKey) {
        this.customerKey = customerKey;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }


}
