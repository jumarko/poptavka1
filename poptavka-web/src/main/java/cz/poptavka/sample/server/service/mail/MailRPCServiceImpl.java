package cz.poptavka.sample.server.service.mail;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cz.poptavka.sample.client.service.demand.MailRPCService;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;

/**
 * RPC service implementation for sending mails
 *
 * @author kolkar
 *
 */
public class MailRPCServiceImpl extends AutoinjectingRemoteService implements
        MailRPCService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MailRPCServiceImpl.class);

    /**
     * Method used to send messages using gmail SMTP server, in future possibly
     * replaced by our own SMTP server.
     *
     */
    @Override
    public Boolean sendMail(String recipient, String body, String subject,
            String sender) {
        LOGGER.info("Sending mail message to: " + recipient);

        final String username = "kolkar100@gmail.com";
        final String password = "8809117823";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        Message simpleMessage = new MimeMessage(session);

        InternetAddress fromAddress = null;
        InternetAddress toAddress = null;
        try {
            fromAddress = new InternetAddress(sender);
            toAddress = new InternetAddress(recipient);
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            simpleMessage.setFrom(fromAddress);
            simpleMessage.setRecipient(RecipientType.TO, toAddress);
            simpleMessage.setSubject(subject);
            simpleMessage.setText(body);

            Transport.send(simpleMessage);
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

}
