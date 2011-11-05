package cz.poptavka.sample.application.logging;

import com.google.common.base.Preconditions;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * Aspect which is responsible for logging all uncaught exceptions that occur in any method in application.
 *
 * @author Juraj Martinka
 *         Date: 10.4.11
 */
@Aspect
public class ExceptionLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionLogger.class);

    private static final String NOTIFICATION_MAIL_FROM = "poptavka1@gmail.com";
    private static final int DEFAULT_STACKTRASE_SIZE = 1000;

    @Autowired
    private JavaMailSender javaMailSender;

    private List<String> recipients;


    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public void logExceptionMethod(Exception exception) {
        LOGGER.error("An exception has been thrown.", exception);
        sendNotificationMail(exception);
    }


    private void sendNotificationMail(Exception exception) {
        if (CollectionUtils.isNotEmpty(recipients)) {
            Preconditions.checkNotNull(exception);
            final SimpleMailMessage exceptionNotificationMessage = createNotificationMessage(exception);
            try {
                this.javaMailSender.send(exceptionNotificationMessage);
            } catch (MailException me) {
                LOGGER.warn("An error occured while sending exception notification mail: ", me);
            }

        }
    }

    private SimpleMailMessage createNotificationMessage(Exception exception) {
        final SimpleMailMessage exceptionNotificationMessage = new SimpleMailMessage();
        exceptionNotificationMessage.setFrom(NOTIFICATION_MAIL_FROM);
        exceptionNotificationMessage.setTo(recipients.toArray(new String[recipients.size()]));

        final StringWriter stackTraceWriter = new StringWriter(DEFAULT_STACKTRASE_SIZE);
        exception.printStackTrace(new PrintWriter(stackTraceWriter));
        exceptionNotificationMessage.setSubject(
                "Poptavka exception notification: "
                + exception.getMessage());
        exceptionNotificationMessage.setText(
                "Following exception occurs while executing Poptavka application [location=" + getApplicationLocation()
                        + "]: " + exception.getMessage()
                + "\nStacktrace:"
                + "\n" + stackTraceWriter.toString());
        return exceptionNotificationMessage;
    }

    private InetAddress getApplicationLocation() {
        InetAddress applicationLocation = null;
        try {
            applicationLocation = InetAddress.getLocalHost();
        } catch (UnknownHostException uhe) {
            LOGGER.warn("Cannot get application location (host)", uhe);
        }
        return applicationLocation;
    }

}
