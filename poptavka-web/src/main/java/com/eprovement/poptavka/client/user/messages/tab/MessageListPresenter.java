package com.eprovement.poptavka.client.user.messages.tab;

import com.google.gwt.cell.client.FieldUpdater;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.messages.MessagesEventBus;
import com.eprovement.poptavka.client.user.messages.MessagesTabViewView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.view.client.MultiSelectionModel;
import java.util.ArrayList;

@Presenter(view = MessageList.class)
public class MessageListPresenter extends LazyPresenter<MessageListPresenter.IListM, MessagesEventBus> {

    public interface IListM extends LazyView, IsWidget {

        /** Table related. **/
        UniversalAsyncGrid<MessageDetail> getGrid();

        MultiSelectionModel<MessageDetail> getSelectionModel();

        UniversalPagerWidget getPager();

        /** Columns. **/
        Column<MessageDetail, Boolean> getCheckColumn();

        Column<MessageDetail, String> getSenderColumn();

        Column<MessageDetail, String> getSubjectColumn();

        Column<MessageDetail, String> getDateColumn();

        /** Buttons. **/
        Button getReplyBtn();

        /** Action Box. **/
        DropdownButton getActionBox();

        NavLink getActionRead();

        NavLink getActionUnread();

        NavLink getActionStar();

        NavLink getActionUnstar();

        /** Others. **/
        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //viewType
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        addButtonsHandlers();
        addActionBoxChoiceHandlers();
        addTextColumnFieldUpdaters();
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Init view and fetch new supplier's demands. Demand request
     * is sent ONLY for the first time - when view is loaded.
     *
     * Associated DetailWrapper widget is created and initialized.
     */
    public void onInitInbox(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.MESSAGES_INBOX);
        //Set visibility
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.setUpSearchBar(new MessagesTabViewView());
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Display methods                                                        */
    /**************************************************************************/
    public void onDisplayInboxMessages(List<MessageDetail> inboxMessages) {
        view.getGrid().getDataProvider().updateRowData(view.getGrid().getStart(), inboxMessages);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Get IDs of selected objects.
     * @return
     */
    public List<Long> getSelectedUserMessageIds() {
        List<Long> idList = new ArrayList<Long>();
        for (MessageDetail detail : view.getSelectionModel().getSelectedSet()) {
            idList.add(detail.getUserMessageId());
        }
        return idList;
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /** Field updater. **/
    //--------------------------------------------------------------------------
    /**
     * Show and loads detail section. Show after clicking on certain columns that
     * have defined this fieldUpdater.
     */
    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<ClientDemandConversationDetail, String>() {
            @Override
            public void update(int index, ClientDemandConversationDetail object, String value) {
                object.setIsRead(true);

                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
            }
        };
        view.getSenderColumn().setFieldUpdater(textFieldUpdater);
        view.getSubjectColumn().setFieldUpdater(textFieldUpdater);
        view.getDateColumn().setFieldUpdater(textFieldUpdater);
    }

    /** Buttons handers. **/
    //--------------------------------------------------------------------------
    private void addButtonsHandlers() {
        view.getReplyBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });
    }

    /** Action box handers. **/
    //--------------------------------------------------------------------------
    private void addActionBoxChoiceHandlers() {
        view.getActionRead().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnread().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestReadStatusUpdate(getSelectedUserMessageIds(), false);
            }
        });
        view.getActionStar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(getSelectedUserMessageIds(), true);
            }
        });
        view.getActionUnstar().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestStarStatusUpdate(getSelectedUserMessageIds(), false);
            }
        });
    }
}
