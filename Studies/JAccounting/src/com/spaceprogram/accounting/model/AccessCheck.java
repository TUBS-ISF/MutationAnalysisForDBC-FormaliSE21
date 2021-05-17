package com.spaceprogram.accounting.model;


import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import com.spaceprogram.accounting.basic.Company;

/**
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Aug 29, 2003
 * Time: 2:16:45 PM
 * @version 0.1
 */

/*@nullable_by_default@*/
public abstract class AccessCheck extends SecurityCheck{


     int accessLevel = -1;
    Long companyId = new Long(0);
    private Company company;


    protected abstract String perform2() throws Exception;

    protected final String securePerform() throws Exception {


// globals
      /*  projectId = new Long(0);
        String pid_string = request.getParameter("projectId");
                             //boolean showReturnToProjectLink = true;
        if(pid_string != null){
            projectId = new Long(pid_string);
        }
*/
                             accessLevel = userManager.getAccessLevel(user, realm, companyId.toString());
                             // lets just boot out here if returns -1
                             if(accessLevel < 0){ // && projectId.longValue() > 0){
                                 //pool.free(conn);
                                 //response.sendRedirect("index.jsp");
                                 return "noaccess";
                             }



        return perform2();
    }




    public int getAccessLevel() {
        return accessLevel;
    }
    public Company getCompany() throws HibernateException {
        if(company == null){
            Session sess = sessionFactory.openSession();
            company = (Company) sess.load(Company.class, (companyId));
            sess.close();
        }
        return company;
    }

    public Long getCompanyId() {
        return companyId;
    }

    //@ pre companyId != null;
    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }


}
