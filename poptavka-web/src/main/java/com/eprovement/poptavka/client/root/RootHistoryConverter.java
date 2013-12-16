package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.root.footer.texts.FooterInfo.FooterInfoViews;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * History converter class. Handles history for RootModule. Especially login/logout process.
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    public String onCreateCustomToken(String param) {
        return param;
    }

    /**
     * Called either when browser action <b>back</b> or <b>forward</b> is evocated,
     * or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates onAtHome & onAtAccount method in RootHistoryConverter class.
     * @param eventBus - RootEventBus
     */
    @Override
    public void convertFromToken(String historyName, String param, RootEventBus eventBus) {
        eventBus.setHistoryStoredForNextOne(false);
        if (FooterInfoViews.ABOUT_US.getValue().equals(param)) {
            eventBus.displayAboutUs();
        } else if (FooterInfoViews.FAQ.getValue().equals(param)) {
            eventBus.displayFaq();
        } else if (FooterInfoViews.PRIVACY_POLICY.getValue().equals(param)) {
            eventBus.displayPrivacyPolicy();
        } else if (FooterInfoViews.TERMS_AND_CONDITIONS.getValue().equals(param)) {
            eventBus.displayTermsAndConditions();
        } else {
            eventBus.goToHomeWelcomeModule();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
