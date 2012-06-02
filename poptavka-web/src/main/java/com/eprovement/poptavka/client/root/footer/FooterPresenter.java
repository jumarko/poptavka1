package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.eprovement.poptavka.client.root.interfaces.IFooterView.IFooterPresenter;

@Presenter(view = FooterView.class)
public class FooterPresenter extends BasePresenter<IFooterView, RootEventBus>
        implements IFooterPresenter {

    public void onStart() {
        // TODO praso toto je zrejme staticky pohlad takze ho mozeme optimalizovat podla
        // http://mvp4g.blogspot.com/2011/04/mvp-pattern-associated-with-event-bus.html
        // part 2
        GWT.log("Footer presenter loaded");
        eventBus.setFooter(view);
    }
}
