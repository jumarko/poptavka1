/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
import cz.poptavka.sample.client.user.admin.AdminModuleEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminSuppliersView.class)
public class AdminSuppliersPresenter
        extends LazyPresenter<AdminSuppliersPresenter.AdminSuppliersInterface, AdminModuleEventBus> {

    //history of changes
    private Map<Long, FullSupplierDetail> dataToUpdate = new HashMap<Long, FullSupplierDetail>();
    private Map<Long, FullSupplierDetail> originalData = new HashMap<Long, FullSupplierDetail>();
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder;
    //for asynch data retrieving
    private AsyncDataProvider dataProvider = null;
    private int start = 0;
    //for asynch data sorting
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    private final String[] columnNames = new String[]{
        "id", "businessUser.businessUserData.companyName", "businessUser.businessType.businessType",
        "certified", "verification"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);
    //detail related
    private Boolean detailDisplayed = false;

    /**
     * Interface for widget AdminSuppliersView.
     */
    public interface AdminSuppliersInterface extends LazyView {

        // TABLE
        DataGrid<FullSupplierDetail> getDataGrid();

        Column<FullSupplierDetail, String> getSupplierIdColumn();

        Column<FullSupplierDetail, String> getSupplierNameColumn();

        Column<FullSupplierDetail, String> getSupplierTypeColumn();

        Column<FullSupplierDetail, Boolean> getCertifiedColumn();

        Column<FullSupplierDetail, String> getVerificationColumn();

        SingleSelectionModel<FullSupplierDetail> getSelectionModel();

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
        SimplePanel getAdminSupplierDetail();

        Widget getWidgetView();
    }

    /*** INIT ***
     *
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitSuppliers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_SUPPLIERS);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminSuppliersCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    /*** DISPLAY ***
     *
     * Displays retrieved data.
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabSuppliers(List<FullSupplierDetail> suppliers) {
        if (suppliers == null) {
            GWT.log("suppliers are null");
        }
        dataProvider.updateRowData(start, suppliers);
        Storage.hideLoading();
    }

    /*** DATA PROVIDER ***
     *
     * Creates asynchronous data provider for datagrid. Also sets sorting on ID column.
     * @param totalFound - count of all data in DB displayed in pager
     */
    public void onCreateAdminSuppliersAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[0], OrderType.ASC);
        dataProvider = new AsyncDataProvider<FullSupplierDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullSupplierDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminSuppliers(start, start + length, searchDataHolder, orderColumns);

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
        sortHandler = new AsyncHandler(view.getDataGrid()) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;

                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<FullSupplierDetail, String> column = (Column<FullSupplierDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getAdminSuppliers(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    /*** DATA CHANGE ***
     *
     * Store changes made in table data.
     */
    public void onAddSupplierToCommit(FullSupplierDetail data) {
        dataToUpdate.remove(data.getSupplierId());
        dataToUpdate.put(data.getSupplierId(), data);
        if (detailDisplayed) {
            eventBus.showAdminSupplierDetail(data);
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onSetDetailDisplayedSupplier(Boolean displayed) {
        detailDisplayed = displayed;
    }

    public void displayDetailContent(FullSupplierDetail detail) {
        eventBus.showAdminSupplierDetail(detail);
    }

    public void onResponseAdminSupplierDetail(Widget widget) {
        view.getAdminSupplierDetail().setWidget(widget);
    }

    /*** ACTION HANDLERS ***
     *
     * Register handlers for widget actions.
     */
    @Override
    public void bindView() {
        addPageChangeHandler();
        //
        setSupplierIdColumnUpdater();
        setNameColumnUpdater();
        setTypeColumnUpdater();
        setCertifiedColumnUpdater();
        setVerificationColumnUpdater();
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
     * COLUMN UPDATER - VERIFICATION
     */
    private void setVerificationColumnUpdater() {
        view.getVerificationColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                for (Verification verification : Verification.values()) {
                    if (!verification.name().equalsIgnoreCase(object.getVerification())) {
                        if (!originalData.containsKey(object.getSupplierId())) {
                            originalData.put(object.getSupplierId(), new FullSupplierDetail(object));
                        }
                        object.setVerification(value);
                        eventBus.addSupplierToCommit(object);
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - CERTIFIED
     */
    private void setCertifiedColumnUpdater() {
        view.getCertifiedColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, Boolean>() {

            @Override
            public void update(int index, FullSupplierDetail object, Boolean value) {
                if (object.isCertified() != value) {
                    if (!originalData.containsKey(object.getSupplierId())) {
                        originalData.put(object.getSupplierId(), new FullSupplierDetail(object));
                    }
                    object.setCertified(value);
                    eventBus.addSupplierToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - TYPE
     */
    private void setTypeColumnUpdater() {
        view.getSupplierTypeColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                for (BusinessType businessType : BusinessType.values()) {
                    if (businessType.getValue().equals(value)) {
                        if (!object.getBusinessType().equals(businessType.name())) {
                            if (!originalData.containsKey(object.getSupplierId())) {
                                originalData.put(object.getSupplierId(), new FullSupplierDetail(object));
                            }
                            object.setBusinessType(value);
                            eventBus.addSupplierToCommit(object);
                        }
                    }
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - NAME
     */
    private void setNameColumnUpdater() {
        view.getSupplierNameColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                if (!object.getCompanyName().equals(value)) {
                    if (!originalData.containsKey(object.getSupplierId())) {
                        originalData.put(object.getSupplierId(), new FullSupplierDetail(object));
                    }
                }
                object.setCompanyName(value);
                eventBus.addSupplierToCommit(object);
            }
        });
    }

    /*
     * COLUMN UPDATER - SUPPLIER ID
     */
    private void setSupplierIdColumnUpdater() {
        view.getSupplierIdColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                eventBus.showAdminSupplierDetail(object);
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
                    Storage.showLoading(Storage.MSGS.commit());
                    for (Long idx : dataToUpdate.keySet()) {
                        eventBus.updateSupplier(dataToUpdate.get(idx));
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
     * ROLBACK
     */
    private void addRollbackButtonHandler() {
        view.getRollbackBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                dataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (FullSupplierDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeSupplier(data);
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
                    eventBus.getAdminDemandsCount(searchDataHolder);
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }
}