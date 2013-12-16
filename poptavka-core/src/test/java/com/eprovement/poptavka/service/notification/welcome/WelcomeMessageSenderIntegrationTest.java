package com.eprovement.poptavka.service.notification.welcome;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import com.eprovement.poptavka.base.integration.DBUnitIntegrationTest;
import com.eprovement.poptavka.base.integration.DataSet;
import com.eprovement.poptavka.dao.message.MessageFilter;
import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.service.message.MessageService;
import com.googlecode.genericdao.search.Search;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

@DataSet(path = {
        "classpath:com/eprovement/poptavka/domain/register/RegisterDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/address/LocalityDataSet.xml",
        "classpath:com/eprovement/poptavka/domain/user/UsersDataSet.xml"
        }, dtd = "classpath:test.dtd")
public class WelcomeMessageSenderIntegrationTest extends DBUnitIntegrationTest {

    private static final String WELCOME_MAIL_TEXT = "Welcome New Client!";
    @Autowired
    private WelcomeMessageSender welcomeMessageSender;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private MessageService messageService;
    /** Mail service mock is set up in "test" bean profile */
    @Autowired
    private MailService mailServiceMock;

    @Test
    public void testSendWelcomeMessage() throws Exception {
        final Search userByEmail = new Search(User.class);
        // find user for testing - make sure to not select user which has "INSTANT" notification settings,
        // otherwise he will get two emails immediately - one "welcome email" itself and one notification email
        // about new internal "welcome message"
        userByEmail.addFilterEqual("email", "lisohlavka@email.com");
        final User user = (User) generalService.searchUnique(userByEmail);
        Assert.assertNotNull("user should not be null", user);
        welcomeMessageSender.sendWelcomeMessage(user);

        checkWelcomeEmailSent();
        // TODO juraj welcome: right now, the internal welcome message is not sent -> se have to set up system account
        // for these things and then enable messageNotificationSender
//        checkInternalWelcomeMessageSent(user);
    }

    private void checkWelcomeEmailSent() {
        final ArgumentCaptor<SimpleMailMessage> welcomeEmail = ArgumentCaptor.forClass(SimpleMailMessage.class);
        // beware that mailServiceMock can be called in other tests and influence the verification
        verify(mailServiceMock, atLeast(1)).sendAsync(welcomeEmail.capture());
        assertNotNull("welcome email should not be null", welcomeEmail.getValue());
        assertThat("welcome email should have proper body", welcomeEmail.getValue().getText(), is(WELCOME_MAIL_TEXT));
    }

    private void checkInternalWelcomeMessageSent(User elvira) {
        final List<Message> welcomeMessage = messageService.getAllMessages(elvira, MessageFilter.EMPTY_FILTER);
        assertNotNull(welcomeMessage);
        assertThat("Exactly one internal welcome message expected", welcomeMessage.size(), is(1));
        assertThat("Incorrect welcome message body", welcomeMessage.get(0).getBody(), is(WELCOME_MAIL_TEXT));
    }
}
