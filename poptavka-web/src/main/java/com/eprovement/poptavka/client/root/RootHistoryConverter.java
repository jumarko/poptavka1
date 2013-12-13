package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.root.footer.texts.FooterInfo.FooterInfoViews;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

/**
 * Manages history for Root module.
 * <b><i>Note:</i></b>
 * Using history conterter type DEFAULT recquires implementing each method
 * defined in eventBus that is handled by history converter. Those method are also known
 * as <b>convertToToken</b> methods. They creates appropriate URL tokens which are stored in history.
 * There is also <b>convertFromToken</b> method that must be overriden. This method
 * parse URL tokens on history event and fires appriopriate action.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "root")
public class RootHistoryConverter implements HistoryConverter<RootEventBus> {

    /**
     * Creates token for history.
     * @param param string that is added to URL
     * @return URL token
     */
    public String onCreateCustomToken(String param) {
        return param;
    }

    /**
     * Converts URL tokens thant belongs to root module and fires appripriate action.
     * <b><i>Note:</i></b>
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

    /**
     * Sets content to be crawlable for search engines.
     * <b><i>Note:</i></b>
     * Allow crawlable content only when the content is unique.
     * Otherwise search engines can give you low rank.
     * @return true if crawlable content is allowed, false otherwise
     */
    @Override
    public boolean isCrawlable() {
        return false;
    }
}