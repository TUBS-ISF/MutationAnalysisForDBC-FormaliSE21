package com.spaceprogram.accounting.model;

import com.spaceprogram.usersystem.UserManager;
import com.spaceprogram.usersystem.impls.hibernate.UserManagerHibernateImpl;

import javax.servlet.http.Cookie;

/**
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Aug 29, 2003
 * Time: 3:41:49 PM
 * @version 0.1
 */
public class Login extends Base {

    String username;
    String password;

    /**
     * the subclasses should implement this, not perform()
     */
    protected String perform() throws Exception {
        if (username == null || username.equals("")) {
            addError("username", "Invalid username.");
            //response.sendRedirect("index.jsp?erruser=2"); //this didn't write the cookies, bug in tomcat so used above to get around
            return "nouser";
        }
        if (password == null || password.equals("")) {
            addError("password", "Invalid password.");
            //response.sendRedirect("index.jsp?erruser=3"); //this didn't write the cookies, bug in tomcat so used above to get around

            return "nouser";
        }

        init();

        //Connection conn = pool.getConnection();
        //com.spaceprogram.users.User u = com.thinkvirtual.User.getUserByName(factory, conn, username, password);

          UserManager userManager = UserManagerHibernateImpl.getSingletonInstance();
    	com.spaceprogram.usersystem.User u = userManager.getUser(username);
       // System.out.println("USER: " + u + " - ");

        if (u == null || !u.getPassword().equals(password)) {
            //conn.close();
            addError("main", "User does not exist or password is incorrect.");

//response.sendRedirect("index.jsp?erruser=1"); //this didn't write the cookies, bug in tomcat so used above to get around
            return "nouser";
        }

        else {
            session.setMaxInactiveInterval(60 * 60 * 1); // change the last number to set the hours to stay logged in.

            //UserManager userManager = new UserManager();
            //userManager.setUser(u);
            // get company
            /*com.crossdb.sql.SelectQuery sq = factory.getSelectQuery();
            sq.addTable("Users");
            sq.addWhereCondition("user_id", com.crossdb.sql.WhereCondition.EQUAL_TO, u.getId());
            ResultSet rs = sq.execute(conn);
            int cid = 0;
            if(rs.next()){
                cid = rs.getInt("user_company_key");
            }
            rs.close();
            */
            //userManager.setCompanyKey(cid);
            //userManager.setConnectionPool(pool);
            //session.setAttribute("uid", new Integer(user_id));
            //out.print(u + " -- " + user_id); out.flush();
            //session.setAttribute("user", u);
            //if(company_key != 0){ // set it anyways
            //session.setAttribute("cid", new Integer(comp.getID()));
            //}
            //session.setAttribute("company", comp);
            session.setAttribute("user", u);

            Cookie userCookie2 = new Cookie("username", username);
            userCookie2.setMaxAge(2592000); // 30 days in seconds
            //userCookie2.setPath("/");
            response.addCookie(userCookie2);

            /*java.util.Date changed_pass = u.getChangedPassword();
            if(changed_pass == null){
                pool.free(conn);
                response.sendRedirect("secure2/changepass.jsp");
                return;
            }*/

           //conn.close();
            //response.sendRedirect("main.jsp");

        }
        return SUCCESS;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
