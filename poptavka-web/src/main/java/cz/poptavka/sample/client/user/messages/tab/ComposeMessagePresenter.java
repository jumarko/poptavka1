package cz.poptavka.sample.client.user.messages.tab;

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
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.messages.MessagesModuleEventBus;
import cz.poptavka.sample.client.user.messages.tab.ComposeMessagePresenter.IComposeMessage;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;

@Presenter(view = ComposeMessageView.class)
public class ComposeMessagePresenter extends LazyPresenter<IComposeMessage, MessagesModuleEventBus> {

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
    private UserDetail userDetail = null;

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
        Storage.setCurrentlyLoadedView(null);

        //TODO Martin Uzivatel musi vybrat uzivatela z kontaktov

        view.clearMessage();
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onInitComposeReply(MessageDetail msgDetail) {
        Storage.setCurrentlyLoadedView(null);

        if (msgDetail != null) {
            messageDetail = msgDetail;
        }
        //Ak forwardnuty z kliku na nejakeho uzivatela, uzivatel sa automaticky nastavy ako recipient
        eventBus.requestUserInfo(messageDetail.getSenderId());

        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onResponseUserInfo(UserDetail userDetail) {
        this.userDetail = userDetail;
        view.getRecipientTextBox().setText(userDetail.getEmail());
    }
}