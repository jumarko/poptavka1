/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.domain.common.OrderType;
import com.eprovement.poptavka.domain.message.MessageState;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.type.MessageType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminMessagesView.class)
public class AdminMessagesPresenter
        extends LazyPresenter<AdminMessagesPresenter.AdminMessagesInterface, AdminEventBus> {

    //history of changes
    private Map<Long, MessageDetail> dataToUpdate = new HashMap<Long, MessageDetail>();
    private Map<Long, MessageDetail> originalData = new HashMap<Long, MessageDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    //for asynch data retrieving
    private AsyncDataProvider dataProvider = null;
    private int start = 0;
    //for asynch data sorting
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "id", "demand.id", "parent.id", "sender.id", "receiver.id", "subject", "messageState", "", "sent", "body"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    /**
     * Interface for widget AdminMessagesView.
     */
    public interface AdminMessagesInterface extends LazyView {

        // TABLE
        DataGrid<MessageDetail> getDataGrid();

        Column<MessageDetail, String> getStateColumn();

        Column<MessageDetail, String> getSubjectColumn();

        Column<MessageDetail, String> getBodyColumn();

        Column<MessageDetail, String> getTypeColumn();

        Column<MessageDetail, Date> getCreatedColumn();

        Column<MessageDetail, Date> getSentColumn();

        SingleSelectionModel<MessageDetail> getSelectionModel();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // WIDGETS
        Widget getWidgetView();
    }

    /*** INIT ***
     *
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitMessages(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_MESSAGES);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminMessagesCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    /*** DISPLAY ***
     *
     * Displays retrieved data.
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabMessages(List<MessageDetail> messages) {
        dataProvider.updateRowData(start, messages);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    /*** DATA PROVIDER ***
     *
     * Creates asynchronous data provider for datagrid. Also sets sorting on ID column.
     * @param totalFound - count of all data in DB displayed in pager
     */
    public void onCreateAdminMessagesAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[0], OrderType.ASC);
        dataProvider = new AsyncDataProvider<MessageDetail>() {

            @Override
            protected void onRangeChanged(HasData<MessageDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminMessages(start, start + length, searchDataHolder, orderColumns);
                Storage.hideLoading();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }

    /*** SORTING HANDLER ***
     *
     * Creates asynchronous sort handler. Handle sorting of data provided by asynchronous data provider.
     */
    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<MessageDetail, String> column = (Column<MessageDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getAdminMessages(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    /*** DATA CHANGE ***
     *
     * Store changes made in table data.
     */
    public void onAddMessageToCommit(MessageDetail data) {
        dataToUpdate.remove(data.getMessageId());
        dataToUpdate.put(data.getMessageId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    /*** ACTION HANDLERS ***
     *
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setSubjectColumnUpdater();
        setBodyColumnUpdater();
        setStateColumnUpdater();
        setTypeColumnUpdater();
        setCreatedColumnUpdater();
        setSentColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    /*
     * TABLE PAGE CHANGER
     */
    private void addPageChangeHandler() {
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
    }

    /*
     * COLUMN UPDATER - SENT
     */
    private void setSentColumnUpdater() {
        view.getSentColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {

            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getSent().equals(object)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSent(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - CREATED
     */
    private void setCreatedColumnUpdater() {
        view.getCreatedColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {

            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getCreated().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setCreated(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - TYPE
     */
    private void setTypeColumnUpdater() {
        view.getTypeColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                for (MessageType msgType : MessageType.values()) {
                    if (msgType.getValue().equals(value)) {
                        if (!object.getMessageType().equals(msgType.name())) {
                            if (!originalData.containsKey(object.getMessageId())) {
                                originalData.put(object.getMessageId(), new MessageDetail(object));
                            }
                            object.setMessageType(msgType.name());
                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - STATE
     */
    private void setStateColumnUpdater() {
        view.getStateColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                for (MessageState msgState : MessageState.values()) {
                    if (msgState.name().equals(value)) {
                        if (!object.getMessageState().equals(msgState.name())) {
                            if (!originalData.containsKey(object.getMessageId())) {
                                originalData.put(object.getMessageId(), new MessageDetail(object));
                            }
                            object.setMessageState(msgState.name());
                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - BODY
     */
    private void setBodyColumnUpdater() {
        view.getBodyColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getBody().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setBody(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - SUBJECT
     */
    private void setSubjectColumnUpdater() {
        view.getSubjectColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getSubject().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSubject(value);
                    eventBus.addMessageToCommit(object);
                }
            }
        });
    }

    /*
     * COMMIT
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateMessage(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    /*
     * ROLLBACK
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (MessageDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeMessage(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
    }

    /*
     * REFRESH
     */
    private void addRefreshButtonHandler() {
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    dataProvider.updateRowCount(0, true);
                    dataProvider = null;
                    view.getDataGrid().flush();
                    view.getDataGrid().redraw();
                    eventBus.getAdminMessagesCount(searchDataHolder);
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
