/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.event.dom.client.ClickEvent;
import java.util.List;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
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
import cz.poptavka.sample.client.user.admin.AdminModuleEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author ivan.vlcek
 */
@Presenter(view = AdminSuppliersView.class)//, multiple=true)
public class AdminSuppliersPresenter
        extends LazyPresenter<AdminSuppliersPresenter.AdminSuppliersInterface, AdminModuleEventBus>
        implements HasValueChangeHandlers<String> {

    private final static Logger LOGGER = Logger.getLogger("AdminSuppliersPresenter");
    private Map<Long, FullSupplierDetail> dataToUpdate = new HashMap<Long, FullSupplierDetail>();
    private Map<Long, FullSupplierDetail> originalData = new HashMap<Long, FullSupplierDetail>();

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminSuppliersInterface extends LazyView {

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

        Button getRollbackBtn();

        Button getRefreshBtn();

        Label getChangesLabel();

        ListBox getPageSizeCombo();
    }
//    private ArrayList<Demand> demands = new ArrayList<Demand>();

    public void onInitSuppliers() {
        eventBus.getAdminSuppliersCount();
        eventBus.displayView(view.getWidgetView());
    }

    public void onDisplayAdminTabSuppliers(List<FullSupplierDetail> suppliers) {
        LOGGER.info("list: " + suppliers.size());
        if (suppliers == null) {
            GWT.log("suppliers are null");
        }
        dataProvider.updateRowData(start, suppliers);
        Storage.hideLoading();
    }
    private AsyncDataProvider dataProvider = null;
    private int start = 0;

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
                eventBus.getAdminSuppliers(start, start + length);
//                eventBus.getSortedSuppliers(start, start + length, orderColumns);
                Storage.hideLoading();
            }
        };
        this.dataProvider.addDataDisplay(view.getDataGrid());
        createAsyncSortHandler();
    }
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "id", "businessUser.businessUserData.companyName", "businessUser.businessType.businessType",
        "certified", "verification"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

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

                eventBus.getSortedSuppliers(start, view.getPageSize(), orderColumns);
            }
        };
        view.getDataGrid().addColumnSortHandler(sortHandler);
    }

    public void onResponseAdminSupplierDetail(Widget widget) {
        view.getAdminSupplierDetail().setWidget(widget);
    }

    @Override
    public void bindView() {
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
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getSupplierId())) {
                    eventBus.showAdminSupplierDetail(dataToUpdate.get(
                            view.getSelectionModel().getSelectedObject().getSupplierId()));
                } else {
                    eventBus.showAdminSupplierDetail(view.getSelectionModel().getSelectedObject());
                }
                eventBus.setDetailDisplayedSupplier(true);
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
    private Boolean detailDisplayed = false;

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
}
