/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.message.MessageState;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.type.MessageType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminMessagesView.class)
public class AdminMessagesPresenter
        extends BasePresenter<AdminMessagesPresenter.AdminMessagesInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("    AdminDemandsPresenter");
    private Map<Long, MessageDetail> dataToUpdate = new HashMap<Long, MessageDetail>();
    private Map<Long, MessageDetail> originalData = new HashMap<Long, MessageDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminMessagesInterface {

        Widget getWidgetView();

        DataGrid<MessageDetail> getDataGrid();

        Column<MessageDetail, String> getStateColumn();

        Column<MessageDetail, String> getSubjectColumn();

        Column<MessageDetail, String> getBodyColumn();

        Column<MessageDetail, String> getTypeColumn();

        Column<MessageDetail, Date> getCreatedColumn();

        Column<MessageDetail, Date> getSentColumn();

        SingleSelectionModel<MessageDetail> getSelectionModel();

        SimplePanel getAdminDemandDetail();

        SimplePager getPager();

        int getPageSize();

        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        ListBox getPageSizeCombo();
    }
    private AsyncDataProvider dataProvider = null;
    private AsyncHandler sortHandler = null;
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "", "id", "client", "title", "type", "status", "validTo", "endDate"
    };
    private int start = 0;
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void onCreateAdminDemandsAsyncDataProvider(final int totalFound) {
        this.start = 0;
        dataProvider = new AsyncDataProvider<MessageDetail>() {

            @Override
            protected void onRangeChanged(HasData<MessageDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminDemands(start, start + length);
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }

    public void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                OrderType orderType = OrderType.DESC;
                Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();

                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<MessageDetail, String> column = (Column<MessageDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getSortedDemands(start, view.getPageSize(), orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onInvokeAdminMessages() {
        eventBus.getAdminDemandsCount();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    public void onDisplayAdminTabDemands(List<MessageDetail> demands) {
        dataProvider.updateRowData(start, demands);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    @Override
    public void bind() {
        view.getSubjectColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {
            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getSubject().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSubject(value);
//                    eventBus.addMessageToCommit(object);
                }
            }
        });
        view.getBodyColumn().setFieldUpdater(new FieldUpdater<MessageDetail, String>() {
            @Override
            public void update(int index, MessageDetail object, String value) {
                if (!object.getBody().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setBody(value);
//                    eventBus.addMessageToCommit(object);
                }
            }
        });
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
//                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
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
//                            eventBus.addMessageToCommit(object);
                        }
                    }
                }
            }
        });
        view.getCreatedColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {
            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getCreated().equals(value)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setCreated(value);
//                    eventBus.addMessageToCommit(object);
                }
            }
        });
        view.getSentColumn().setFieldUpdater(new FieldUpdater<MessageDetail, Date>() {
            @Override
            public void update(int index, MessageDetail object, Date value) {
                if (!object.getSent().equals(object)) {
                    if (!originalData.containsKey(object.getMessageId())) {
                        originalData.put(object.getMessageId(), new MessageDetail(object));
                    }
                    object.setSent(value);
//                    eventBus.addMessageToCommit(object);
                }
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
            }
        });
        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                int page = view.getPager().getPageStart() / view.getPageSize();
                view.getPager().setPageStart(page * view.getPageSize());
                view.getPager().setPageSize(view.getPageSize());
            }
        });
        view.getCommitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    eventBus.loadingShow("Commiting");
                    for (Long idx : dataToUpdate.keySet()) {
//                        eventBus.updateMessages(dataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
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
        view.getRefreshBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (dataToUpdate.isEmpty()) {
                    dataProvider.updateRowCount(0, true);
                    dataProvider = null;
                    view.getDataGrid().flush();
                    view.getDataGrid().redraw();
                    eventBus.getAdminDemandsCount();
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    public void onaddMessageToCommit(MessageDetail data, String dataType) {
        dataToUpdate.remove(data.getMessageId());
        dataToUpdate.put(data.getMessageId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }
}
