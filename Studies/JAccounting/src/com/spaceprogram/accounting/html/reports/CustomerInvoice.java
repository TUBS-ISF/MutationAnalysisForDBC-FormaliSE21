/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Oct 8, 2002
 * Time: 4:46:50 PM
 * 
 */
package com.spaceprogram.accounting.html.reports;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.crossdb.sql.SQLFactory;
import com.crossdb.sql.SelectQuery;
import com.crossdb.sql.WhereCondition;
import com.spaceprogram.accounting.basic.Charge;
import com.spaceprogram.accounting.basic.ChargeLine;
import com.spaceprogram.accounting.basic.Invoice;
import com.spaceprogram.accounting.basic.Tax;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import com.spaceprogram.text.HTMLFormat;
import com.spaceprogram.util.WebappProperties;

// todo: this and other forms for sending should extend class that will do style and common things

/*@nullable_by_default@*/
public class CustomerInvoice {
    private boolean showPaymentOptions = true;


    public String createInvoice(SQLFactory factory, Session sess, Integer cid, Invoice invoice) throws SQLException, HibernateException {
        Connection conn = sess.connection();
        //Invoice invoice = AccountingPersistenceManager.getInvoice(factory, conn, cid, uid, invoice_id);

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        NumberFormat nf = NumberFormat.getCurrencyInstance();

        NumberFormat taxFormat = NumberFormat.getPercentInstance();


        StringWriter sw = new StringWriter();
        PrintWriter out = new PrintWriter(sw);
        out.write("<html>\r\n");
        out.write("<head>\r\n\t");
        out.write("<title>");
        out.write("Invoice");
        out.write("</title>\r\n\t");
        //out.write("<link rel=\"STYLESHEET\" type=\"text/css\" href=\"style1.css\">\r\n\t"); // cause can't attach to email, so putting inline
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



        String invoice_name = "";
        String logo = null;
        String compinfo = "";
        String toptext = "";
        String bottext = ""; // todo: bottext not used?  should add it somewhere
        SelectQuery sq = factory.getSelectQuery();
        sq.addTable(AccountingPersistenceManager.tablePrefix + "InvoiceTemplates");
        sq.addWhereCondition("companyKey", WhereCondition.EQUAL_TO, cid.intValue());
        sq.addWhereCondition("id", WhereCondition.EQUAL_TO, invoice.getTemplateKey());
        //System.out.println(sq);
        ResultSet rs = sq.execute(conn);
        if (rs.next()) {


            invoice_name = rs.getString("name");

            logo = rs.getString("logo");
            compinfo = rs.getString("companyInfo");
            toptext = rs.getString("topText");
            bottext = rs.getString("bottomText");


        }
        rs.close();

        BigDecimal sub_total = new BigDecimal("0.0");
        BigDecimal taxable_sub_total = new BigDecimal("0.0");

        String billTo = invoice.getBillTo();

        /* if(billTo == null){
             billTo = customer.getName();
             Address address = Address.getAddressByLabel(factory, conn, ThinkVirtual.APP_CONTACT_COMPANIES, customer.getID(), "Billing");
             if(address != null){
                 billTo += address.getStreet(1) + "\n"
                    + address.getStreet(2) + "\n"
                 + address.getStreet(3) + "\n"
                 + address.getCity() + ", "
                 + address.getProvince() + "\n"
                 + address.getPostCode() + "\n"
                 + address.getCountry();
             }

         }*/



        out.write("\r\n\r\n      ");
        out.write("<style>\r\n      TABLE.invoicetop {\r\n        border : 1px;\r\n      }\r\n      TD.invoicetop1 {\r\n        background-color: #234992;\r\n        color : white;\r\n      }\r\n      "
                + ".formtableheader {	background-color : #8F9A0E; color : White; text-align : center; } ");
        out.write("</style>\r\n\r\n   ");
        out.write("<table border=\"0\" width=\"100%\">\r\n\t");
        out.write("<tr>\r\n\t  ");
        out.write("<td>\r\n      ");

        if (logo != null) {
            out.write("<img src=\"");
            out.write(logo);
            out.write("\" align=\"left\">\r\n\t");

        }
        out.write("\r\n        ");
        out.write("</td>\r\n        ");
        out.write("<td>");
        out.write(compinfo);
        out.write("</td>\r\n        ");
        out.write("<td align=\"right\">\r\n        ");
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
        out.write("</td>\r\n\r\n       ");
        out.write("</tr>\r\n      ");
        out.write("</table>\r\n      ");
        out.write("<p>\r\n      ");
        out.write(toptext);
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
        out.write("<table class=\"invoicebox\">\r\n\r\n\t\t  ");
        out.write("<tr>\r\n\t\t\t");
        out.write("<td class=\"invoicetop1\">Invoice Date:");
        out.write("</td>");
        out.write("<td class=\"invoicetop1\">Due Date:");
        out.write("</td>");
        out.write("</tr>\r\n      ");
        out.write("<tr>\r\n            ");
        out.write("<td>");
        out.write(sdf.format(invoice.getInvoiceDate()));
        out.write("\r\n            ");
        out.write("</td>\r\n\t\t\t");
        out.write("<td>");
        out.write(sdf.format(invoice.getDueDate()));
        out.write("\r\n\t\t\t");
        out.write("</td>\r\n\t\t  ");
        out.write("</tr>\r\n\t\t");
        out.write("</table>\r\n\t\t");
        out.write("<p>&nbsp; ");
        out.write("</p>\r\n\t\t");
        out.write("</td>\r\n\t");
        out.write("</tr>\r\n  ");
        out.write("</table>\r\n\r\n");
        out.write("<b>Charges, Credits and Expenses");
        out.write("</b>");
        out.write("<br>\r\n\r\n");


        //List unbilledCharges = AccountingPersistenceManager.getChargesByCustomer(sess, cid, invoice.getCustomerKey());
        // now show charges that are billed

        Collection billedCharges = invoice.getCharges();


        int listsize = //unbilledCharges.size() +
                billedCharges.size();


        out.write("\r\n");
        out.write("<table width=\"100%\" class=\"formtable\">\r\n  ");
        out.write("<tr class=\"formtableheader\">\r\n\r\n    ");
        out.write("<td>Date");
        out.write("</td>\r\n\t");
        out.write("<td>Product/Service");
        out.write("</td>\r\n\t");
        out.write("<td>Description");
        out.write("</td>\r\n\t");
        out.write("<td>Qty");
        out.write("</td>\r\n\t");
        out.write("<td>Rate");
        out.write("</td>\r\n\t");
        out.write("<td>Amount");
        out.write("</td>\r\n\t");
        out.write("<td>Tax");
        out.write("</td>\r\n\r\n  ");
        out.write("</tr>\r\n  ");


        // TODO: Put previous charges here
        /*ex:
        ACCOUNT SUMMARY
          Balance Forward as of 07/01/2002  $642.00
          New charges (see details below)  $107.00
          Pay this amount  $749.0
        */
        String currency = "CAD";

        Iterator iter = billedCharges.iterator();
        for (int i = 0; i < listsize; i++) {
            out.flush();
            Charge charge;

            charge = (Charge) iter.next();// billedCharges.get(i-unbilledSize);

            Collection chargeLines = charge.getChargeLines();

            boolean billed = true;

/*       out.write("\r\n  ");
      out.write("<tr class=\"formtablerow0\">\r\n\t  ");
      out.write("<td>\r\n\t\t");
      out.write("<input type=\"checkbox\" name=\"charges\" value=\"");
      out.write(charge.getId());
      out.write("\" ");
if(charge.getInvoiceKey() > 0){ billed = true;       out.write(" checked");
}      out.write(">\r\n\t  ");
      out.write("</td>\r\n      ");
      out.write("<td>");
      out.write(charge.getChargeNumber());
      out.write("</td>\r\n\t");
      out.write("<td colspan=\"7\">\r\n\t ");
      out.write(sdf.format(charge.getChargeDate()));
      out.write("\r\n\t");
      out.write("</td>\r\n    ");
      out.write("</tr>\r\n    ");

              */
            // now go through chargelines and display
            Iterator iter2 = chargeLines.iterator();
            //for (int j = 0; j < chargeLines.size(); j++) {
            while (iter2.hasNext()) {
                ChargeLine chargeLine = (ChargeLine) iter2.next();
                BigDecimal quantity = chargeLine.getQuantity();
                BigDecimal rate = chargeLine.getRate();

                out.write("\r\n\r\n    ");
                out.write("<tr class=\"formtablerow1\">\r\n\t");
                out.write("<td>");
                out.write(sdf.format(charge.getChargeDate()));
                out.write("\r\n\t");
                out.write("</td>\r\n\r\n    ");
                out.write("<td>");
                out.write(chargeLine.getProduct().getName());
                out.write("</td>\r\n    ");
                out.write("<td>");
                out.write(htmlf.format(chargeLine.getDescription()));
                out.write("</td>\r\n\r\n\t");
                out.write("<td align=\"right\">\r\n\t  ");
                out.print(quantity);

                out.write("\r\n\t");
                out.write("</td>\r\n\t");
                out.write("<td align=\"right\">\r\n\t  ");
                out.print(rate);
                out.write("\r\n\t");
                out.write("</td>\r\n\r\n\t");
                out.write("<td align=\"right\">\r\n     ");

                BigDecimal amount = quantity.multiply(rate);
                if (billed) {
                    sub_total = sub_total.add(amount);
                    if (chargeLine.isTaxable()) {
                        taxable_sub_total = taxable_sub_total.add(amount);
                    }
                }
                out.write("\r\n\t  ");
                out.print(amount);
                out.write("\r\n\t");
                out.write("</td>\r\n\t");
                out.write("<td>\r\n\t  ");
                if (chargeLine.isTaxable()) {
                    out.write("Yes");
                }
                out.write("\r\n\t");
                out.write("</td>\r\n\r\n  ");
                out.write("</tr>\r\n\r\n  ");

                currency = chargeLine.getCurrency();

            }
        }


        out.write("\r\n\r\n");
        out.write("</table>\r\n\r\n\r\n  ");
        out.write("<table width=\"100%\" border=\"0\">\r\n\t");
        out.write("<tr>\r\n\t  ");
        out.write("<td>\r\n\t\t");
        out.write("<p>Customer Message:");
        out.write("<br>\r\n\t\t ");
        out.write(invoice.getCustomerMessage());
        out.write("\r\n\t\t");
        out.write("</p>\r\n\t\t");
        out.write("</td>\r\n\t  ");
        out.write("<td align=\"right\">\r\n      ");

        BigDecimal total = sub_total;
        out.write("\r\n\t\t");
        out.write("<table border=\"0\">\r\n\t\t  ");
        out.write("<tr>\r\n\t\t\t");
        out.write("<td align=\"right\" class=\"invoicetop1\">Subtotal: ");
        out.write("</td>\r\n\t\t\t");
        out.write("<td align=\"right\">\r\n\t\t\t  ");
        out.write(nf.format(sub_total));
        out.write("\r\n\t\t\t");
        out.write("</td>\r\n\t\t  ");
        out.write("</tr>\r\n\t\t  ");
        out.write("<tr>\r\n\t\t\t");
        out.write("<td align=\"right\" class=\"invoicetop1\">Taxes:");
        out.write("<br>\r\n\t\t\t");
        out.write("</td>\r\n\t\t\t");
        out.write("<td align=\"right\">&nbsp;");
        out.write("</td>\r\n\t\t  ");
        out.write("</tr>\r\n          ");

        List attachedTaxes = invoice.getTaxes();
        //List taxes = AccountingPersistenceManager.getTaxesForInvoices(factory, conn, cid, uid);
        for (int i = 0; i < attachedTaxes.size(); i++) {
            Tax tax = (Tax) attachedTaxes.get(i);
            BigDecimal taxamount = taxable_sub_total.multiply(tax.getPercent());
            out.write("\r\n\t\t  ");
            out.write("<tr>\r\n\t\t\t");
            out.write("<td align=\"right\">");
            out.write(tax.getName());
            out.write("\r\n            [");
            out.print(taxFormat.format(tax.getPercent()));
            out.write("%]\r\n\t\t\t");
            out.write("</td>\r\n\t\t\t");
            out.write("<td align=\"right\">\r\n\t\t\t  ");
            out.write(nf.format(taxamount));
            out.write("\r\n\t\t\t");
            out.write("</td>\r\n\t\t  ");
            out.write("</tr>\r\n          ");

            total = total.add(taxamount);

        }
        out.write("\r\n            ");
        out.write("<tr>\r\n\t\t\t");
        out.write("<td align=\"right\" class=\"invoicetop1\">");
        out.write("<b>Total New Charges:");
        out.write("</b>");
        out.write("</td>\r\n\t\t\t");
        out.write("<td align=\"right\">\r\n\t\t\t ");
        out.write("<b>");
        out.write(nf.format(total));
        out.write("</b>\r\n\t\t\t");
        out.write("</td>\r\n\t\t  ");
        out.write("</tr>\r\n\t\t  ");

        // TODO: should add up previous charges carried forward and new charge total above
        out.write("\r\n          ");
        out.write("<tr>\r\n\r\n\t\t\t");
        out.write("<td align=\"right\" class=\"invoicetop1\">");
        out.write("<b>Total Amount Due:");
        out.write("</b>");
        out.write("</td>\r\n\t\t\t");
        out.write("<td align=\"right\">\r\n\t\t\t ");
        out.write("<b>");
        out.write(nf.format(total));
        out.write("</b>\r\n\t\t\t");
        out.write("</td>\r\n\t\t  ");
        out.write("</tr>\r\n\t\t");
        out.write("</table>\r\n\t  ");
        out.write("</td>\r\n\t");
        out.write("</tr>\r\n  ");
        out.write("</table>\r\n\r\n");

        if (showPaymentOptions) {
            // show cheque or paypal or whatever based on company preferences
            String paypalEmail = null;
            try {
                paypalEmail = WebappProperties.getSingletonInstance().getProperty("systemEmail");

                // new a new formatter for sending total to paypal
                NumberFormat noDollarSignFormat = NumberFormat.getNumberInstance();
                noDollarSignFormat.setMaximumFractionDigits(2);
                noDollarSignFormat.setMinimumFractionDigits(2);

                out.write("<h4>Payment Options</h4>");
                out.write("<table border=\"0\" ><tr>");
                out.write("<th>Cheque or Money Order</th>");
                out.write("<th>Paypal</th>");
                out.write("</tr><tr>");
                out.write("<td>Send cheque or money order to the address at the top of the invoice.</td>");
                out.write("<td>");
                try {
                    out.write("<a href=\"https://www.paypal.com/xclick/business=" + URLEncoder.encode(paypalEmail, "UTF-8") +
                            "&item_number=" +
                            invoice.getInvoiceNumber() + "&amount=" +
                            noDollarSignFormat.format(total) + "&no_shipping=0&no_note=1&currency_code=" + currency +
                            "\"><img src=\"https://www.paypal.com/en_US/i/btn/x-click-but02.gif\" alt=\"Pay with Paypal\" border=\"0\"/></a>");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                out.write("</td>");
                out.write("</tr></table>");
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        out.write("\r\n\r\n\r\n\r\n");
        out.write("</body>\r\n");
        out.write("</html>\r\n");
        return sw.toString();
    }

}
