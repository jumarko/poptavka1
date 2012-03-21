package cz.poptavka.sample.client.user.messages;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

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
@History(type = HistoryConverterType.SIMPLE)
public class MessagesModuleHistoryConverter implements HistoryConverter<MessagesModuleEventBus> {

    /**
     * Creates tokens for methods from eventBus that has set history converter.
     * @param methodName
     * @return
     */
    public String convertToToken(String methodName) {
        return methodName;
    }

    public String convertToToken(String methodName, MessageDetail msgDetail) {
        return methodName;
    }

    public String convertToToken(String methodName, SearchModuleDataHolder searchDataHolder) {
        return methodName;
    }

    /**
     * Called when browser action <b>back</b> or <b>forward</b> is evocated.
     * Or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param). Url generates convertToToken method.
     * @param eventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, MessagesModuleEventBus eventBus) {
        eventBus.goToMessagesModule(null, Integer.valueOf(historyName));
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
