/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

/**
 *
 * @author varghees
 */
public class TextMailWithAttachment {

    private MailProperties props = null;

    public TextMailWithAttachment(MailProperties props) {
        this.props = props;
    }

    public static void main(String argv[]) {
        MailProperties mailProps = new MailProperties();
        TextMailWithAttachment sender = new TextMailWithAttachment(mailProps);
        String[] attachments = {"/tmp/downloadedFile.pdf"};
        sender.sendMail("sam@digitalanalystteam.com", "Test", "Test MEssage", Arrays.asList(attachments));
    }

    public String sendMail(String to, String subject, String message, List<String> attachments) {

        List<MailAttachment> mailAttachments = new ArrayList<>();
        for (Iterator<String> iterator = attachments.iterator(); iterator.hasNext();) {
            String attachment = iterator.next();
            MailAttachment mailAttachment = new MailAttachment();
            mailAttachment.setAttachDescription("");
            mailAttachment.setAttachmentPath(attachment);
            mailAttachments.add(mailAttachment);
        }
        this.props.setHostName("gator3272.hostgator.com");
        this.props.setPort(587);
        this.props.setAuthUser("jp@digitalanalystteam.com");
        this.props.setAuthPasswd("d@tjp527");
        this.props.setFrom("jp@digitalanalystteam.com");
        this.props.setHtmlMessage(message);
        this.props.setTxtMessage(message);
        this.props.setSubject(subject);
        this.props.setTo(to);
        this.props.setAttachment(mailAttachments);
        return sendMail();
    }

    public String sendMail() {
        try {
            // Create the email message
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(props.getHostName());
            email.setSmtpPort(props.getPort());
            email.setAuthentication(props.getAuthUser(), props.getAuthPasswd());
            //email.setSSLOnConnect(props.isSetSSLOnConnect());
            email.setFrom(props.getFrom());
            email.setSubject(props.getSubject());
            email.setMsg(props.getTxtMessage());
            email.addTo(props.getTo());

            // add attachments
            List<MailAttachment> attachFiles = props.getAttachment();
            for (int i = 0; i < attachFiles.size(); i++) {
                MailAttachment attachFile = attachFiles.get(i);
                // Create the attachment
                EmailAttachment attachment = new EmailAttachment();
                //attachment.setURL(props.getAttachmentURL());
                attachment.setPath(attachFile.getAttachmentPath());
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription(attachFile.getAttachDescription());
                attachment.setName(attachFile.getAttachName());
                email.attach(attachment);
            }

            // send the email
            return email.send();

        } catch (EmailException ex) {
            Logger.getLogger(TextMailWithAttachment.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "Not Sent";
    }
}
