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
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import java.util.HashMap;
import java.util.Map;

//@Presenter(view = SupplierList.class)
@Presenter(view = ComposeMessageView.class, multiple = true)
public class ComposeMessagePresenter extends LazyPresenter<IComposeMessage, MessagesModuleEventBus> {

    public interface IComposeMessage extends LazyView, IsWidget {

        Widget getWidgetView();

        HTMLPanel getWrapperPanel();

        TextBox getRecipientTextBox();

        MessageDetail getMessage();

        //control buttons getters
        Button getSendBtn();

        Button getDiscardBtn();
    }
    private String action = null;

    /** Defines button actions. */
    @Override
    public void bindView() {
        view.getSendBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                //TODO Martin - co tam ma byt? aky ViewType?? - USER
                eventBus.sendMessage(view.getMessage(), action);
            }
        });
        view.getDiscardBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                //TODO Martin
            }
        });
    }

    /**
     * Init view and fetch new supplier's demands. Demand request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitMessagesTabComposeMail(UserDetail recipient, String action) {
        Storage.setCurrentlyLoadedView(null);
        this.action = action;

        if (recipient == null) {
            //TODO Martin Uzivatel musi vybrat uzivatela z kontaktov
        } else {
            //Ak forwardnuty z kliku na nejakeho uzivatela, uzivatel sa automaticky nastavy ako recipient
            view.getRecipientTextBox().setValue(recipient.getUserId().toString());
            view.getRecipientTextBox().setText(recipient.getEmail());
        }

        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayMain(view.getWidgetView());
    }
    private final Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
}
