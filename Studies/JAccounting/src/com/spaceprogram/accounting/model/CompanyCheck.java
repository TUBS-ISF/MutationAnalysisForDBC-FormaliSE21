package com.spaceprogram.accounting.model;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 8
 * @author 2003
 *         Time: 7:24:35 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public abstract class CompanyCheck extends SecurityCheck{
    int accessLevel = -1;
    Integer cid = new Integer(0);


    protected abstract String perform2() throws Exception;

    protected final String securePerform() throws Exception {

        cid = (Integer) session.getAttribute("cid");


        accessLevel = userManager.getAccessLevel(user, "tvcompany", cid + "");
        // lets just boot out here if returns -1
        if (accessLevel < 0) { // && projectId.longValue() > 0){
            //pool.free(conn);
            //response.sendRedirect("index.jsp");
            return "noaccess";
        }


        return perform2();
    }


    public int getAccessLevel() {
        return accessLevel;
    }

    public Integer getCid() {
        return cid;
    }


}
