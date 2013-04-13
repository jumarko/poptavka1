package com.eprovement.poptavka.client.user.messages;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.history.NavigationEventCommand;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 */
@Presenter(view = MessagesView.class, multiple = true)
public class MessagesPresenter
        extends LazyPresenter<MessagesPresenter.MessagesLayoutInterface, MessagesEventBus>
        implements NavigationConfirmationInterface {

    private SearchModuleDataHolder filter = null;

    public interface MessagesLayoutInterface extends LazyView, IsWidget {

        SimplePanel getContentPanel();

        Button getMessagesInbox();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getMessagesInbox().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToMessagesModule(filter, Constants.MESSAGES_INBOX);
            }
        });
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    /**
     * Every call of onForward method invokes updateUnreadMessagesCount event that is secured thus user without
     * particular access role can't access it and loginPopupView will be displayed.
     */
    public void onForward() {
        //Must be set before any widget start initialize because of autoDisplay feature
        if (!(Storage.getUser() == null && Storage.isAppCalledByURL() != null && Storage.isAppCalledByURL())) {
            eventBus.updateUnreadMessagesCount();
        }
        eventBus.setBody(view.getWidgetView());
        eventBus.userMenuStyleChange(Constants.USER_MESSAGES_MODULE);
    }

    @Override
    public void confirm(NavigationEventCommand event) {
        // nothing
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Initialize view for message module.
     * @param filter
     */
    public void onGoToMessagesModule(SearchModuleDataHolder filter, int loadWidget) {
        this.filter = filter;

        eventBus.initInbox(filter);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onDisplayView(IsWidget content) {
        view.getContentPanel().setWidget(content);
    }
}
