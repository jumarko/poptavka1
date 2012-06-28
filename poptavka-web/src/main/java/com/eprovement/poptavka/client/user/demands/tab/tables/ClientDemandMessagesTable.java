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
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.demands.tab.ClientListPresenter;
import com.eprovement.poptavka.client.user.widget.grid.ColumnFactory;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.domain.enums.DemandStatus;

import com.eprovement.poptavka.shared.domain.message.MessageDetail;
//import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class represents table with asynchronous data provider and sort handler.
 * Class not used yet.
 * @author Mato
 */
public class ClientDemandMessagesTable extends DataGrid<MessageDetail> {

    // Search Data Holder
    private SearchModuleDataHolder searchDataHolder;
    private long demandId;
    private ClientListPresenter presenter;
    private MultiSelectionModel<MessageDetail> selectionModel;
    private SimplePager pager;
//    private int pageSize;
    // Data Provider
    private AsyncDataProvider dataProvider = null;
    private int start = 0;
    // Sort Provider
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    //list of grid columns, used to sort them. First must by blank (checkbox in table)
    private final String[] columnNames = new String[]{
        "status", "title", "price", "finnishDate", "validToDate"
    };
    private List<String> gridColumns = Arrays.asList(columnNames);

    public void createAsyncDataProvider(final long demandId, final int resultCount,
            final SearchModuleDataHolder searchDataHolder) {
        this.searchDataHolder = searchDataHolder;
        this.demandId = demandId;
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<MessageDetail>() {

            @Override
            protected void onRangeChanged(HasData<MessageDetail> display) {
                display.setRowCount(resultCount);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.DESC);
                //Uncoment if implemented
//                presenter.requestClientDemandMessages(Storage.getUser().getUserId(), demandId,
//                        start, start + length, searchDataHolder, orderColumns);

//                eventBus.loadingHide();
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
                Column<MessageDetail, String> column = (Column<MessageDetail, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(getColumnIndex(column)), orderType);

                //view.getPageSize()
//                presenter.requestClientDemandMessages(Storage.getUser().getUserId(), demandId,
//                        start, getPageSize(), searchDataHolder, orderColumns);
            }
        };
        this.addColumnSortHandler(sortHandler);
    }

    public ClientDemandMessagesTable(ClientListPresenter presenter, int pageSize) {
        //Matin - if KEY_PROVIDER to MessageDetail implemented, uncoment following code
//        super(MessageDetail.KEY_PROVIDER);
        setPageSize(pageSize);

        this.presenter = presenter;
        this.initClientDemandTable();
    }

    private void initClientDemandTable() {
        // Add a selection model so we can select cells.
        //Matin - if KEY_PROVIDER to MessageDetail implemented, uncoment following code
//        selectionModel = new MultiSelectionModel<MessageDetail>(MessageDetail.KEY_PROVIDER);
        this.setSelectionModel(selectionModel);
        setEmptyTableWidget(new Label("No data available."));

        //init table
        initTableColumns(selectionModel);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(this);
    }

    /**
     * Create all columns to the grid and define click actions.
     */
    private void initTableColumns(final SelectionModel<MessageDetail> selectionModel) {
        //init column factory
        ColumnFactory<MessageDetail> factory = new ColumnFactory<MessageDetail>();

// **** definition of all needed FieldUpdaters
        //TEXT FIELD UPDATER create common demand display fieldUpdater for demand and related conversation display
        FieldUpdater<MessageDetail, String> action = new FieldUpdater<MessageDetail, String>() {

            @Override
            public void update(int index, MessageDetail object,
                    String value) {
//                TableDisplay obj = (TableDisplay) object;
                object.setRead(true);
                redraw();
                //Martin get demandID not provided by detail objet
// presenter.displayDetailContent(object.getDemandId(), object.getMessageId(), object.getUserMessageId());
            }
        };

// **** demand status column
        Column<MessageDetail, DemandStatus> statusColumn = factory.createStatusColumn(null);
        this.addColumn(statusColumn, Storage.MSGS.status());

// **** demand title column
        Column<MessageDetail, String> titleCol = factory.createTitleColumn(null, true);
        titleCol.setFieldUpdater(action);
        this.addColumn(titleCol, Storage.MSGS.title());

// **** demand price column
        Column<MessageDetail, String> priceCol = factory.createPriceColumn(null);
        priceCol.setFieldUpdater(action);
        this.addColumn(priceCol, Storage.MSGS.price());

// **** finishDate column
        //TODO Martin - implement DATE_FINNISHED to ColumnFactory
//        Column<MessageDetail, String> finishCol = factory.createDateColumn(null, ColumnFactory.DATE_FINISHED);
//        finishCol.setFieldUpdater(action);
//        this.addColumn(finishCol, Storage.MSGS.finnishDate());

// **** expireDate column
        //TODO Martin - implement DATE_VALIDTO to ColumnFactory
//        Column<MessageDetail, String> expireCol = factory.createDateColumn(null, ColumnFactory.DATE_VALIDTO);
//        expireCol.setFieldUpdater(action);
//        this.addColumn(expireCol, Storage.MSGS.validTo());
    }

    public List<Long> getSelectedIdList() {
        List<Long> idList = new ArrayList<Long>();
        Set<MessageDetail> set = getSelectedMessageList();
        Iterator<MessageDetail> it = set.iterator();
        while (it.hasNext()) {
            //Martin get demandID not provided by detail objet
//            idList.add(it.next().getDemandId());
        }
        return idList;
    }

    @SuppressWarnings("unchecked")
    public Set<MessageDetail> getSelectedMessageList() {
        MultiSelectionModel<MessageDetail> model = (MultiSelectionModel<MessageDetail>) getSelectionModel();
        return model.getSelectedSet();
    }

    public SimplePager getPager() {
        return this.pager;
    }

    public void display(ArrayList<MessageDetail> data) {
        dataProvider.updateRowData(start, data);
        flush();
        redraw();
    }
}