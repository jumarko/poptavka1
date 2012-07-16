package com.eprovement.poptavka.client.user.demands.tab.tables;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.demands.tab.ClientListPresenter;
import com.eprovement.poptavka.client.user.widget.grid.ColumnFactory;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientProjectDetail;

import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class represents table with asynchronous data provider and sort handler.
 * Class not used yet.
 *
 * TODO: implement lines: 76, 101, 148
 *
 * @author Mato
 */
public class ClientDemandsTable extends DataGrid<ClientProjectDetail> {

    private final static long ID = Storage.getUser().getUserId();
    // Search Data Holder
    private SearchModuleDataHolder searchDataHolder;
    private ClientListPresenter presenter;
    private SingleSelectionModel<ClientProjectDetail> selectionModel;
    private SimplePager pager;
//    private int pageSize;
    // Data Provider
    private AsyncDataProvider dataProvider = null;
    private int start = 0;
    private int count = 0;
    // Sort Provider
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "status", "title", "price", "finnishDate", "validToDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void createAsyncDataProvider(final int resultCount,
            final SearchModuleDataHolder searchDataHolder) {
        this.searchDataHolder = searchDataHolder;
        this.start = 0;
        this.count = 0;
        this.dataProvider = new AsyncDataProvider<ClientProjectDetail>() {

            @Override
            protected void onRangeChanged(HasData<ClientProjectDetail> display) {
                display.setRowCount(resultCount);
                start = display.getVisibleRange().getStart();
                count = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.DESC);
                //uncoment if implemented
//                presenter.requestClientDemands(ID, start, count, searchDataHolder, orderColumns);
            }
        };
        this.dataProvider.addDataDisplay(this);
        this.createAsyncSortHandler();
    }

    private void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(this) {

            @Override
            public void onColumnSort(ColumnSortEvent event) {
                orderColumns.clear();
                OrderType orderType = OrderType.DESC;
                if (event.isSortAscending()) {
                    orderType = OrderType.ASC;
                }
                Column<ClientProjectDetail, String> column = (Column<ClientProjectDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(getColumnIndex(column)), orderType);

                //uncoment if implemented
//                presenter.requestClientDemands(ID, start, count, searchDataHolder, orderColumns);
            }
        };
        this.addColumnSortHandler(sortHandler);
    }

    public ClientDemandsTable(ClientListPresenter presenter, int pageSize) {
        super(ClientProjectDetail.KEY_PROVIDER);
        setPageSize(pageSize);

        this.presenter = presenter;
        initClientDemandTable();
    }

    private void initClientDemandTable() {
        // Add a selection model so we can select cells.
        selectionModel = new SingleSelectionModel<ClientProjectDetail>(ClientProjectDetail.KEY_PROVIDER);
        this.setSelectionModel(selectionModel);
        setEmptyTableWidget(new Label(Storage.MSGS.noData()));

        //init table
        initTableColumns();

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(this);
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    private void initTableColumns() {
        //init column factory
        ColumnFactory<ClientProjectDetail> factory = new ColumnFactory<ClientProjectDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<ClientProjectDetail, String> action = new FieldUpdater<ClientProjectDetail, String>() {

            @Override
            public void update(int index, ClientProjectDetail object,
                    String value) {
                TableDisplay obj = (TableDisplay) object;
                obj.setRead(true);
                redraw();
                //uncoment if implemented
// presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

// **** demand status column
        Column<ClientProjectDetail, DemandStatus> statusColumn = factory.createStatusColumn(null);
        this.addColumn(statusColumn, Storage.MSGS.status());

// **** demand title column
//        Column<ClientProjectDetail, String> titleCol = factory.createTitleColumn(null, true);
//        titleCol.setFieldUpdater(action);
//        this.addColumn(titleCol, Storage.MSGS.title());

// **** demand price column
//        Column<ClientProjectDetail, String> priceCol = factory.createPriceColumn(null);
//        priceCol.setFieldUpdater(action);
//        this.addColumn(priceCol, Storage.MSGS.price());

        //uncoment if implemented
        // **** finishDate column
//        Column<ClientDemandDetail, String> finishCol = factory.createDateColumn(null, ColumnFactory.DATE_FINISHED);
//        finishCol.setFieldUpdater(action);
//        this.addColumn(finishCol, Storage.MSGS.finnishDate());

// **** expireDate column
//        Column<ClientDemandDetail, String> expireCol = factory.createDateColumn(null, ColumnFactory.DATE_VALIDTO);
//        expireCol.setFieldUpdater(action);
//        this.addColumn(expireCol, Storage.MSGS.validTo());
    }

    @Override
    public SingleSelectionModel<ClientProjectDetail> getSelectionModel() {
        return selectionModel;
    }

    public SimplePager getPager() {
        return this.pager;
    }

    public void display(ArrayList<ClientProjectDetail> data) {
        dataProvider.updateRowData(start, data);
        flush();
        redraw();
    }
}
