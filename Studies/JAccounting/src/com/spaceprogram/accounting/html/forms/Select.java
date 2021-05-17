/**
 * @author Travis Reeder - travis@spaceprogram.com
 * @version 0.1
 * Date: Sep 16, 2002
 * Time: 5:00:11 PM
 * 
 */
package com.spaceprogram.accounting.html.forms;

import java.io.PrintWriter;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

/*@nullable_by_default@*/
public interface Select {
    void refresh(Session sess, Integer cid) throws HibernateException;

    void write(PrintWriter out);

    String getSelectName();

    //@ pre selectName != null;
    void setSelectName(String selectName);

    int getSelected();

    //@ pre selected >= 0;
    void setSelected(int selected);
}
