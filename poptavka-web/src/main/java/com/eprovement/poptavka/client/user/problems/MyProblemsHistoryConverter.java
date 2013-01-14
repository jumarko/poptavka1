package com.eprovement.poptavka.client.user.problems;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;


@History(type = HistoryConverterType.NONE)
public class MyProblemsHistoryConverter implements HistoryConverter<RootEventBus> {

    private static final String PROBLEMS_MY = "invokeMyProblems";
    private MyProblemsPresenter myProblemsPresenter = null;

    @Override
    public void convertFromToken(String historyName, String param,
            RootEventBus eventBus) {
        String cookie = Cookies.getCookie("user-presenter");
        if (cookie.equals("loaded")) {

            GWT.log(historyName);

            if (historyName.equals(PROBLEMS_MY)) {
                if (myProblemsPresenter != null) {
                    eventBus.removeHandler(myProblemsPresenter);
                }
                myProblemsPresenter = eventBus.addHandler(MyProblemsPresenter.class);
                myProblemsPresenter.onInvokeMyProblems();
            }
        } else {
            eventBus.atAccount();
//            eventBus.markEventToLoad(historyName);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
