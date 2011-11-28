package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.messages.tab.MessagesPresenter;
import java.util.logging.Logger;

/**
 * History Converter for Demands tab in user interface. Instances of view
 * are singletons - loaded only once.
 *
 * During development phase will ALL these presenters set to multiple = true.
 * To achieve faster development without need of view refresh.
 *
 * For production this wil be set back to normal.
 *
 * @author Beho
 */
@History(type = HistoryConverterType.NONE)
public class MessagesHistoryConverter implements HistoryConverter<UserEventBus> {

    /*******************************************************************/
    /**           .DEVEL PRESENTER INITIALIZATION SECTION.               */
    private static final Logger LOGGER = Logger.getLogger(MessagesHistoryConverter.class.getName());
    private static final String MESSAGES_INBOX = "invokeInbox";
    private static final String MESSAGES_SENT = "invokeSent";
    private static final String MESSAGES_DELETED = "invokeDeleted";
    private MessagesPresenter messagesPresenter = null;

    /**           DEVEL PRESENTER INITIALIZATION SECTION               */
    /*******************************************************************/
    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        LOGGER.fine(" +++++++++ MESSAGES Name: " + historyName + "\nParam: " + param);

        GWT.log("history name called: " + historyName);

        //devel behaviour
        if (historyName.equals(MESSAGES_INBOX)) {
            eventBus.invokeInbox();
        }

        if (historyName.equals(MESSAGES_SENT)) {
            eventBus.invokeSent();
        }

        if (historyName.equals(MESSAGES_DELETED)) {
            eventBus.invokeDeleted();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
