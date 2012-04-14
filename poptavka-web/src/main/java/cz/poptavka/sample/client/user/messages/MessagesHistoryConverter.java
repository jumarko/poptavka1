package cz.poptavka.sample.client.user.messages;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * History converter class. Handles history for MessagesModule.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "messages")
public class MessagesHistoryConverter implements HistoryConverter<MessagesEventBus> {

    private static final String MESSAGES_COMPOSE_NEW_TEXT = "messagesComposeNew";
    private static final String MESSAGES_COMPOSE_REPLY_TEXT = "messagesComposeReply";
    private static final String MESSAGES_INBOX_TEXT = "messagesInbox";
    private static final String MESSAGES_SENT_TEXT = "messagesSent";
    private static final String MESSAGES_DRAFT_TEXT = "messagesDraft";
    private static final String MESSAGES_TRASH_TEXT = "messagesTrash";
    private static final String MESSAGES_NONE = "messagesWelcome"; //for further usage

    /**
     * Created token(URL) for goToDemandModule method.
     *
     * @param searchDataHolder - Provided by search module. Holds data to filter.
     * @param loadWidget - Constant from class Constants. Tells which view to load.
     * @return token string like module/method?param, where param = messagesInbox, ....
     */
    public String onGoToMessagesModule(SearchModuleDataHolder searchDataHolder, int loadWidget) {
        switch (loadWidget) {
            case Constants.MESSAGES_COMPOSE_NEW:
                return MESSAGES_COMPOSE_NEW_TEXT;
            case Constants.MESSAGES_COMPOSE_REPLY:
                return MESSAGES_COMPOSE_REPLY_TEXT;
            case Constants.MESSAGES_INBOX:
                return MESSAGES_INBOX_TEXT;
            case Constants.MESSAGES_SENT:
                return MESSAGES_SENT_TEXT;
            case Constants.MESSAGES_DRAFT:
                return MESSAGES_DRAFT_TEXT;
            case Constants.MESSAGES_TRASH:
                return MESSAGES_TRASH_TEXT;
            default:
                return MESSAGES_NONE;
        }
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onGoToMessagesModule method in MessagesModuleHistoryConverter class.
     * @param eventBus - MessagesModuleEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, MessagesEventBus eventBus) {
        eventBus.clickMessagesUserMenuStyleChange();
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
