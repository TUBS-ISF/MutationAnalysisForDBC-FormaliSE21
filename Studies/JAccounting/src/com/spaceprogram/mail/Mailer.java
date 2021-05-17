package com.spaceprogram.mail;

/**
 * @deprecated use the .util one, has a login feature
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class Mailer {

    public static String CTYPE_PLAIN_TEXT = "text/plain";
    public static String CTYPE_HTML = "text/html";

    String content_type = Mailer.CTYPE_PLAIN_TEXT; // default

    String host;
    boolean debug = false;

    InternetAddress[] to;
    InternetAddress from;
    InternetAddress[] cc;
    InternetAddress[] bcc;
    String subject;
    String body;

    List attachments = new ArrayList();

    public Mailer(String host) {
        this.host = host;
    }

    public Mailer(String host, String to, String from, String cc, String bcc, String subject) throws AddressException {
        this.host = host;
        this.to = InternetAddress.parse(to);
        this.from = new InternetAddress(from);
        this.cc = InternetAddress.parse(cc);
        this.bcc = InternetAddress.parse(bcc);
        this.subject = subject;
    }

    public Mailer(String host, String to, String from, String cc, String bcc, String subject, String body) throws AddressException {
        this.host = host;
        this.to = InternetAddress.parse(to);
        this.from = new InternetAddress(from);
        this.cc = InternetAddress.parse(cc);
        this.bcc = InternetAddress.parse(bcc);
        this.subject = subject;
        this.body = body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setContentType(String type) {
        content_type = type;
    }

    /**
     * makes an attachment from a string
     * @param contents
     */
    public void addAttachmentFromString(String contents, String content_type, String name) throws IOException {
        ByteArrayDataSource bad = new ByteArrayDataSource(contents, content_type);
        //bad.setName(name);
        attachments.add(bad);
    }

    public void addAttachment(String filename) {
        FileDataSource fds = new FileDataSource(filename);
        attachments.add(fds);
    }

    public void send() throws MessagingException {// convenience method for sendMail
        sendMail();
    }

    public void sendMail() throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.host", host);

        Session session = Session.getInstance(props, null); // changed this from getDefaultInstance because http://www.jguru.com/forums/view.jsp?EID=434477
        session.setDebug(debug);

        // create a message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(from);

        msg.setRecipients(Message.RecipientType.TO, to);
        msg.setRecipients(Message.RecipientType.CC, cc);
        msg.setRecipients(Message.RecipientType.BCC, bcc);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        // now for the content
        int attach_len = attachments.size();
        if (attach_len > 0) {
            MimeBodyPart mbp1 = new MimeBodyPart();
            mbp1.setContent(body, content_type);

            // create the Multipart and its parts to it
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbp1);

            // create the rest of the contents if needed

            for (int i = 0; i < attach_len; i++) {
                MimeBodyPart mbp2 = new MimeBodyPart();

                // attach the file to the message

                DataSource fds = (DataSource) attachments.get(i);
                mbp2.setDataHandler(new DataHandler(fds));
                mbp2.setFileName(fds.getName());
                mp.addBodyPart(mbp2);
            }
            msg.setContent(mp);
        }
        else { // just text part, so no multipart
            msg.setContent(body, content_type);
        }

        // send the message

        msg.saveChanges();

        Transport transport = session.getTransport("smtp");
        String user = "";
        String pass = "";
        transport.connect(host, user, pass);
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
        //Transport.send(msg);


    }

    public void sendMail(String to, String from, String cc, String bcc, String subject, String body) throws MessagingException {

        // create some properties and get the default Session
        Properties props = new Properties();
        props.put("mail.smtp.host", host);

        Session session = Session.getInstance(props, null); // changed this from getDefaultInstance because http://www.jguru.com/forums/view.jsp?EID=434477
        session.setDebug(debug);

        // create a message
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        InternetAddress[] address = InternetAddress.parse(to);
        msg.setRecipients(Message.RecipientType.TO, address);
        msg.setSubject(subject);
        msg.setSentDate(new Date());

        /*MimeBodyPart mbp2 = new MimeBodyPart();

// attach the file to the message
           FileDataSource fds = new FileDataSource(filename);
        mbp2.setDataHandler(new DataHandler(fds));
        mbp2.setFileName(fds.getName());

        // create the Multipart and its parts to it
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);

        // add the Multipart to the message
        msg.setContent(mp);

        // set the Date: header
        msg.setSentDate(new Date());

        // send the message
        Transport.send(msg);

        // create and fill the first message part
        MimeBodyPart mbp1 = new MimeBodyPart();
        mbp1.setText("text 1");

        // create and fill the second message part
        MimeBodyPart mbp2 = new MimeBodyPart();
        // Use setText(text, charset), to show it off !
        mbp2.setText("text 2", "us-ascii");

        // create the Multipart and its parts to it
        Multipart mp = new MimeMultipart();
        mp.addBodyPart(mbp1);
        mp.addBodyPart(mbp2);

        // add the Multipart to the message
        msg.setContent(mp);
        */
        msg.setText(body);
        // send the message

        // SHOULD MAYBE CHANGE TO WHAT WE HAVE IN OTHER SENDMAIL TO FIX
        // THE mail.smtp.host problem
        Transport.send(msg);

    }
   

    /**
     * 
     * @param host
     * @param to
     * @param from
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     * @throws MessagingException
     */
    //@ pre !host.equals("");
    //@ pre !to.equals("");
    //@ post true;
    public static void sendMail(String host, String to, String from, String cc, String bcc, String subject, String body) throws MessagingException {
        Mailer m = new Mailer(host, to, from, cc, bcc, subject, body);
        m.sendMail();
    }
}
