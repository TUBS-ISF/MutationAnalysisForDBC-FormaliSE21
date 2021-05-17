package com.spaceprogram.accounting.model;

import com.spaceprogram.usersystem.User;
import com.spaceprogram.util.WebappProperties;

/**
 * 
 * @author Travis Reeder - travis@spaceprogram.com
 * Date: Sep 29, 2003
 * Time: 8:32:35 PM
 * @version 0.1
 */
public class Feedback extends SecurityCheck{
    private boolean submitted = false;
    int companyKey;

    protected String securePerform() throws Exception {
        if(request.getParameter("formaction") != null){
            submitted = true;

             String s = (String) session.getAttribute("cid");
            if (s != null)
                companyKey = Integer.parseInt(s);
            sendFeedback(request.getParameter("feedback"), user, companyKey);


        }
        return SUCCESS;
    }

    void sendFeedback(String feedback, User user, int site_key) throws Exception {
		String to = WebappProperties.getSingletonInstance().getProperty("feedbackemail");
		String from = user.getEmail();
		String subject = "ACCOUNTING FEEDBACK";
		String body = "SITE: " + site_key ;

		if(user != null){
			body += "\nUSER ID: " + user.getId() + "\n" + user.getName() + " - " + user.getEmail() + "\n\n";
		}
		else
			body += "\nUSER IS NULL\n\n";
		body += feedback;

        // todo: get the mailer in here
		/*com.spaceprogram.mail.Mailer mailer = new com.spaceprogram.mail.Mailer("localhost", to, from, "", "", subject);
		//mailer.setContentType(com.spaceprogram.mail.Mailer.CTYPE_HTML);
		mailer.setBody(body);
		mailer.send();*/
	}

    public boolean isSubmitted() {
        return submitted;
    }

    public void setSubmitted(boolean submitted) {
        this.submitted = submitted;
    }
}
