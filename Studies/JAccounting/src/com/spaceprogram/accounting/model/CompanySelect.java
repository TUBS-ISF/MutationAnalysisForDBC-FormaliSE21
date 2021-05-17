package com.spaceprogram.accounting.model;


import com.spaceprogram.usersystem.UserManager;
import com.spaceprogram.usersystem.impls.hibernate.UserManagerHibernateImpl;
import com.spaceprogram.contacts.impls.CompanyImpl;
import com.spaceprogram.contacts.Company;
import net.sf.hibernate.Session;

/**
 * @author Travis Reeder - travis@spaceprogram.com
 *         Date: Nov 7
 * @author 2003
 *         Time: 5:03:09 PM
 * @version 0.1
 */
public class CompanySelect extends SecurityCheck {
    protected String securePerform() throws Exception {

        String formaction = request.getParameter("formaction");
        if (formaction != null) {
            if (formaction.equals("newcomp")) {

                // todo: surround with hibernate try/catch/rollback
                UserManager um = UserManagerHibernateImpl.getSingletonInstance();

                Session sess = getSessionFactory().openSession();
                Company c = new CompanyImpl();
                c.setName(request.getParameter("compname"));
                sess.save(c);

                Integer compid = c.getId();
                 //Company.insertCompany(factory, conn, request.getParameter("compname"), true, intModules);

                /////OLD system
                // add user to company as admin
                //Company.addUser(factory, conn, compid, uid, 1000);

                // now add to admin and everyone group
                /* int adminGroupId = Company.getAdminGroupId(factory, conn, cid);
                 int everyoneGroupId = Company.getEveryoneGroupId(factory, conn, cid);
                 String groups[] = new String[2];
                 groups[0] = ""+adminGroupId;
                 groups[1] = ""+everyoneGroupId;
                 com.thinkvirtual.User.createUserGroups(factory, conn, uid, groups);*/
                //// NEW system
                // give admin rights
                um.giveAccess(user, "tvcompany", "" + compid, UserManager.ADMINISTRATOR);


                sess.close();
                session.setAttribute("cid", compid);

                //response.sendRedirect("newcompany.jsp?notlogin=1");
                return "selected";


            }
        }
        String cidStr = request.getParameter("cid");
        if (cidStr != null) {

            //System.out.println("yo");

            //userManager.setCompanyKey(Integer.parseInt(cidStr));

            //Company company = Company.getCompany(factory, conn, uid, userManager.getCompanyKey());
            //out.print("COMP!!" + company); out.flush();
            //userManager.setCompany(company);


            session.setAttribute("cid", new Integer(cidStr));
            /* response.sendRedirect(//"contacts/");
             "accounting/");*/
            return "selected";
        }
        return SUCCESS;
    }
}
