package com.eprovement.poptavka.client.home;

import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

@History(type = HistoryConverterType.DEFAULT, name = "home")
public class HomeHistoryConverter implements HistoryConverter<HomeEventBus> {

    @Override
    public void convertFromToken(String historyName, String param, HomeEventBus eventBus) {
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }
}
