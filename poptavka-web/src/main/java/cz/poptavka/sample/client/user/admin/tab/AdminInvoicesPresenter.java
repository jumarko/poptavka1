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
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.client.user.admin.AdminModuleEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.InvoiceDetail;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminInvoicesView.class)
public class AdminInvoicesPresenter
        extends LazyPresenter<AdminInvoicesPresenter.AdminInvoicesInterface, AdminModuleEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("AdminDemandsPresenter");
    private Map<Long, InvoiceDetail> dataToUpdate = new HashMap<Long, InvoiceDetail>();
    private Map<Long, String> metadataToUpdate = new HashMap<Long, String>();
    private Map<Long, InvoiceDetail> originalData = new HashMap<Long, InvoiceDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminInvoicesInterface extends LazyView {

        Widget getWidgetView();

        DataGrid<InvoiceDetail> getDataGrid();

        Column<InvoiceDetail, String> getIdColumn();

        Column<InvoiceDetail, String> getPriceColumn();

        Column<InvoiceDetail, String> getPaymentMethodColumn();

        Column<InvoiceDetail, String> getVariableSymbolColumn();

        Column<InvoiceDetail, String> getInvoiceNumberColumn();

        SingleSelectionModel<InvoiceDetail> getSelectionModel();

        SimplePanel getAdminInvoiceDetail();

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
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "id", "invoiceNumber", "variableSymbol", "totalPrice", "paymentMethod"
    };
    private int start = 0;
    private List<String> gridColumns = Arrays.asList(columnNames);
    private SearchModuleDataHolder searchDataHolder; //need to remember for asynchDataProvider if asking for more data

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

    public void onInitInvoices(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView("adminInvoices");
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminInvoicesCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    public void onDisplayAdminTabInvoices(List<InvoiceDetail> invoices) {
        dataProvider.updateRowData(start, invoices);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    public void onResponseAdminInvoiceDetail(Widget widget) {
        view.getAdminInvoiceDetail().setWidget(widget);
    }

    @Override
    public void bindView() {
        setIdColumnUpdater();
        setInvoiceNumberColumnUpdater();
        setVariableSymbolColumnUpdater();
        setPriceColumnUpdater();
        setPaymentMethodColumnUpdater();
        addChangeUpdater();
        addPageChangeHandler();
        addCommitButtonHandler();
        addRollbackButtonHandler();
        addRefreshButtonHandler();
    }

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

    private void addChangeUpdater() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getId())) {
//                    eventBus.showAdminDemandDetail(dataToUpdate.get(
//                            view.getSelectionModel().getSelectedObject().getId()));
//                } else {
//                    eventBus.showAdminDemandDetail(view.getSelectionModel().getSelectedObject());
//                }
//                eventBus.setDetailDisplayedDemand(true);
            }
        });
    }

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

    private void setPriceColumnUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                if (!object.getTotalPrice().toString().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new InvoiceDetail(object));
                    }
                    object.setTotalPrice(BigDecimal.valueOf(Long.valueOf(value)));
                    eventBus.addInvoiceToCommit(object);
                }
            }
        });
    }

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

    private void setIdColumnUpdater() {
        view.getIdColumn().setFieldUpdater(new FieldUpdater<InvoiceDetail, String>() {

            @Override
            public void update(int index, InvoiceDetail object, String value) {
                eventBus.showAdminInvoicesDetail(object);
            }
        });
    }

    private Boolean detailDisplayed = false;

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
//            eventBus.showAdminDemandDetail(data);
        }
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }

    public void onSetDetailDisplayedInvoices(Boolean displayed) {
        detailDisplayed = displayed;
    }
}
