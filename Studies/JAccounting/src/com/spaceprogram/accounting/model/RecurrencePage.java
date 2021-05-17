package com.spaceprogram.accounting.model;

import com.spaceprogram.accounting.common.Recurrence;
import com.spaceprogram.accounting.datastore.AccountingPersistenceManager;
import com.spaceprogram.util.ServletUtils;
import com.spaceprogram.util.ServletUtils;

import java.text.SimpleDateFormat;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 8
 * @author 2003
 *         Time: 9:42:06 PM
 * @version 0.1
 */
public class RecurrencePage extends CompanyCheck {
    Long id;
    String realm;
    String itemKey;
    String formaction;

    protected String perform2() throws Exception {
        //int id = ServletUtils.getIntParameter(request, "id", 0);
        /*String realm = request.getParameter("realm");
        String itemKey = request.getParameter("itemKey");

        String formaction = request.getParameter("formaction");*/
        Recurrence recurrence;
        if (formaction != null) {
            if (formaction.equals("update")) {
                Session sess = getSessionFactory().openSession();
                Transaction tx = sess.beginTransaction();

                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

                if(id.longValue() == 0){
                recurrence = new Recurrence();
                }
                else{
                    recurrence = (Recurrence) sess.load(Recurrence.class, id);
                }
                recurrence.setRealm(realm);
                recurrence.setItemKey(itemKey);
                String typeStr = request.getParameter("type");
                char type = typeStr.charAt(0);
                recurrence.setType(type);
                //String tes = "" + type;
                //out.print(tes);out.flush();


                recurrence.setX(ServletUtils.getIntParameter(request, "x" + type, 0));
                recurrence.setY(ServletUtils.getIntParameter(request, "y" + type, 0));
                recurrence.setZ(ServletUtils.getIntParameter(request, "z" + type, 0));
                recurrence.setStartDate(sdf.parse(request.getParameter("startDate")));
                String range = request.getParameter("range");
                if (range.equals("0")) {// no end
                    recurrence.setEndDate(null);
                    recurrence.setCountdown(-1);
                } else if (range.equals("1")) {
                    recurrence.setEndDate(null);
                    // recurrence.setCountdown(Integer.parseInt(request.getParameter("countdown")));

                } else if (range.equals("2")) {
                    recurrence.setEndDate(sdf.parse(request.getParameter("endDate")));
                    recurrence.setCountdown(-1);
                }
                if (id.longValue() == 0){
                    sess.save(recurrence);
                }
                tx.commit();

                // AccountingPersistenceManager.saveRecurrence(factory, conn, cid, uid, recurrence);


                //response.sendRedirect("../redirect.jsp?app_key=" + app_id_charge + "&item_key=" + itemKey);
                getCtx().setViewParam("id", itemKey);
                return "submitted";


            }
        }
        return SUCCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getItemKey() {
        return itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getFormaction() {
        return formaction;
    }

    public void setFormaction(String formaction) {
        this.formaction = formaction;
    }
}
