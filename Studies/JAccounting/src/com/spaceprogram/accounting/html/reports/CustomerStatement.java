package com.spaceprogram.accounting.html.reports;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sf.hibernate.Hibernate;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.spaceprogram.accounting.basic.Customer;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.InvoiceTemplate;
import com.spaceprogram.contacts.StreetAddress;
import com.spaceprogram.text.HTMLFormat;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Dec 3
 * @author 2003
 *         Time: 4:36:15 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public class CustomerStatement {
    public String createStatement(Session sess, Integer cid, Integer customerKey, Date statementDate) throws HibernateException {

        //Invoice invoice = AccountingPersistenceManager.getInvoice(factory, conn, cid, uid, invoice_id);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        NumberFormat taxFormat = NumberFormat.getPercentInstance();


        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        out.write("<html>\r\n");
        out.write("<head>\r\n\t");
        out.write("<title>");
        out.write("Statement");
        out.write("</title>\r\n\t");
        //out.write("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"style1.css\">\r\n\t");
        //out.write("<LINK REL=\"STYLESHEET\" TYPE=\"text/css\" HREF=\"../style1_ns.css\" DISABLED>\r\n\r\n\r\n");
        out.write("</head>\r\n\r\n");
        out.write("<body leftmargin=0 topmargin=0 rightmargin=\"0\" marginheight=\"0\" marginwidth=\"0\">\r\n\r\n");

        out.write("\r\n");
            out.write("<style>" +
                    "BODY {\n" +
                    "\tbackground-color : White;\n" +
                    "\tfont-family: Verdana, Geneva, Arial, Helvetica, sans-serif;\n" +
                    "\tfont-size : 12px;\n" +
                    "}" +
                    "TD {\n" +
                    "\tfont-family: Verdana, Geneva, Arial, Helvetica, sans-serif;\n" +
                    "\tfont-size : 12px;\n" +
                    "}" +
                    "TR.formtablerow0{\n" +
                    "\tbackground-color : #B8E0B6;\n" +
                    "}\n" +
                    "TR.formtablerow1{\n" +
                    "\tbackground-color: #CCCCFF;\n" +
                    "}" +
                    "</style>");


        // formaction = "invoice";
        //int invoice_id= ServletUtils.getIntParameter(request, "invoice_id", 0);



        /*String invoice_name = "";
        String logo = null;
        String compinfo = "";
        String toptext = "";
        String bottext = ""; // todo: bottext not used?  should add it somewhere
        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(AccountingPersistenceManager.tablePrefix + "InvoiceTemplates");
        sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid.intValue());
        sq.addWhereCondition("id", WhereCondition.EQUAL_TO, invoice.getTemplateKey());
        ResultSet rs = sq.execute(conn);
        if (rs.next()) {


            invoice_name = rs.getString("name");
            this.companyName = invoice_name;
            logo = rs.getString("logo");
            compinfo = rs.getString("companyInfo");
            toptext = rs.getString("topText");
            bottext = rs.getString("bottomText");


        }
        rs.close();*/
        List invoiceTemplates = sess.find("from " + InvoiceTemplate.class.getName() + " it where it.companyKey = ?", cid, Hibernate.INTEGER);
        // maybe have a way to choose
        if(invoiceTemplates.size() == 0){
            return "No templates.";
        }
        InvoiceTemplate invoiceTemplate = (InvoiceTemplate) invoiceTemplates.get(0);

        BigDecimal sub_total = new BigDecimal("0.0");
        BigDecimal taxable_sub_total = new BigDecimal("0.0");

        String billTo = null;
        Customer customer = (Customer) sess.get(Customer.class, customerKey);
             billTo = customer.getName();
        List addresses = customer.getStreetAddresses();
        for (int j = 0; j < addresses.size(); j++) {
            StreetAddress address = (StreetAddress) addresses.get(j);
            if(address.getLabel().equals("Billing")){
                billTo += address.getStreet1() + "\n"
                    + address.getStreet2() + "\n"
                 + address.getStreet3() + "\n"
                 + address.getCity() + ", "
                 + address.getProvince() + "\n"
                 + address.getPostcode() + "\n"
                 + address.getCountry();
            }
        }




        out.write("\r\n\r\n      ");
        out.write("<style>\r\n      TABLE.invoicetop {\r\n        border : 1px;\r\n      }\r\n      TD.invoicetop1 {\r\n        background-color: #234992;\r\n        color : white;\r\n      }\r\n      "
                + ".formtableheader {	background-color : #8F9A0E; color : White; text-align : center; } ");
        out.write("</style>\r\n\r\n   ");
        out.write("<table border=\"0\" width=\"100%\">\r\n\t");
        out.write("<tr>\r\n\t  ");
        out.write("<td>\r\n      ");


        if (invoiceTemplate.getLogo() != null) {
            out.write("<img src=\"");
            out.write(invoiceTemplate.getLogo());
            out.write("\" align=\"left\">\r\n\t");

        }
        out.write("\r\n        ");
        out.write("</td>\r\n        ");
        out.write("<td>");
        out.write(invoiceTemplate.getCompanyInfo());
        out.write("</td>\r\n        ");
        /*out.write("<td align=\"right\">\r\n        ");
        out.write("<table border=\"0\">\r\n        ");
        out.write("<tr>\r\n        ");
        out.write("<td class=\"invoicetop1\">Invoice #");
        out.write("</td>\r\n        ");
        out.write("</tr>\r\n        ");
        out.write("<tr>\r\n        ");
        out.write("<td>");
        out.print(invoice.getInvoiceNumber());
        out.write("</td>\r\n        ");
        out.write("</tr>\r\n        ");
        out.write("</table>\r\n\r\n        ");
        out.write("</td>\r\n\r\n       ");*/
        out.write("</tr>\r\n      ");
        out.write("</table>\r\n      ");
        out.write("<p>\r\n      ");
        out.write(invoiceTemplate.getTopText());
        out.write("\r\n      ");
        out.write("</p>\r\n\r\n  ");
        out.write("<table border=\"0\" width=\"100%\">\r\n\t");
        out.write("<tr>\r\n\t  ");
        out.write("<td>\r\n      ");
        out.write("<table class=\"invoicebox\">\r\n      ");
        out.write("<tr>");
        out.write("<td class=\"invoicetop1\">\r\n      Bill To:");
        out.write("</td>");
        out.write("</tr>\r\n      ");
        out.write("<tr>\r\n            ");
        out.write("<td>");
        HTMLFormat htmlf = new HTMLFormat();

        out.write(htmlf.format(billTo));
        out.write("\r\n\t  ");
        out.write("</td>\r\n      ");
        out.write("</tr>");
        out.write("</table>\r\n\r\n      ");
        out.write("</td>\r\n\t  ");
        out.write("<td align=\"right\">\r\n\t\t");
        out.write("<h3>STATEMENT</h3>");
        out.write("<table class=\"invoicebox\">\r\n\r\n\t\t  ");
        out.write("<tr>\r\n\t\t\t");
        out.write("<td class=\"invoicetop1\">Date:");
        out.write("</td>");
       /* out.write("<td class=\"invoicetop1\">Due Date:");
        out.write("</td>");*/
        out.write("</tr>\r\n      ");
        out.write("<tr>\r\n            ");
        out.write("<td>");
        out.write(sdf.format(statementDate));
        out.write("\r\n            ");
        out.write("</td>\r\n\t\t\t");
        /*out.write("<td>");
        out.write(sdf.format(invoice.getDueDate()));
        out.write("\r\n\t\t\t");
        out.write("</td>\r\n\t\t  ");*/
        out.write("</tr>\r\n\t\t");
        out.write("</table>\r\n\t\t");
        out.write("<p>&nbsp; ");
        out.write("</p>\r\n\t\t");
        out.write("</td>\r\n\t");
        out.write("</tr>\r\n  ");
        out.write("</table>\r\n\r\n");
        /*out.write("<b>Charges, Credits and Expenses");
        out.write("</b>");*/
        out.write("<br>\r\n\r\n");



        out.write("\r\n");
        out.write("<table width=\"100%\" class=\"formtable\">\r\n  ");
        out.write("<tr class=\"formtableheader\">\r\n\r\n    ");
        out.write("<td>Date");
        out.write("</td>\r\n\t");
        out.write("<td>Activity");
        out.write("</td>\r\n\t");
        out.write("<td>Amount");
        out.write("</td>\r\n\t");
        out.write("<td>OPEN AMOUNT");
        out.write("</td>\r\n\t");

        out.write("</tr>\r\n  ");


        List invoices = sess.find("from com.spaceprogram.accounting.basic.Invoice invoice " +
               "where invoice.customerKey = " + customerKey +
               " and invoice.companyKey = " + cid  +
               " order by invoiceDate ");
       BigDecimal totalAR = new BigDecimal("0.0");
        BigDecimal totalDue = new BigDecimal("0.0");
        BigDecimal dueCurrent = new BigDecimal("0.0");
        BigDecimal due1to30 = new BigDecimal("0.0");
        BigDecimal due31to60 = new BigDecimal("0.0");
        BigDecimal due61to90 = new BigDecimal("0.0");
        BigDecimal due90plus = new BigDecimal("0.0");
        Calendar cal30 = Calendar.getInstance();
        cal30.add(Calendar.DAY_OF_YEAR, -30);
        Calendar cal60 = Calendar.getInstance();
        cal30.add(Calendar.DAY_OF_YEAR, -60);
        Calendar cal90 = Calendar.getInstance();
        cal30.add(Calendar.DAY_OF_YEAR, -90);


      for (int i = 0; i < invoices.size(); i++) {
          Invoice invoice = (Invoice) invoices.get(i);
          if(invoice.isPaid()){
              continue;
          }


           BigDecimal amount = invoice.getTotal();
           totalAR = totalAR.add(amount);
          BigDecimal amountPaid = invoice.getAmountPaid();
          BigDecimal amountOpen = amount.subtract(amountPaid);
          totalDue = totalDue.add(amountOpen);


                out.write("<tr class=\"formtablerow1\">\r\n\t");
                out.write("<td>");
                out.write(sdf.format(invoice.getInvoiceDate()));
                out.write("\r\n\t");
                out.write("</td>\r\n\r\n    ");
                out.write("<td>");
                out.write("Invoice #" + invoice.getInvoiceNumber() + " due " + sdf.format(invoice.getDueDate()) );
                out.write("</td>\r\n    ");

                out.write("<td align=\"right\">\r\n\t  ");
                out.print(nf.format(amount));
                out.write("\r\n\t");
                out.write("</td>\r\n\r\n\t");
                out.write("<td align=\"right\">\r\n     ");
                        out.print(nf.format(amountOpen));
                out.write("</td>\r\n\t");

                out.write("</tr>\r\n\r\n  ");

          // which balance bucket
          if(invoice.getDueDate().after(statementDate)){ // then current
              dueCurrent = dueCurrent.add(amountOpen);
          }
          else if(invoice.getDueDate().after(cal30.getTime())){
              due1to30 = due1to30.add(amountOpen);
          }
          else if(invoice.getDueDate().after(cal60.getTime())){
              due31to60 = due31to60.add(amountOpen);
          }
          else if(invoice.getDueDate().after(cal90.getTime())){
              due61to90 = due61to90.add(amountOpen);
          }
          else{
              due90plus = due90plus.add(amountOpen);
          }


      }



        out.write("\r\n\r\n");
        out.write("</table>\r\n\r\n\r\n  ");

        // balances
        out.write("<table width=\"100%\" border=\"0\">\r\n\t");
        out.write("<tr class=\"formtableheader\"> ");
        out.write("<td>");
        out.write("Current Due");
        out.write("</td>\r\n\t  ");
         out.write("<td>");
        out.write("1-30 Days Past Due");
        out.write("</td>\r\n\t  ");
         out.write("<td>");
        out.write("31-60 Days Past Due");
        out.write("</td>\r\n\t  ");
         out.write("<td>");
        out.write("61-90 Days Past Due");
        out.write("</td>\r\n\t  ");
         out.write("<td>");
        out.write("90+ Days Past Due");
        out.write("</td>\r\n\t  ");
          out.write("<td>");
        out.write("Amount Due");
        out.write("</td>\r\n\t  ");
        out.write("</tr>\r\n          ");
         out.write("<tr>");
        out.write("<td align=\"middle\">");
        out.print(nf.format(dueCurrent));
        out.write("</td>\r\n\t  ");
         out.write("<td align=\"middle\">");
        out.print(nf.format(due1to30));
        out.write("</td>\r\n\t  ");
         out.write("<td align=\"middle\">");
        out.print(nf.format(due31to60));
        out.write("</td>\r\n\t  ");
         out.write("<td align=\"middle\">");
        out.print(nf.format(due61to90));
        out.write("</td>\r\n\t  ");
       out.write("<td align=\"middle\">");
       out.print(nf.format(due90plus));
        out.write("</td>\r\n\t  ");
        out.write("<td align=\"middle\">");
       out.print("<b>" + nf.format(totalDue) + "</b>");
        out.write("</td>\r\n\t  ");
        out.write("</tr>\r\n          ");
        out.write("</table>");

      
        out.write("\r\n\r\n\r\n\r\n");
        out.write("</body>\r\n");
        out.write("</html>\r\n");
        return sw.toString();
    }
}
