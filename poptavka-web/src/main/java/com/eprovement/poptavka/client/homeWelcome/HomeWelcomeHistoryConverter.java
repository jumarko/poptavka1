package com.eprovement.poptavka.client.homeWelcome;

import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks.HowItWorksViews;
import com.eprovement.poptavka.client.root.info.FooterInfo;
import com.eprovement.poptavka.client.root.info.FooterInfo.FooterInfoViews;

/**
 * History converter class. Handles history for HomeWelcomeModule
 *
 * @author slavkovsky.martin
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeWelcome")
public class HomeWelcomeHistoryConverter implements HistoryConverter<HomeWelcomeEventBus> {

    /**
     * Creates token(URL) for goToHomeWelcomeModule method.
     *
     * @param searchDataHolder
     * @return token string like module/method?param, where param = welcome
     */
    public String onGoToHomeWelcomeModule() {
        return "welcome";
    }

    public String onCreateCustomToken(String param) {
        return param;
    }

    /**
     * Called when browser action <b>back</b> or <b>forward</b> is evocated.
     * Or by clicking on <b>hyperlink</b> with set token.
     *
     * @param methodName - name of the called method
     * @param param - string behind '?' in url (module/method?param).
     *                URL creates method onGoToHomeWelcomeModule in HomeWelcomeHistoryConverter class.
     * @param eventBus - HomeWelcomeEventBus
     */
    @Override
    public void convertFromToken(String methodName, String param, HomeWelcomeEventBus eventBus) {
        if (HowItWorksViews.HOW_IT_WORKS_DEMAND.getValue().equals(param)) {
            eventBus.setBody(HowItWorks.createHowItWorksDemand());
        } else if (HowItWorksViews.HOW_IT_WORKS_SUPPLIER.getValue().equals(param)) {
            eventBus.setBody(HowItWorks.createHowItWorksSupplier());
        } else if (FooterInfoViews.ABOUT_US.getValue().equals(param)) {
            eventBus.setBody(FooterInfo.createAboutUs());
        } else if (FooterInfoViews.FAQ.getValue().equals(param)) {
            eventBus.setBody(FooterInfo.createFAQ());
        } else if (FooterInfoViews.PRIVACY_POLICY.getValue().equals(param)) {
            eventBus.setBody(FooterInfo.createPrivacyPolicy());
        } else {
            eventBus.goToHomeWelcomeModule();
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
