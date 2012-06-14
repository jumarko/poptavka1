package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represent DataGrid with asynchronous data retrieving.
 *
 * @author Martin
 * @param <T>
 */
public class UniversalAsyncGrid<T> extends DataGrid<T> {

    /**
     * To use this class, eventBus which uses it must extend this interface.
     */
    public interface IEventBusData {

        void getDataCount(SearchModuleDataHolder searchDataHolder);

        void getData(int start, int maxResult,
                SearchModuleDataHolder searchDataHolder, Map<String, OrderType> orderColumns);
    }
    // Data Provider
    private AsyncDataProvider<T> dataProvider = null;
    private int start = 0;
    // Sorting
    private AsyncHandler sortHandler = null;
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    private List<String> gridColumns = null;
    // Others
    private IEventBusData eventBus = null;
    private SearchModuleDataHolder searchDataHolder = null;
    private final SingleSelectionModel<T> selectionModel = new SingleSelectionModel<T>();

    public UniversalAsyncGrid() {
        super();
        setSelectionModel(selectionModel);
    }

    public UniversalAsyncGrid(ProvidesKey<T> keyProvider) {
        super(keyProvider);
        setSelectionModel(selectionModel);
    }

    /**
     * Gets data count.
     * Must be called first.
     *
     * @param eventBus - define eventBus to handle <b>getDataCount</b> and <b>getData</b> methods
     * @param searchDataHolder - define search criteria if any
     * @param gridColumns - define column names of table
     */
    public void getDataCount(IEventBusData eventBus, SearchModuleDataHolder searchDataHolder,
            List<String> gridColumns) {
        this.eventBus = eventBus;
        this.searchDataHolder = searchDataHolder;
        this.gridColumns = gridColumns;

        eventBus.getDataCount(searchDataHolder);
    }

    /**
     * Creates asynchronous data provider for table.
     * Must be called when count of all data is known.
     *
     * @param resultCount - count of all data
     */
    public void createAsyncDataProvider(final int resultCount) {
        this.start = 0;
        this.dataProvider = new AsyncDataProvider<T>() {

            @Override
            protected void onRangeChanged(HasData<T> display) {
                display.setRowCount(resultCount);
                start = display.getVisibleRange().getStart();
                int length = display.getVisibleRange().getLength();

                orderColumns.clear();
                orderColumns.put(gridColumns.get(0), OrderType.DESC);
                eventBus.getData(start, start + length, searchDataHolder, orderColumns);
            }
        };
        this.dataProvider.addDataDisplay(this);
        this.createAsyncSortHandler();

    }

    /**
     * Creates aynchronous handler for sorting.
     */
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
                Column<T, String> column = (Column<T, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                orderColumns.put(gridColumns.get(getColumnIndex(column)), orderType);

                eventBus.getData(start, getPageSize(), searchDataHolder, orderColumns);
            }
        };
        addColumnSortHandler(sortHandler);
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> - the cell type
     */
    public interface GetValue<C> {

        C getValue(Object object);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> - the cell type
     * @param cell - the cell used to render the column
     * @param headerText - the header string
     * @param getter - the value getter for the cell
     */
    public <C> Column<T, C> addColumn(Cell<C> cell,
            String headerText, boolean sort, int width, final GetValue<C> getter) {
        Column<T, C> column = new Column<T, C>(cell) {

            @Override
            public C getValue(T demand) {
                return getter.getValue(demand);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        addColumn(column, headerText);
        setColumnWidth(column, width, Unit.PX);
        return column;
    }

    // ***********************************************************************
    // Getter metods
    // ***********************************************************************
    public AsyncDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    public AsyncHandler getSortHandler() {
        return sortHandler;
    }

    @Override
    public SingleSelectionModel<T> getSelectionModel() {
        return selectionModel;
    }

    public void displayEmptyTable() {
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">"
                + Storage.MSGS.emptyTable() + "</div>"));
    }

    public int getStart() {
        return start;
    }
}
