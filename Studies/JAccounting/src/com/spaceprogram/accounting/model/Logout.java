package com.spaceprogram.accounting.model;



/**
 *
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Aug 29, 2003
 * Time: 3:41:49 PM
 * @version 0.1
 */
public class Logout extends Base {

    String username;
    String password;

    /**
     * the subclasses should implement this, not perform()
     */
    protected String perform() throws Exception {
        init();
        session.removeAttribute("user");
        return SUCCESS;
    }

}
