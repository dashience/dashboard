/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.visumbu.mail;

import com.visumbu.vb.utils.PropertyReader;
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

    PropertyReader propReader = new PropertyReader();

    private final String hostName = "smtp.hostname";
    private final String setPort = "smtp.port";
    private final String authUser = "smtp.authuser";
    private final String authPass = "smtp.authpass";
    private final String fromAddress = "smtp.fromaddress";
    private final String tlsSslRequired = "smtp.tls.ssl.required";

    public TextMailWithAttachment(MailProperties props) {
        this.props = props;
    }

    public static void main(String argv[]) {
        MailProperties mailProps = new MailProperties();
        TextMailWithAttachment sender = new TextMailWithAttachment(mailProps);
        String[] attachments = {"/tmp/test.pdf"};
        sender.sendMail("sam@digitalanalystteam.com,aruljose445@gmail.com", "Test", "Test MEssage", Arrays.asList(attachments));
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
        this.props.setHostName(propReader.readProperties(hostName));
        int portNo = Integer.parseInt(propReader.readProperties(setPort));
        this.props.setPort(portNo);
        this.props.setAuthUser(propReader.readProperties(authUser));
        this.props.setAuthPasswd(propReader.readProperties(authPass));
        this.props.setFrom(propReader.readProperties(fromAddress));
        this.props.setSetSSLOnConnect(Boolean.parseBoolean(propReader.readProperties(tlsSslRequired)));
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
            email.setSSLOnConnect(props.isSetSSLOnConnect());
            email.setFrom(props.getFrom());
            email.setSubject(props.getSubject());
            email.setMsg(props.getTxtMessage());
            String[] toAddressArr = props.getTo().split(",");
            for (int i = 0; i < toAddressArr.length; i++) {
                String to = toAddressArr[i];
                email.addTo(to);
            }

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
