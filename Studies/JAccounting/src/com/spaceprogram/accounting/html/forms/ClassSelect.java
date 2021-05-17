/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 4, 2002
 * Time: 2:43:11 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.crossdb.sql.SQLFactory;
import com.spaceprogram.accounting.basic.AClass;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;

/*@nullable_by_default@*/
public class ClassSelect {

	/*@spec_public@*/String selectName = "classKey";
	/*@spec_public@*/List classes;
	/*@spec_public@*/int selected = -1;

        public ClassSelect(SQLFactory factory, Connection conn, int cid) throws SQLException {
            // fill in products list
            refreshAccountList(factory, conn, cid);
        }

        public void refreshAccountList(SQLFactory factory, Connection conn, int cid) throws SQLException{
            classes = AccountingPersistenceManager.getClasses(factory, conn, cid);
        }

        //@ pre out != null;
        public void write(PrintWriter out){
            out.print("<select name=\"" + selectName + "\">\n<option value=\"0\">Choose Class...");
            for (int i = 0; i < classes.size(); i++) {
                AClass aclass = (AClass) classes.get(i);
                int aid = aclass.getId();
                out.print("<option value=\"" + aid + "\"");
                if(selected == aid) out.print(" selected");
                out.print(">" + aclass.getName());
            }
            out.print("</select>");


        }
        
        //@ pre true;
        //@ post \result.equals(this.selectName);
        public String getSelectName() {
            return selectName;
        }

        //@ pre !selectName.equals("");
        //@ pre selectName != null;
        //@ post true;
        public void setSelectName(String selectName) {
            this.selectName = selectName;
        }

        //@ pre true;
        //@ post \result == selected;
        public int getSelected() {
            return selected;
        }

        //@ pre selected >=0;
        //@ post true;
        public void setSelected(int selected) {
            this.selected = selected;
        }


}
