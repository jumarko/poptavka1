package cz.poptavka.sample.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;

/**
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesModuleView.class, multiple = true)
public class MessagesModulePresenter
        extends BasePresenter<MessagesModulePresenter.MessagesLayoutInterface, MessagesModuleEventBus> {

    private SearchModuleDataHolder filter = null;

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

    @Override
    public void bind() {
        view.getComposeButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.initComposeNew();
                view.getWrapperDetail().clear();
            }
        });
        view.getInboxButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initInbox(filter);
                view.getWrapperDetail().clear();
            }
        });
        view.getSentButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initSent(filter);
                view.getWrapperDetail().clear();
            }
        });
        view.getTrashButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initTrash(filter);
                view.getWrapperDetail().clear();
            }
        });
        view.getDraftButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                eventBus.initDraft(filter);
                view.getWrapperDetail().clear();
            }
        });
    }

    /**
     * Initialize view for message module.
     * @param filter
     */
    public void onInitMessagesModule(SearchModuleDataHolder filter, String loadWidget) {
        Storage.setCurrentlyLoadedModule("messages");
        Storage.showLoading(Storage.MSGS.progressMessagesLayoutInit());
        this.filter = filter;

        //Need for search module. To have one entry point.
        if (loadWidget.equalsIgnoreCase("composeNew")) {
            eventBus.initComposeNew();
        } else if (loadWidget.equalsIgnoreCase("composeReply")) {
            Window.alert("I shouldn't be here. do I?");
        } else if (loadWidget.equalsIgnoreCase("sent")) {
            eventBus.initSent(filter);
        } else if (loadWidget.equalsIgnoreCase("trash")) {
            eventBus.initTrash(filter);
        } else if (loadWidget.equalsIgnoreCase("draft")) {
            eventBus.initDraft(filter);
        } else {
            eventBus.initInbox(filter);
        }

        view.getWidgetView().setStyleName(Storage.RSCS.common().user());
        eventBus.setHomeBodyHolderWidget(view.getWidgetView());

        Storage.hideLoading();
    }

    /**
     * MessageModule widget is divided into Main and Detail parts. This sets the main widgets.
     * @param content - widget to bet set as main part
     */
    public void onDisplayMain(Widget content) {
        view.getWrapperMain().setWidget(content);
    }

    /**
     * MessageModule widget is divided into Main and Detail parts. This sets the main widgets.
     * @param content - widget to be set as detail part
     */
    public void onDisplayDetail(Widget content) {
        view.getWrapperDetail().setWidget(content);
    }
}
