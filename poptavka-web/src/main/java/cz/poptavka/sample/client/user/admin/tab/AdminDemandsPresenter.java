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
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ClientDemandType;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminDemandsView.class)//, multiple=true)
public class AdminDemandsPresenter
        //      extends LazyPresenter<AdminDemandsPresenter.AdminDemandsInterface, UserEventBus>
        extends BasePresenter<AdminDemandsPresenter.AdminDemandsInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("    AdminDemandsPresenter");
    private Map<Long, FullDemandDetail> dataToUpdate = new HashMap<Long, FullDemandDetail>();
    private Map<Long, String> metadataToUpdate = new HashMap<Long, String>();
    private Map<Long, FullDemandDetail> originalData = new HashMap<Long, FullDemandDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminDemandsInterface { //extends LazyView {

        Widget getWidgetView();

        DataGrid<FullDemandDetail> getDataGrid();

        Column<FullDemandDetail, String> getDemandTitleColumn();

        Column<FullDemandDetail, String> getDemandTypeColumn();

        Column<FullDemandDetail, String> getDemandStatusColumn();

        Column<FullDemandDetail, Date> getDemandExpirationColumn();

        Column<FullDemandDetail, Date> getDemandEndColumn();

        SingleSelectionModel<FullDemandDetail> getSelectionModel();

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
    private int start = 0;

    public void onCreateAdminDemandsAsyncDataProvider(final int totalFound) {
        this.start = 0;
        dataProvider = new AsyncDataProvider<FullDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullDemandDetail> display) {
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
    private AsyncHandler sortHandler = null;
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "", "id", "client", "title", "type", "status", "validTo", "endDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

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
                Column<FullDemandDetail, String> column = (Column<FullDemandDetail, String>) event.getColumn();
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

    public void onInvokeAdminDemands() {
        eventBus.getAdminDemandsCount();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    public void onDisplayAdminTabDemands(List<FullDemandDetail> demands) {
//        dataGridListOriginal.clear();
//        for(FullDemandDetail detail : demands ) {
//         dataGridListOriginal.add(new FullDemandDetail(detail));
//        }
        dataProvider.updateRowData(start, demands);
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    @Override
    public void bind() {
        view.getDemandTitleColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                if (!object.getTitle().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setTitle(value);
                    eventBus.addDemandToCommit(object, "demand", "table");
                }
            }
        });
        view.getDemandTypeColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (ClientDemandType clientDemandType : ClientDemandType.values()) {
                    if (clientDemandType.getValue().equals(value)) {
                        if (!object.getDemandType().equals(clientDemandType.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandType(clientDemandType.name());
                            eventBus.addDemandToCommit(object, "demand", "table");
                        }
                    }
                }
            }
        });
        view.getDemandStatusColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                for (DemandStatusType demandStatusType : DemandStatusType.values()) {
                    if (demandStatusType.getValue().equals(value)) {
                        if (!object.getDemandStatus().equals(demandStatusType.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getDemandId(), new FullDemandDetail(object));
                            }
                            object.setDemandStatus(demandStatusType.name());
                            eventBus.addDemandToCommit(object, "demand", "table");
                        }
                    }
                }
            }
        });
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getValidToDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setValidToDate(value);
                    eventBus.addDemandToCommit(object, "other", "table");
                }
            }
        });
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getEndDate().equals(object)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setEndDate(value);
                    eventBus.addDemandToCommit(object, "other", "table");
                }
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getDemandId())) {
                    eventBus.showAdminDemandDetail(dataToUpdate.get(
                            view.getSelectionModel().getSelectedObject().getDemandId()));
                } else {
                    eventBus.showAdminDemandDetail(view.getSelectionModel().getSelectedObject());
                }
                eventBus.setDetailDisplayed(true);
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
                    eventBus.loadingShow("Commiting");
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateDemand(dataToUpdate.get(idx), metadataToUpdate.get(idx));
                    }
                    eventBus.loadingHide();
                    dataToUpdate.clear();
                    metadataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                metadataToUpdate.clear();
                for (FullDemandDetail data : originalData.values()) {
                    LOGGER.info("data: " + data.toString() + " list: "
                            + view.getDataGrid().getVisibleItems().toString());
                    eventBus.changeGridData(data, view.getDataGrid().getVisibleItems());
                }
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
        view.getRefreshBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataProvider.updateRowCount(0, true);
                dataProvider = null;
                eventBus.getAdminDemandsCount();
            }
        });
    }
    private Boolean detailDisplayed = false;
//    private List<FullDemandDetail> dataGridListOriginal = new ArrayList<FullDemandDetail>();

    public void onAddDemandToCommit(FullDemandDetail data, String dataType, String source) {
//        view.getSelectionModel().setSelected(data, true);

        dataToUpdate.remove(data.getDemandId());
        metadataToUpdate.remove(data.getDemandId());
        dataToUpdate.put(data.getDemandId(), data);
        metadataToUpdate.put(data.getDemandId(), dataType);
        if (detailDisplayed) {
            eventBus.showAdminDemandDetail(data);
        }
        if (source.equals("detail")) {
//            List<FullDemandDetail> list = new ArrayList<FullDemandDetail>(dataGridListOriginal);
//            List<FullDemandDetail> list = new ArrayList<FullDemandDetail>(view.getDataGrid().getVisibleItems());
////            List<FullDemandDetail> list = view.getDataGrid().getVisibleItems();
//            int idx = list.indexOf(data);
//            list.remove(idx);
//            list.add(idx, data);
////            dataProvider.updateRowData(start, list);
//            view.getDataGrid().redraw();
            LOGGER.info("data: " + data.toString() + " list: " + view.getDataGrid().getVisibleItems().toString());
            eventBus.changeGridData(data, view.getDataGrid().getVisibleItems());
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
    }

    public void onChangeGridData(FullDemandDetail data, List<FullDemandDetail> dataList) {
        List<FullDemandDetail> list = new ArrayList<FullDemandDetail>(dataList);
        int idx = list.indexOf(data);
        list.remove(idx);
        list.add(idx, data);
//        eventBus.displayAdminTabDemandsLoop(list);

        dataProvider.updateRowData(start, list);
        view.getDataGrid().redraw();
    }

    public void onSetDetailDisplayed(Boolean displayed) {
        detailDisplayed = displayed;
    }
}
