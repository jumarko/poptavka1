package com.eprovement.poptavka.client.root.header;

import com.eprovement.poptavka.client.common.session.Constants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.ClosingEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView;
import com.eprovement.poptavka.client.root.interfaces.IHeaderView.IHeaderPresenter;
import java.util.logging.Logger;

@Presenter(view = HeaderView.class)
public class HeaderPresenter extends BasePresenter<IHeaderView, RootEventBus>
        implements IHeaderPresenter {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final Logger LOGGER = Logger.getLogger(HeaderPresenter.class.getName());

    public void onStart() {
        // empty
    }

    public void onAtHome() {
        GWT.log("Header presenter loaded");
        eventBus.setHeader(view);
    }

    @Override
    public void bind() {
        view.getLoginLink().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.login(Constants.NONE);
            }
        });
        Window.addWindowClosingHandler(new Window.ClosingHandler() {
            @Override
            public void onWindowClosing(ClosingEvent event) {
                //TODO LATER Martin - musi to tu byt, pouzivame vobec cookies?
//                Cookies.setCookie("login", "no");
            }
        });
    }
}