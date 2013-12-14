/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.homeWelcome;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;
import com.eprovement.poptavka.client.homeWelcome.texts.HowItWorks.HowItWorksViews;

/**
 * Manages history for HomeWelcome module.
 *
 * @author Martin Slavkovsky
 */
@History(type = HistoryConverterType.DEFAULT, name = "homeWelcome")
public class HomeWelcomeHistoryConverter implements HistoryConverter<HomeWelcomeEventBus> {

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Creates token(URL) for goToHomeWelcomeModule method.
     *
     * @param searchDataHolder
     * @return token string like module/method?param, where param = welcome
     */
    public String onGoToHomeWelcomeModule() {
        return "welcome";
    }

    /**
     * Creates custom history token.
     * @param param that is added at the end of token.
     * @return constucted history token
     */
    public String onCreateCustomToken(String param) {
        return param;
    }

    /**************************************************************************/
    /* ConvertToToken events                                                  */
    /**************************************************************************/
    /**
     * Convert history token into action.
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
        eventBus.setHistoryStoredForNextOne(false);
        if (HowItWorksViews.HOW_IT_WORKS_DEMAND.getValue().equals(param)) {
            eventBus.displayHowItWorkdsDemands();
        } else if (HowItWorksViews.HOW_IT_WORKS_SUPPLIER.getValue().equals(param)) {
            eventBus.displayHowItWorkdsSuppliers();
        } else {
            eventBus.goToHomeWelcomeModule();
        }
    }

    /**************************************************************************/
    /* Other events                                                           */
    /**************************************************************************/
    @Override
    public boolean isCrawlable() {
        return false;
    }
}
