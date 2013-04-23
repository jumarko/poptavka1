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
import com.eprovement.poptavka.client.user.widget.detail.MessageDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import java.util.ArrayList;
import java.util.Set;

@Presenter(view = MessageListView.class)
public class MessageListPresenter extends
        LazyPresenter<MessageListPresenter.MessageListViewInterface, MessagesEventBus> {

    public interface MessageListViewInterface extends LazyView, IsWidget {

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

        /** Others. **/
        MessageDetailView getMessageDetailView();

        SimplePanel getActionBox();

        SimplePanel getWrapperPanel();

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
        addTableSelectionModelClickHandler();
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
        eventBus.initActionBox(view.getActionBox(), view.getGrid());
        //Set visibility
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
    public void onDisplayInboxMessages(ArrayList<MessageDetail> inboxMessages) {
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
    public void addTableSelectionModelClickHandler() {
        view.getGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Set<MessageDetail> selectedSet = ((MultiSelectionModel<MessageDetail>) view.getGrid()
                        .getSelectionModel()).getSelectedSet();
                //set actionBox visibility
                view.getActionBox().setVisible(selectedSet.size() > 0);
                //init details
                if (selectedSet.size() == 1) {
                    MessageDetail selected = selectedSet.iterator().next();
                    view.getMessageDetailView().setVisible(true);
                    view.getMessageDetailView().setMessageDetail(selected);
                } else {
                    view.getMessageDetailView().setVisible(false);
                }
            }
        });
    }

    /** Field updater. **/
    //--------------------------------------------------------------------------
    /**
     * Show and loads detail section. Show after clicking on certain columns that
     * have defined this fieldUpdater.
     */
    public void addTextColumnFieldUpdaters() {
        textFieldUpdater = new FieldUpdater<MessageDetail, String>() {
            @Override
            public void update(int index, MessageDetail object, String value) {
                object.setRead(true);

                MultiSelectionModel selectionModel = (MultiSelectionModel) view.getGrid().getSelectionModel();
                selectionModel.clear();
                selectionModel.setSelected(object, true);
                view.getWrapperPanel().setVisible(false);
                view.getMessageDetailView().setVisible(true);
                view.getMessageDetailView().setMessageDetail(object);
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
}
