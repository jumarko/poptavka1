/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
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
import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminInvoicesView.class)
public class AdminInvoicesPresenter
        extends LazyPresenter<AdminInvoicesPresenter.AdminInvoicesInterface, AdminEventBus> {

    //history of changes
    private Map<Long, InvoiceDetail> dataToUpdate = new HashMap<Long, InvoiceDetail>();
    private Map<Long, String> metadataToUpdate = new HashMap<Long, String>();
    private Map<Long, InvoiceDetail> originalData = new HashMap<Long, InvoiceDetail>();
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
        "id", "invoiceNumber", "variableSymbol", "totalPrice", "paymentMethod"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);
    //detail related
    private Boolean detailDisplayed = false;

    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);

    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());

    /**
     * Interface for widget AdminInvoicesView.
     */
    public interface AdminInvoicesInterface extends LazyView {

        // TABLE
        DataGrid<InvoiceDetail> getDataGrid();

        Column<InvoiceDetail, String> getIdColumn();

        Column<InvoiceDetail, String> getPriceColumn();

        Column<InvoiceDetail, String> getPaymentMethodColumn();

        Column<InvoiceDetail, String> getVariableSymbolColumn();

        Column<InvoiceDetail, String> getInvoiceNumberColumn();

        SingleSelectionModel<InvoiceDetail> getSelectionModel();

        // PAGER
        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        // BUTTON
        Button getCommitBtn();

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        // WIDGETS
        AdminInvoiceInfoView getAdminInvoiceDetail();

        Widget getWidgetView();
    }

    /**
     * Interface for widget AdminInvoicesInfoView.
     */
    public interface AdminInvoiceInfoInterface {

        void setInvoiceDetail(InvoiceDetail contact);

        InvoiceDetail getUpdatedInvoiceDetail();

        Button getUpdateBtn();

        Widget getWidgetView();
    }

    /*** INIT ***
     *
     * Initial methods for handling starting.
     * @param filter
     */
    public void onInitInvoices(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_INVOICES);
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminInvoicesCount(searchDataHolder);
        view.getAdminInvoiceDetail().setVisible(false);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    /*** DISPLAY ***
     *
     * Displays retrieved data.
     * @param accessRoles -- list to display
     */
    public void onDisplayAdminTabInvoices(List<InvoiceDetail> invoices) {
        dataProvider.updateRowData(start, invoices);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    /*** DATA PROVIDER ***
     *
     * Creates asynchronous data provider for datagrid. Also sets sorting on ID column.
     * @param totalFound - count of all data in DB displayed in pager
     */
    public void onCreateAdminInvoicesAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[1], OrderType.ASC);
        dataProvider = new AsyncDataProvider<InvoiceDetail>() {

            @Override
            protected void onRangeChanged(HasData<InvoiceDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminInvoices(start, start + length, searchDataHolder, orderColumns);
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
                Column<InvoiceDetail, String> column = (Column<InvoiceDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(
                        view.getDataGrid().getColumnIndex(column)), orderType);

                eventBus.getAdminInvoices(start, view.getPageSize(), searchDataHolder, orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    /*** DATA CHANGE ***
     *
     * Store changes made in table data.
     */
    public void onAddInvoiceToCommit(InvoiceDetail data) {
        //TODO Martin - otestovat, alebo celkom zrusit cistocne auktualizovanie
        if (metadataToUpdate.containsKey(data.getId())) {
            dataToUpdate.remove(data.getId());
            metadataToUpdate.remove(data.getId());
            metadataToUpdate.put(data.getId(), "all");
        } else {
            dataToUpdate.put(data.getId(), data);
//            metadataToUpdate.put(data.getId(), dataType);
        }
        if (detailDisplayed) {
            view.getAdminInvoiceDetail().setInvoiceDetail(data);
        }
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
        setIdColumnUpdater();
        setInvoiceNumberColumnUpdater();
        setVariableSymbolColumnUpdater();
        setPriceColumnUpdater();
        setPaymentMethodColumnUpdater();
        //
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
        addDetailUpdateButtonHandler();
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
     * COLUMN UPDATER - ID
     */
    private void setIdColumnUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                view.getAdminInvoiceDetail().setVisible(true);
                detailDisplayed = true;
            }
        });
    }

    /*
     * COLUMN UPDATER - PAYMENT METHOD
     */
    private void setPaymentMethodColumnUpdater() {
        view.getPaymentMethodColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                if (!object.getPaymentMethod().getName().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new InvoiceDetail(object));
                    }
//                    object.setPaymentMethod(value);
                    eventBus.addInvoiceToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - PRICE
     */
    private void setPriceColumnUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                if (!object.getTotalPrice().toString().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new InvoiceDetail(object));
                    }
                    object.setTotalPrice(BigDecimal.valueOf(currencyFormat.parse(value)));
                    eventBus.addInvoiceToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - VARIABLE SYMBOL
     */
    private void setVariableSymbolColumnUpdater() {
        view.getVariableSymbolColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                if (!object.getVariableSymbol().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new InvoiceDetail(object));
                    }
                    object.setVariableSymbol(value);
                    eventBus.addInvoiceToCommit(object);
                }
            }
        });
    }

    /*
     * COLUMN UPDATER - INVOICE
     */
    private void setInvoiceNumberColumnUpdater() {
        view.getInvoiceNumberColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                if (!object.getInvoiceNumber().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new InvoiceDetail(object));
                    }
                    object.setInvoiceNumber(value);
                    eventBus.addInvoiceToCommit(object);
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
                        eventBus.updateInvoice(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
                    metadataToUpdate.clear();
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
                metadataToUpdate.clear();
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (InvoiceDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeInvoice(data);
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
                    eventBus.getAdminInvoicesCount(searchDataHolder);
                } else {
                    Window.alert("You have some uncommited data. Do commit or rollback first");
                }
            }
        });
    }

    /*
     * UPDATE
     */
    private void addDetailUpdateButtonHandler() {
        view.getAdminInvoiceDetail().getUpdateBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
//                eventBus.addInvoiceToCommit(view.getUpdatedInvoiceDetail(), "all");
                Window.alert("Demand updated, NOT IMPLENETED");
            }
        });
    }
}
