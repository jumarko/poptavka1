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

//@Presenter(view = SupplierList.class)
@Presenter(view = ComposeMessageView.class, multiple = true)
public class ComposeMessagePresenter extends LazyPresenter<IComposeMessage, MessagesModuleEventBus> {

    public interface IComposeMessage extends LazyView, IsWidget {

        Widget getWidgetView();

        HTMLPanel getWrapperPanel();

        TextBox getRecipientTextBox();

        MessageDetail getMessage(MessageDetail detail);

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
                //TODO Martin - co tam ma byt? aky ViewType?? - USER
                MessageDetail msgDetail = view.getMessage(messageDetail);
                eventBus.sendMessage(msgDetail, action);
            }
        });
        view.getDiscardBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initMessagesTabModuleInbox(null);
            }
        });
    }

    /**
     * Init view and fetch new supplier's demands. Demand request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitMessagesTabComposeMail(MessageDetail msgDetail, String action) {
        Storage.setCurrentlyLoadedView(null);
        this.action = action; // what is that for?
        messageDetail = msgDetail;

        if (messageDetail == null) {
            //TODO Martin Uzivatel musi vybrat uzivatela z kontaktov
        } else {
            //Ak forwardnuty z kliku na nejakeho uzivatela, uzivatel sa automaticky nastavy ako recipient
            eventBus.requestUserInfo(messageDetail.getSenderId());
        }

        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }

    public void onResponseUserInfo(UserDetail userDetail) {
        this.userDetail = userDetail;
        view.getRecipientTextBox().setText(userDetail.getEmail());
    }
}