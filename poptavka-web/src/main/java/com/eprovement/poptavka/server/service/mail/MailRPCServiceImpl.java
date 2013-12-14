package com.eprovement.poptavka.server.service.mail;

import com.eprovement.poptavka.validation.EmailValidator;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;

import com.eprovement.poptavka.client.service.demand.MailRPCService;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.shared.domain.message.ContactUsDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

/**
 * RPC service implementation for sending mails
 *
 * @author kolkar
 *
 */
@Configurable
public class MailRPCServiceImpl extends AutoinjectingRemoteService implements
        MailRPCService {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(MailRPCServiceImpl.class);

    private static final String DEFAULT_NOTIFICATION_MAIL_SENDER = "noreply@want-something.com";

    private MailService mailService;
    private String notificationMailSender = DEFAULT_NOTIFICATION_MAIL_SENDER;


    /**
     * Send notification email to the given recipient ({@code emailDialogDetail#getRecipient}).
     * <p>
     *     This implementation sends emails in an asynchronous way.
     * </p>
     * @param emailDialogDetail object describing all email details
     * @see #setNotificationMailSender(String)  for setting sender (FROM) for notification emails.
     */
    @Override
    public Boolean sendMail(ContactUsDetail emailDialogDetail) throws RPCException {
        LOGGER.info("Sending mail message to: " + emailDialogDetail.getRecipient());

        final SimpleMailMessage notificationMessage = new SimpleMailMessage();
        if (emailDialogDetail.getEmailFrom() != null) {
            notificationMessage.setCc(emailDialogDetail.getEmailFrom());
        }
        notificationMessage.setFrom(notificationMailSender);
        notificationMessage.setTo(emailDialogDetail.getRecipient());

        notificationMessage.setSubject(emailDialogDetail.getSubject());
        notificationMessage.setText(emailDialogDetail.getMessage());

        try {
            // send asynchronously to avoid blocking of normal execution
            this.mailService.sendAsync(notificationMessage);
        } catch (MailException me) {
            LOGGER.warn(
                    "An error occured while sending exception notification mail: ",
                    me);
            throw me;
        }

        return true;
    }

    @Autowired
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Value("${mail.noreply.address}")
    public void setNotificationMailSender(String notificationMailSender) {
        Validate.isTrue(EmailValidator.getInstance().isValid(notificationMailSender),
                "notificationMailSender is not a valid email address)");
        this.notificationMailSender = notificationMailSender;
    }


}
