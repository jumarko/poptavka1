package com.eprovement.poptavka.client.user.messages.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;


import com.mvp4g.client.presenter.LazyPresenter;

import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.messages.MessagesEventBus;
import com.eprovement.poptavka.client.user.messages.tab.ComposeMessagePresenter.IComposeMessage;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;

@Presenter(view = ComposeMessageView.class)
public class ComposeMessagePresenter extends LazyPresenter<IComposeMessage, MessagesEventBus> {

    public interface IComposeMessage extends LazyView, IsWidget {

        Widget getWidgetView();

        HTMLPanel getWrapperPanel();

        TextBox getRecipientTextBox();

        MessageDetail getMessage(MessageDetail detail);

        void clearMessage();

        //control buttons getters
        Button getSendBtn();

        Button getDiscardBtn();
    }
    private String action = null;
    private MessageDetail messageDetail = null;
    private BusinessUserDetail userDetail = null;

    /** Defines button actions. */
    @Override
    public void bindView() {
        view.getSendBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                MessageDetail msgDetail = view.getMessage(messageDetail);
                eventBus.sendMessage(msgDetail, action);
            }
        });
        view.getDiscardBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initInbox(null);
            }
        });
    }

    public void onInitComposeNew() {
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_COMPOSE_NEW);

        //TODO LATER Martin Uzivatel musi vybrat uzivatela z kontaktov

        view.clearMessage();
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onInitComposeReply(MessageDetail msgDetail) {
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_COMPOSE_REPLY);

        if (msgDetail != null) {
            messageDetail = msgDetail;
        }
        //Ak forwardnuty z kliku na nejakeho uzivatela, uzivatel sa automaticky nastavy ako recipient
        eventBus.requestUserInfo(messageDetail.getSenderId());

        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onResponseUserInfo(BusinessUserDetail userDetail) {
        this.userDetail = userDetail;
        view.getRecipientTextBox().setText(userDetail.getEmail());
    }
}