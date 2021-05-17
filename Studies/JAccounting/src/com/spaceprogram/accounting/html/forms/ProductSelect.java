/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 16, 2002
 * Time: 4:58:59 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.io.PrintWriter;
import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import com.spaceprogram.accounting.basic.Product;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;

/*@nullable_by_default@*/
public class ProductSelect implements Select {




        String selectName = "productKey";
        List products;
        int selected = -1;

        public ProductSelect(Session sess, Integer cid) throws HibernateException {
            // fill in products list
            refresh(sess, cid);
        }

        public void refresh(Session sess, Integer cid) throws HibernateException{
            products = AccountingPersistenceManager.getProducts(sess, cid);
        }

        public void write(PrintWriter out){
            out.print("<select name=\"" + selectName + "\">\n<option value=\"0\">Choose Product...");
            for (int i = 0; i < products.size(); i++) {
               Product account = (Product) products.get(i);
                Integer aid = account.getId();
                out.print("<option value=\"" + aid + "\"");
                if(selected == aid.longValue()) out.print(" selected");
                out.print(">" + account.getName());
            }
            out.print("</select>");


        }

        public String getSelectName() {
            return selectName;
        }

        public void setSelectName(String selectName) {
            this.selectName = selectName;
        }


        public int getSelected() {
            return selected;
        }

        public void setSelected(int selected) {
            this.selected = selected;
        }


}
