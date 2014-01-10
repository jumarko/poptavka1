package com.eprovement.poptavka.client.user.messages.widgets;

import com.google.gwt.cell.client.FieldUpdater;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.messages.MessagesEventBus;
import com.eprovement.poptavka.client.user.messages.MessagesSearchView;
import com.eprovement.poptavka.client.user.messages.toolbar.MessagesToolbarView;
import com.eprovement.poptavka.client.user.widget.detail.MessageDetailView;
import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Martin Slavkovsky
 */
@Presenter(view = MessageListView.class)
public class MessageListPresenter
    extends LazyPresenter<MessageListPresenter.MessageListViewInterface, MessagesEventBus> {

    public interface MessageListViewInterface extends LazyView, IsWidget {

        /** Table related. **/
        UniversalAsyncGrid<MessageDetail> getTable();

        void initTable(UniversalAsyncGrid table);

        /** Others. **/
        MessageDetailView getMessageDetailView();

        MessagesToolbarView getToolbar();

        List<Long> getSelectedUserMessageIds();

        Set getSelectedObjects();

        SimplePanel getAdvertisementPanel();

        SimplePanel getFooterContainer();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SearchModuleDataHolder searchDataHolder;
    private FieldUpdater textFieldUpdater = new FieldUpdater<TableDisplayUserMessage, String>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, String value) {
            object.setRead(true);

            MultiSelectionModel selectionModel = (MultiSelectionModel) view.getTable().getSelectionModel();
            selectionModel.clear();
            selectionModel.setSelected(object, true);
        }
    };
    private FieldUpdater starFieldUpdater = new FieldUpdater<TableDisplayUserMessage, Boolean>() {
        @Override
        public void update(int index, TableDisplayUserMessage object, Boolean value) {
            object.setStarred(!value);
            view.getTable().redrawRow(index);
            eventBus.requestStarStatusUpdate(Arrays.asList(object.getUserMessageId()), !value);
        }
    };
    private ValueUpdater<Boolean> checkboxHeader = new ValueUpdater<Boolean>() {
        @Override
        public void update(Boolean value) {
            for (Object row : view.getTable().getVisibleItems()) {
                ((MultiSelectionModel) view.getTable().getSelectionModel()).setSelected(row, value);
            }
        }
    };

    /**************************************************************************/
    /* General Widget events                                                  */
    /**************************************************************************/
    @Override
    public void createPresenter() {
        view.initTable(initTable());
    }

    @Override
    public void bindView() {
        addButtonsHandlers();
        addTableSelectionModelClickHandler();
        eventBus.setFooter(view.getFooterContainer());
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
        eventBus.messagesMenuStyleChange(Constants.MESSAGES_INBOX);
        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getTable());
        //Set visibility
        eventBus.resetSearchBar(new MessagesSearchView());
        searchDataHolder = filter;
        eventBus.createTokenForHistory();

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onDisplayInboxMessages(ArrayList<MessageDetail> inboxMessages) {
        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), inboxMessages);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void addTableSelectionModelClickHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //  display actionBox if needed
                view.getToolbar().getActionBox().setVisible(view.getSelectedUserMessageIds().size() > 0);

                //  display detail
                if (view.getSelectedUserMessageIds().size() == 1) {
                    view.getMessageDetailView().setVisible(true);
                    view.getAdvertisementPanel().setVisible(false);
                    view.getMessageDetailView().setMessageDetail(
                        (MessageDetail) view.getSelectedObjects().iterator().next());
                } else {
                    view.getMessageDetailView().setVisible(false);
                    view.getAdvertisementPanel().setVisible(true);
                }
            }
        });
    }

    private void addButtonsHandlers() {
        view.getToolbar().getReplyBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            }
        });
    }

    private UniversalAsyncGrid initTable() {
        return new UniversalGridFactory.Builder<MessageDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnSender(textFieldUpdater)
            .addColumnSubject(textFieldUpdater)
            .addColumnMessageCreated(textFieldUpdater)
            .addSelectionModel(new MultiSelectionModel(), MessageDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(new SortPair(MessageDetail.MessageField.CREATED)))
            .build();
    }
}
