/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.main.Constants;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.admin.AdminEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ClientDemandType;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ivan.vlcek, edited by Martin Slavkovsky
 */
@Presenter(view = AdminDemandsView.class)
public class AdminDemandsPresenter
        extends LazyPresenter<AdminDemandsPresenter.AdminDemandsInterface, AdminEventBus> {

    //history of changes
    private Map<Long, FullDemandDetail> dataToUpdate = new HashMap<Long, FullDemandDetail>();
    private Map<Long, FullDemandDetail> originalData = new HashMap<Long, FullDemandDetail>();
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
        "id", "client.id", "title", "type", "status", "validTo", "endDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);
    //detail related
    private Boolean detailDisplayed = false;

    /**
     * Interface for widget AdminMessagesView.
     */
    public interface AdminDemandsInterface extends LazyView {

        //TABLE
        DataGrid<FullDemandDetail> getDataGrid();

        Column<FullDemandDetail, String> getIdColumn();

        Column<FullDemandDetail, String> getCidColumn();

        Column<FullDemandDetail, String> getDemandTitleColumn();

        Column<FullDemandDetail, String> getDemandTypeColumn();

        Column<FullDemandDetail, String> getDemandStatusColumn();

        Column<FullDemandDetail, Date> getDemandExpirationColumn();

        Column<FullDemandDetail, Date> getDemandEndColumn();

        SingleSelectionModel<FullDemandDetail> getSelectionModel();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTONS
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // DETAIL
        SimplePanel getAdminDemandDetail();

        Widget getWidgetView();
    }

    /*** INIT ***
     *
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_DEMANDS);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminDemandsCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    /*** DISPLAY ***
     *
     * Displays retrieved data.
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabDemands(List<FullDemandDetail> demands) {
        dataProvider.updateRowData(start, demands);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    /*** DATA PROVIDER ***
     *
     * Creates asynchronous data provider for datagrid. Also sets sorting on ID column.
     * @param totalFound - count of all data in DB displayed in pager
     */
    public void onCreateAdminDemandsAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[2], OrderType.ASC);
        dataProvider = new AsyncDataProvider<FullDemandDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullDemandDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                //TODO Martin - start+length - ak to ma byt count, ako pozaduje metoda, tak je to zle
                eventBus.getAdminDemands(start, start + length, searchDataHolder, orderColumns);
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
                Column<FullDemandDetail, String> column = (Column<FullDemandDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getAdminDemands(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    /*** DATA CHANGE ***
     *
     * Store changes made in table data.
     */
    public void onAddDemandToCommit(FullDemandDetail data) {
        dataToUpdate.remove(data.getDemandId());
        dataToUpdate.put(data.getDemandId(), data);
        if (detailDisplayed) {
            eventBus.showAdminDemandDetail(data);
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onResponseAdminDemandDetail(Widget widget) {
        view.getAdminDemandDetail().setWidget(widget);
    }

    public void onSetDetailDisplayedDemand(Boolean displayed) {
        detailDisplayed = displayed;
    }

    /*** ACTION HANDLERS ***
     *
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setIdColumnUpdater();
        setCidColumnUpdater();
        setDemandTitleColumnUpdater();
        setDemandTypeColumnUpdater();
        setDemandStatusColumnUpdater();
        setDemandExpirationColumnUpdater();
        setDemandEndColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

    /**
     * TABLE PAGE CHANGER.
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

    /**
     * COLUMN UPDATER - END COLUMN.
     */
    private void setDemandEndColumnUpdater() {
        view.getDemandEndColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getEndDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setEndDate(value);
                    eventBus.addDemandToCommit(object);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - EXPIRATION.
     */
    private void setDemandExpirationColumnUpdater() {
        view.getDemandExpirationColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, Date>() {

            @Override
            public void update(int index, FullDemandDetail object, Date value) {
                if (!object.getValidToDate().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setValidToDate(value);
                    eventBus.addDemandToCommit(object);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - STATUS.
     */
    private void setDemandStatusColumnUpdater() {
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
                            eventBus.addDemandToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TYPE.
     */
    private void setDemandTypeColumnUpdater() {
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
                            eventBus.addDemandToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - TITLE.
     */
    private void setDemandTitleColumnUpdater() {
        view.getDemandTitleColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                if (!object.getTitle().equals(value)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getDemandId(), new FullDemandDetail(object));
                    }
                    object.setTitle(value);
                    eventBus.addDemandToCommit(object);
                }
            }
        });
    }

    /**
     * COLUMN UPDATER - CID.
     */
    private void setCidColumnUpdater() {
        view.getCidColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                eventBus.showAdminDemandDetail(object);
            }
        });
    }

    /**
     * COLUMN UPDATER - ID.
     */
    private void setIdColumnUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<FullDemandDetail, String>() {

            @Override
            public void update(int index, FullDemandDetail object, String value) {
                eventBus.showAdminDemandDetail(object);
            }
        });
    }

    /**
     * COMMIT.
     */
    private void addCommitButtonHandler() {
        view.getCommitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (Window.confirm("Realy commit changes?")) {
                    view.getDataGrid().setFocus(true);
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateDemand(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    originalData.clear();
                    Window.alert("Changes commited");
                }
            }
        });
    }

    /**
     * ROLLBACK.
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (FullDemandDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeDemand(data);
                }
                view.getDataGrid().flush();
                view.getDataGrid().redraw();
                Window.alert(view.getChangesLabel().getText() + " changes rolledback.");
                view.getChangesLabel().setText("0");
                originalData.clear();
            }
        });
    }

    /**
     * REFRESH.
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
                    eventBus.getAdminDemandsCount(searchDataHolder);
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}
