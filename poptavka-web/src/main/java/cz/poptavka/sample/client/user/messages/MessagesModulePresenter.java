package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.user.messages.tab.MessageListPresenter;

/**
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesModuleView.class, multiple = true)
public class MessagesModulePresenter
        extends BasePresenter<MessagesModulePresenter.MessagesLayoutInterface, MessagesModuleEventBus> {

    public interface MessagesLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        //beho devel section
        Button getInboxButton();

        Button getSentButton();

        Button getTrashButton();

        SimplePanel getContentPanel();
    }
    //devel attribute
    private MessageListPresenter supList = null;

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
        //DEVEl - BEHO
        view.getInboxButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
                if (supList != null) {
                    supList.develRemoveDetailWrapper();
                    eventBus.removeHandler(supList);
                    supList = null;
                    view.getContentPanel().remove(view.getContentPanel().getWidget());
                }
                supList = eventBus.addHandler(MessageListPresenter.class);
                supList.onInitInbox();

                //production code
//                eventBus.initInbox();
            }
        });
        view.getSentButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
                if (supList != null) {
                    supList.develRemoveDetailWrapper();
                    eventBus.removeHandler(supList);
                    supList = null;
                    view.getContentPanel().remove(view.getContentPanel().getWidget());
                }
                supList = eventBus.addHandler(MessageListPresenter.class);
                supList.onInitSent();

                //production code
//                eventBus.initSent();
            }
        });
        view.getTrashButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                //devel code
//                if (supList != null) {
//                    supList.develRemoveDetailWrapper();
//                    eventBus.removeHandler(supList);
//                    supList = null;
//                    view.getContentPanel().remove(view.getContentPanel().getWidget());
//                }
//                supList = eventBus.addHandler(MessageListPresenter.class);
//                supList.onInitDeleted();

                //production code
                eventBus.initTrash();
            }
        });
    }

    //TODO
    //later add UserDetail as parameter
    public void onInitMessagesModule() {
        // hiding window for this is after succesfull Userhandler call
        Storage.showLoading(Storage.MSGS.progressMessagesLayoutInit());
//        if (user.getRoleList().contains(Role.CLIENT)) {
        // TODO execute client specific demands init methods/calls
//        }
//        if (user.getRoleList().contains(Role.SUPPLIER)) {
        // TODO using businessUserId and NOT supplier ID
        // DEBUGING popup
        // TODO Maybe do nothing
//            PopupPanel panel = new PopupPanel(true);
//            panel.getElement().setInnerHTML("<br/>Getting SupplierDemands<")
//            panel.center();
//            eventBus.getPotentialDemands(user.getId());
//        }

//        panel.setWidget(view.getWidgetView());
        view.getWidgetView().setStyleName(Storage.RSCS.common().user());
        eventBus.setBodyHolderWidget(view.getWidgetView());
        Storage.hideLoading();
//        eventBus.setTabWidget(view.getWidgetView());
//        eventBus.fireMarkedEvent();
//
//        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onDisplayView(Widget content) {
        view.setContent(content);
    }
}
