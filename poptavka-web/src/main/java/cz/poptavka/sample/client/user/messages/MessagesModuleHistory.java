package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
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
@History(type = HistoryConverterType.DEFAULT)
public class MessagesModuleHistory implements HistoryConverter<MessagesModuleEventBus> {

    /*******************************************************************/
    /**           .DEVEL PRESENTER INITIALIZATION SECTION.               */
    private static final Logger LOGGER = Logger.getLogger(MessagesModuleHistory.class.getName());
    private static final String MESSAGES_INBOX = "initMessagesTabModuleInbox";
    private static final String MESSAGES_SENT = "initMessagesTabModuleSent";
    private static final String MESSAGES_DELETED = "initMessagesTabModuleTrash";
    private static final String COMPOSE_MESSAGE = "initMessagesTabComposeMail";

//    private MessageListPresenter messagePresenter = null;
    /**           DEVEL PRESENTER INITIALIZATION SECTION               */
    /*******************************************************************/
    @Override
    public void convertFromToken(String historyName, String param,
            MessagesModuleEventBus eventBus) {
        LOGGER.fine(" +++++++++ MESSAGES Name: " + historyName + "\nParam: " + param);

        GWT.log("history name called: " + historyName);

        //devel behaviour
        if (historyName.equals(MESSAGES_INBOX)) {
            eventBus.initMessagesTabModuleInbox(null);
        }

        if (historyName.equals(MESSAGES_SENT)) {
            eventBus.initMessagesTabModuleSent(null);
        }

        if (historyName.equals(MESSAGES_DELETED)) {
            eventBus.initMessagesTabModuleTrash(null);
        }

        if (historyName.equals(COMPOSE_MESSAGE)) {
            eventBus.initMessagesTabComposeMail(null, null);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

    public String onInitMessagesTabModuleInbox(SearchModuleDataHolder filter) {
        return "onInitMessagesTabModuleInbox";
    }

    public String onInitMessagesTabModuleSent(SearchModuleDataHolder filter) {
        return "onInitMessagesTabModuleSent";
    }

    public String onInitMessagesTabModuleTrash(SearchModuleDataHolder filter) {
        return "onInitMessagesTabModuleTrash";
    }

    public String onInitMessagesTabComposeMail(MessageDetail messageDetail, String action) {
        return "onInitMessagesTabComposeMail";
    }
}
