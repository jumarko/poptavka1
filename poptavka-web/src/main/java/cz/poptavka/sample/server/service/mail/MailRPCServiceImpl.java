package cz.poptavka.sample.server.service.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import cz.poptavka.sample.client.service.demand.MailRPCService;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.mail.MailService;

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

    private static final String NOTIFICATION_MAIL_FROM = "poptavka1@gmail.com";

    private MailService mailService;

    /**
     * Method used to send messages using gmail SMTP server, in future possibly
     * replaced by our own SMTP server.
     *
     */
    @Override
    public Boolean sendMail(String recipient, String body, String subject,
            String sender) {
        LOGGER.info("Sending mail message to: " + recipient);

        final SimpleMailMessage exceptionNotificationMessage = new SimpleMailMessage();
        exceptionNotificationMessage.setFrom(NOTIFICATION_MAIL_FROM);
        exceptionNotificationMessage.setTo(recipient);

        exceptionNotificationMessage.setSubject(subject);
        exceptionNotificationMessage.setText(body);

        try {
            // send asynchronously to avoid blocking of normal execution
            this.mailService.sendAsync(exceptionNotificationMessage);
        } catch (MailException me) {
            LOGGER.warn("An error occured while sending exception notification mail: ", me);
        }

        return true;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

}
