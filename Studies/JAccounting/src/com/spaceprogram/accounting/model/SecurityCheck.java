package com.spaceprogram.accounting.model;

import com.spaceprogram.usersystem.User;
import com.spaceprogram.usersystem.UserManager;

/**
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Aug 29, 2003
 * Time: 2:39:33 PM
 * @version 0.1
 */
/*@nullable_by_default@*/
public abstract class SecurityCheck extends Base{


    protected abstract String securePerform() throws Exception;


    protected final String perform() throws Exception {


        super.init();

        // check if user exists, if not boot
        Object o = session.getAttribute("user");
        //System.out.println(o.getClass());
        user = (User)(o);
        if(user == null || user.getId() == null){
            //response.sendRedirect("index.jsp?relogin=1");
            return "loginRequired";
        }
        request.setAttribute("user", user);

        // check for email access stuff
         Integer em = (Integer) session.getAttribute("em");
       if(em != null){
           // then invited by email ex: ?em=5&emc=2147483647
           String emc = (String) session.getAttribute("emc");


           UserManager um = getUserManager();
           //out.print("confirming..."); out.flush();
           try {
               if(um.confirmEmail(user, em, emc)){
                   // could either have a function here to apply the giveAccessByEmail stuff or do it in confirmEmail above?!?!
                   //out.print("confirmed"); out.flush();
               }
           }
           catch (Exception e) {
               e.printStackTrace();
           }
           session.removeAttribute("em");
           session.removeAttribute("emc");

       }

        return securePerform();
    }

}
