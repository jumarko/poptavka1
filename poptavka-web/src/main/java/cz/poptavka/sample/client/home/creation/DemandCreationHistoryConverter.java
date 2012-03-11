package cz.poptavka.sample.client.home.creation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.SIMPLE, name = "demandCreation")
public class DemandCreationHistoryConverter implements HistoryConverter<DemandCreationEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    /**
     * To convert token for initCreateDemand method
     * @param searchDataHolder
     * @param location
     * @return token string like module/method?param, where param = home or user
     */
    public String convertToToken(String methodName, String location) {
        if (methodName.equals("goToCreateDemandModule")) {
            return location;
        }
        return "";
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
    public void convertFromToken(String methodName, String param, DemandCreationEventBus eventBus) {
        if (methodName.equals("goToCreateDemandModule")) {
            eventBus.goToCreateDemandModule(param);
        }
    }

    @Override
    public boolean isCrawlable() {
        // TODO Auto-generated method stub
        return false;
    }
}
