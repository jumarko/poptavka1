package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.messages.tab.ComposeMessagePresenter;
import cz.poptavka.sample.client.user.messages.tab.MessageListPresenter;

/**
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesModuleView.class, multiple = true)
public class MessagesModulePresenter
        extends BasePresenter<MessagesModulePresenter.MessagesLayoutInterface, MessagesModuleEventBus> {

    public interface MessagesLayoutInterface {

        Widget getWidgetView();

        SplitLayoutPanel getSplitPanel();

        SimplePanel getWrapperMain();

        SimplePanel getWrapperDetail();

        Button getComposeButton();

        Button getInboxButton();

        Button getSentButton();

        Button getDraftButton();

        Button getTrashButton();
    }
    //devel attribute
    private MessageListPresenter messagesList = null;
//    private ConversationWrapperPresenter detailSection = null;
    private ComposeMessagePresenter composer = null;

    @Override
    public void bind() {
        /**
        // MENU - CLIENT
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());
        view.setAllDemandsToken(getTokenGenerator().invokeAtDemands());
        view.setAllSuppliersToken(getTokenGenerator().invokeAtSuppliers());

        //MENU - SUPPLIER
        view.setPotentialDemandsToken(getTokenGenerator().invokePotentialDemands());
         */
        view.getComposeButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                composer = eventBus.addHandler(ComposeMessagePresenter.class);
                composer.onInitMessagesTabComposeMail(null, "composeNew");
                view.getWrapperDetail().clear();
//                view.getWrapperDetail().setVisible(false);
//                view.getSplitPanel().setSize("500px", "0px");
//                if (detailSection == null) {
//                    composer.compose(null, "composeNew");
//                } else {
//                    composer.compose(null, "composeReply");
//                }
            }
        });
        view.getInboxButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
//                if (messagesList != null) {
//                    detailSection.develRemoveReplyWidget();
//                    eventBus.removeHandler(messagesList);
//                    messagesList = null;
//                    view.getWrapperMain().remove(view.getWrapperMain().getWidget());
//                }
                messagesList = eventBus.addHandler(MessageListPresenter.class);
                messagesList.onInitMailBox("inbox", null);
                view.getWrapperDetail().clear();

                //production code
//                eventBus.initInbox();
            }
        });
        view.getSentButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
//                if (messagesList != null) {
//                    detailSection.develRemoveReplyWidget();
//                    eventBus.removeHandler(messagesList);
//                    messagesList = null;
//                    view.getWrapperMain().remove(view.getWrapperMain().getWidget());
//                }
                messagesList = eventBus.addHandler(MessageListPresenter.class);
                messagesList.onInitMailBox("sent", null);
                view.getWrapperDetail().clear();

                //production code
//                eventBus.initSent();
            }
        });
        view.getTrashButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
//                if (messagesList != null) {
//                    detailSection.develRemoveReplyWidget();
//                    eventBus.removeHandler(messagesList);
//                    messagesList = null;
//                    view.getWrapperMain().remove(view.getWrapperMain().getWidget());
//                }
                messagesList = eventBus.addHandler(MessageListPresenter.class);
                messagesList.onInitMailBox("trash", null);
                view.getWrapperDetail().clear();

                //production code
//                eventBus.initTrash();
            }
        });
        view.getDraftButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
//                if (messagesList != null) {
//                    detailSection.develRemoveReplyWidget();
//                    eventBus.removeHandler(messagesList);
//                    messagesList = null;
//                    view.getWrapperMain().remove(view.getWrapperMain().getWidget());
//                }
                messagesList = eventBus.addHandler(MessageListPresenter.class);
                messagesList.onInitMailBox("draft", null);
                view.getWrapperDetail().clear();

                //production code
//                eventBus.initTrash();
            }
        });
    }

    //TODO
    //later add UserDetail as parameter
    /**
     *
     * @param action - composeNew, composeNewForwarded, composeReply, displayGrid
     */
    public void onInitMessagesModule(SearchModuleDataHolder filter, String action) {
        Storage.showLoading(Storage.MSGS.progressMessagesLayoutInit());

        view.getWidgetView().setStyleName(Storage.RSCS.common().user());

        if (action.contains("composeNew")) { // composeNew, composeNewForwarded
//            if (composer == null) {
//                composer = eventBus.addHandler(ComposeMessagePresenter.class);
//                view.getWrapperMain().setWidget(composer.getView());// mozno treba opacne ako inde
//            }
//            view.getWrapperDetail().setWidth("0"); // ktore lepsie pouzit?
        } else if (action.equals("composeReply")) {
//            if (composer == null) {
//                composer = eventBus.addHandler(ComposeMessagePresenter.class);
//                view.getWrapperMain().setWidget(composer.getView());
//            }
//            if (detailSection == null) {
//                detailSection = eventBus.addHandler(ConversationWrapperPresenter.class);
//                view.getWrapperDetail().setWidget(detailSection.getView());
//                view.getWrapperDetail().setWidth("500");
//            }
        } else if (action.equals("displayGrid")) {
//            //Load MessageList
//            if (messagesList == null) {
//                messagesList = eventBus.addHandler(MessageListPresenter.class);
//                view.getWrapperMain().setWidget(messagesList.getView());
//            }
//            if (detailSection == null) {
//                detailSection = eventBus.addHandler(ConversationWrapperPresenter.class);
//                view.getWrapperDetail().setWidget(detailSection.getView());
//                view.getWrapperDetail().setWidth("500");
//            }
        } else { // inbox, sent, trash, draft
            eventBus.initMailBox(action, filter);
        }

        eventBus.setHomeBodyHolderWidget(view.getWidgetView());

        Storage.hideLoading();
    }

    public void onDisplayMain(Widget content) {
        view.getWrapperMain().setWidget(content);
    }

    public void onDisplayDetail(Widget content) {
        view.getWrapperDetail().setWidget(content);
    }
}
