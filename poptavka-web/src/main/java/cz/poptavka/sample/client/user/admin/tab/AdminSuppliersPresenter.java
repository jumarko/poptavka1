/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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
import com.google.gwt.user.client.ui.Button;
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
//import cz.poptavka.sample.shared.domain.demand.DemandDetail;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminSuppliersView.class)//, multiple=true)
public class AdminSuppliersPresenter
        //      extends LazyPresenter<AdminSuppliersPresenter.AdminSuppliersInterface, UserEventBus>
        extends BasePresenter<AdminSuppliersPresenter.AdminSuppliersInterface, UserEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("    AdminSuppliersPresenter");
    private Map<Long, FullSupplierDetail> dataToUpdate = new HashMap<Long, FullSupplierDetail>();
    private Map<Long, String> metadataToUpdate = new HashMap<Long, String>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminSuppliersInterface { //extends LazyView {

        Widget getWidgetView();

        DataGrid<FullSupplierDetail> getDataGrid();

        Column<FullSupplierDetail, String> getSupplierNameColumn();

        Column<FullSupplierDetail, String> getSupplierTypeColumn();

        Column<FullSupplierDetail, Boolean> getCertifiedColumn();

        Column<FullSupplierDetail, String> getVerificationColumn();

        SingleSelectionModel<FullSupplierDetail> getSelectionModel();

        SimplePanel getAdminSupplierDetail();

        SimplePager getPager();

        int getPageSize();

        Button getCommitBtn();

        ListBox getPageSizeCombo();
    }
//    private ArrayList<Demand> demands = new ArrayList<Demand>();

    public void onInvokeAdminSuppliers() {
        eventBus.getAdminTabSuppliersCount();
        eventBus.displayAdminContent(view.getWidgetView());
    }

    public void onDisplayAdminTabSuppliers(List<FullSupplierDetail> suppliers) {
        LOGGER.info("list: " + suppliers.size());
        dataProvider.updateRowData(start, suppliers);
    }
    private AsyncDataProvider dataProvider = null;
    private int start = 0;

    public void onCreateAdminSuppliersAsyncDataProvider(final int totalFound) {
        this.start = 0;
        dataProvider = new AsyncDataProvider<FullSupplierDetail>() {

            @Override
            protected void onRangeChanged(HasData<FullSupplierDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminSuppliers(start, start + length);
                eventBus.loadingHide();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }
    private AsyncHandler sortHandler = null;

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
                Column<FullSupplierDetail, String> column = (Column<FullSupplierDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                int idx = view.getDataGrid().getColumnIndex(column);

                LOGGER.info("Column IDX: " + idx + " orderType: " + orderType);
//                LOGGER.info("Column NAME: " + FullSupplierDetail.supplierField[idx - 1]);
                orderColumns.put("companyName", orderType);

                eventBus.getSortedSuppliers(start, view.getPageSize(), orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onResponseAdminSupplierDetail(Widget widget) {
        view.getAdminSupplierDetail().setWidget(widget);
    }

    @Override
    public void bind() {
//    public void bindView() {
        view.getSupplierNameColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                object.setCompanyName(value);
                eventBus.addSuppliersToCommit(object, "userdata");
            }
        });
        view.getSupplierTypeColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                object.setBusinessType(value);
                eventBus.addSuppliersToCommit(object, "userdata");
//                refreshDisplays();
            }
        });
        view.getCertifiedColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, Boolean>() {

            @Override
            public void update(int index, FullSupplierDetail object, Boolean value) {
                object.setCertified(value);
                eventBus.addSuppliersToCommit(object, "supplier");
            }
        });
        view.getVerificationColumn().setFieldUpdater(new FieldUpdater<FullSupplierDetail, String>() {

            @Override
            public void update(int index, FullSupplierDetail object, String value) {
                object.setVerification(value);
                eventBus.addSuppliersToCommit(object, "supplier");
            }
        });
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getSupplierId())) {
                    eventBus.showAdminSupplierDetail(dataToUpdate.get(
                            view.getSelectionModel().getSelectedObject().getSupplierId()));
                } else {
                    eventBus.showAdminSupplierDetail(view.getSelectionModel().getSelectedObject());
                }

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
                eventBus.loadingShow("Commiting");
                for (Long idx : dataToUpdate.keySet()) {
                    eventBus.updateSupplier(dataToUpdate.get(idx), metadataToUpdate.get(idx));
                }
                eventBus.loadingHide();
                dataToUpdate.clear();
                metadataToUpdate.clear();
            }
        });
    }

    public void onAddSuppliersToCommit(FullSupplierDetail data, String dataType) {
        dataToUpdate.remove(data.getSupplierId());
        metadataToUpdate.remove(data.getSupplierId());
        dataToUpdate.put(data.getSupplierId(), data);
        metadataToUpdate.put(data.getSupplierId(), dataType);
    }
}
