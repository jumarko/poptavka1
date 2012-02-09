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
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Presenter(view = AdminOffersView.class)
public class AdminOffersPresenter
        extends LazyPresenter<AdminOffersPresenter.AdminOffersInterface, AdminModuleEventBus>
        implements HasValueChangeHandlers<String> {

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public interface AdminOffersInterface extends LazyView {

        Widget getWidgetView();

        DataGrid<OfferDetail> getDataGrid();

        Column<OfferDetail, String> getPriceColumn();

        Column<OfferDetail, String> getOfferStatusColumn();

        Column<OfferDetail, Date> getOfferCreationDateColumn();

        Column<OfferDetail, Date> getOfferFinishDateColumn();

        SingleSelectionModel<OfferDetail> getSelectionModel();

        SimplePanel getAdminSupplierDetail();

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

    public void onCreateAdminOffersAsyncDataProvider(final int totalFound) {
        this.start = 0;
        orderColumns.clear();
        orderColumns.put(columnNames[0], OrderType.ASC);
        dataProvider = new AsyncDataProvider<OfferDetail>() {

            @Override
            protected void onRangeChanged(HasData<OfferDetail> display) {
                display.setRowCount(totalFound);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();
                eventBus.getAdminDemands(start, start + length, searchDataHolder, orderColumns);
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
        "id", "demand.id", "supplier.id", "price", "state", "", "finnishDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);
    private SearchModuleDataHolder searchDataHolder; //need to remember for asynchDataProvider if asking for more data

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
                Column<OfferDetail, String> column = (Column<OfferDetail, String>) event.getColumn();
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

    public void onInitOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView("adminOffers");
        eventBus.clearSearchContent();
        searchDataHolder = filter;
        eventBus.getAdminOffersCount(searchDataHolder);
        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }

    public void onDisplayAdminTabOffers(List<OfferDetail> demands) {
        dataProvider.updateRowData(start, demands);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        Storage.hideLoading();
    }

    @Override
    public void bindView() {
        setPriceColumnUpdater();
        setOfferStatusColumnUpdater();
        setOfferFinishDateColumnUpdater();
        addSelectionChangeHandler();
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
                    eventBus.getAdminOffersCount(searchDataHolder);
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
                view.getDataGrid().setFocus(true);
                int idx = 0;
                for (OfferDetail data : originalData.values()) {
                    idx = view.getDataGrid().getVisibleItems().indexOf(data);
                    view.getDataGrid().getVisibleItem(idx).updateWholeOfferDetail(data);
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
                        eventBus.updateOffer(dataToUpdate.get(idx));
                    }
                    Storage.hideLoading();
                    dataToUpdate.clear();
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

    private void addSelectionChangeHandler() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                if (dataToUpdate.containsKey(view.getSelectionModel().getSelectedObject().getOfferId())) {
//                    eventBus.showAdminDemandDetail(dataToUpdate.get(
//                            view.getSelectionModel().getSelectedObject().getOfferId()));
//                } else {
//                    eventBus.showAdminDemandDetail(view.getSelectionModel().getSelectedObject());
//                }
//                eventBus.setDetailDisplayedDemand(true);
            }
        });
    }

    private void setOfferFinishDateColumnUpdater() {
        view.getOfferFinishDateColumn().setFieldUpdater(new FieldUpdater<OfferDetail, Date>() {

            @Override
            public void update(int index, OfferDetail object, Date value) {
                if (!object.getFinishDate().equals(object)) {
                    if (!originalData.containsKey(object.getDemandId())) {
                        originalData.put(object.getId(), new OfferDetail(object));
                    }
                    object.setFinishDate(value);
                    eventBus.addOfferToCommit(object);
                }
            }
        });
    }

    private void setOfferStatusColumnUpdater() {
        view.getOfferStatusColumn().setFieldUpdater(new FieldUpdater<OfferDetail, String>() {

            @Override
            public void update(int index, OfferDetail object, String value) {
                for (OfferStateType state : OfferStateType.values()) {
                    if (state.getValue().equals(value)) {
                        if (!object.getState().equals(state.name())) {
                            if (!originalData.containsKey(object.getDemandId())) {
                                originalData.put(object.getId(), new OfferDetail(object));
                            }
                            object.setState(state.name());
                            eventBus.addOfferToCommit(object);
                        }
                    }
                }
            }
        });
    }

    private void setPriceColumnUpdater() {
        view.getPriceColumn().setFieldUpdater(new FieldUpdater<OfferDetail, String>() {

            @Override
            public void update(int index, OfferDetail object, String value) {
                if (!object.getPrice().toString().equals(value)) {
                    if (!originalData.containsKey(object.getId())) {
                        originalData.put(object.getId(), new OfferDetail(object));
                    }
                    object.setPrice(BigDecimal.valueOf(Long.valueOf(value)));
                    eventBus.addOfferToCommit(object);
                }
            }
        });
    }

    private Map<Long, OfferDetail> dataToUpdate = new HashMap<Long, OfferDetail>();
    private Map<Long, OfferDetail> originalData = new HashMap<Long, OfferDetail>();

    public void onAddOfferToCommit(OfferDetail data) {
        dataToUpdate.remove(data.getDemandId());
        dataToUpdate.put(data.getDemandId(), data);
        view.getChangesLabel().setText(Integer.toString(dataToUpdate.size()));
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
    }
}
