package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.Range;
import java.util.List;

/**
 * Represent DataGrid with asynchronous data retrieving.
 *
 * To use this class, eventBus of a witget that uses this class, must extend
 * interface IEventBusData. Therefore getDataCount and getData methods must be
 * implemented in module, which uses this class.
 * Then to get data call "view.getDataGrid().getDataCount(eventBus, searchDataHolder);"
 * in presenter.
 *
 * @author Martin
 * @param <T>
 */
public class UniversalAsyncGrid<T> extends DataGrid<T> {

    /**************************************************************************/
    /*                              INTERFACES                                */
    /**************************************************************************/
    /**
     * Provides interfaces for methods retrieving count and data. Each eventBus
     * of module using this class must extend this interface.
     */
    public interface IEventBusData {

        /**
         * Counts data that satisfy filtering criteria represented by given
         * SearchModuleDataHolder.
         *
         * @param grid
         * @param searchDataHolder - define filtering criteria
         */
        void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

        /**
         * Gets data that satisfy filtering criteria represented by given
         * SearchModuleDataHolder. Supports pagination and ordering.
         *
         * @param start - pagination - start
         * @param maxResult - pagination - page size
         * @param searchDataHolder - define filtering criteria
         * @param orderColumns - define ordering
         */
        void getData(SearchDefinition searchDefinition);
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> - the cell type
     */
    public interface GetValue<C> {

        C getValue(Object object);
    }
    /**************************************************************************/
    /*                          ATTRIBUTES                                    */
    /**************************************************************************/
    private static final int TABLE_PADDING_HEIGHT = 20;
    private static final int ROW_HEIGHT = 42;
    /**
     * Asynchronous Data Provider. When all data count is known, asynchronous
     * data provider is created {@link #createAsyncDataProvider(final int resultCount)}.
     * Asks for new data when user choose different page.
     */
    private AsyncDataProvider<T> dataProvider = null;
    private int start = 0;
    private int length = 0;
    /**
     * If new category is selected, pager.setPage(0) must be called to reset table and pager.
     * But it fires RangeChangeEvent on dataset we don't event want to have displayed anymore.
     * Because right after that is called getData on newly selected category to retrieve new dataset.
     * Therefore in some cases is suitable to cancel rangeChangeEvent to retrieve data.
     */
    private boolean cancelRangeChangedEvent = false;
    /**
     * Asynchronous sorting handler. Provides handler for sorting data where
     * asynchronous data provider is used. When asynchronous data provider is
     * created, creates asynchronous sorting handler too. See {@link #createAsyncSortHandler()}.
     */
    private AsyncHandler sortHandler = null;
    /**
     * Holds information about sorting.
     */
    private SortDataHolder sort;
    /**
     * Stores eventBus of module that uses this class. Stored when
     * (@link getDataCount(IEventBusData eventBus, SearchModuleDataHolder searchDataHolder)
     * method is called.
     */
    private IEventBusData eventBus = null;
    /**
     * Holds filtering criteria. See class (@link SearchModuleDataHolder).
     */
    private SearchModuleDataHolder searchDataHolder = null;

    /**************************************************************************/
    /* Constructors of UniversalAsyncGrid                                     */
    /**************************************************************************/
    public UniversalAsyncGrid(int pageSize, Resources resources) {
        super(pageSize, resources);
        universalAsyncGridCommonSettings();
    }

    public UniversalAsyncGrid(SortDataHolder sort, int pageSize, Resources resources) {
        super(pageSize, resources);
        this.sort = sort;
        universalAsyncGridCommonSettings();
    }

    public UniversalAsyncGrid(ProvidesKey<T> keyProvider, SortDataHolder sort) {
        super(keyProvider);
        this.sort = sort;
        universalAsyncGridCommonSettings();
    }

    /**
     * Common method called in all constructors. It sets the no result Label.
     */
    public void universalAsyncGridCommonSettings() {
        Label noResultsLabel = new Label(Storage.MSGS.commonNoData());
        noResultsLabel.addStyleName("no-results-label");
        setEmptyTableWidget(noResultsLabel);
    }

    /**************************************************************************/
    /* Get Data methods                                                       */
    /**************************************************************************/
    /**
     * Gets data count. Whole retrieving proces starts here, therefore must be
     * called to fire it.
     *
     * @param eventBus - define eventBus to handle <b>getDataCount</b> and
     * <b>getData</b> methods
     * @param searchDataHolder - define search criteria if any
     */
    public void getDataCount(IEventBusData eventBus, SearchDefinition searchDefinition) {
        this.eventBus = eventBus;
        //If eventBus is available and dataProvider is not initialized, do it.
        if (dataProvider == null) {
            createAsyncDataProvider();
        }
        if (searchDefinition != null) {
            this.searchDataHolder = searchDefinition.getFilter();
        }
        eventBus.getDataCount(this, searchDefinition);
    }

    /**************************************************************************/
    /* Create methods                                                         */
    /**************************************************************************/
    /**
     * Creates asynchronous data provider for a table. Must be called when count
     * of all data is known. Method onRangeChanged it called when user choose
     * different page.
     *
     * @param resultCount - count of all data
     */
    public void createAsyncDataProvider() {
        this.start = 0;
        this.length = 0;
        //if first called, just create dataProvider, but don't call eventBus.getData
        cancelRangeChangedEvent = true;
        this.dataProvider = new AsyncDataProvider<T>() {
            @Override
            protected void onRangeChanged(HasData<T> display) {
                start = display.getVisibleRange().getStart();
                length = display.getVisibleRange().getLength();
                if (!cancelRangeChangedEvent) {
                    //Aks for new data
                    if (display.getRowCount() > 0) {
                        eventBus.getData(new SearchDefinition(
                            start, start + length, searchDataHolder, sort.getSortOrder()));
                    }
                }
                cancelRangeChangedEvent = false;
            }

            @Override
            public void updateRowCount(int size, boolean exact) {
                if (size > 0) {
                    super.updateRowCount(size, exact);
                    eventBus.getData(new SearchDefinition(
                        start, start + length, searchDataHolder, sort.getSortOrder()));
                } else {
                    super.updateRowCount(size, true);
                }
            }

            @Override
            public void updateRowData(int start, List<T> values) {
                super.updateRowData(start, values);
                setTableHeight(values.size());
            }
        };
        this.dataProvider.addDataDisplay(this);
        this.createAsyncSortHandler();

    }

    /**
     * Creates aynchronous handler for sorting. Its called when asynchronous
     * data provider is created. See (@link createAsyncDataProvider(final int resultCount)).
     * Method onColumnSort it called when user choose different column to sort.
     */
    private void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(this) {
            @Override
            public void onColumnSort(ColumnSortEvent event) {
                Column<T, String> column = (Column<T, String>) event.getColumn();
                if (column == null) {
                    return;
                }
                sort.clear();
                sort.addCustomSortOrder(
                    getColumnIndex(column),
                    event.isSortAscending() ? OrderType.ASC : OrderType.DESC);
                sort.useCustomSortOrder();

                eventBus.getData(new SearchDefinition(
                    start, getPageSize(), searchDataHolder, sort.getSortOrder()));
            }
        };
        addColumnSortHandler(sortHandler);
    }

    /**************************************************************************/
    /* COLUMN DEFINITIONS                                                     */
    /**************************************************************************/
    /**
     * Add a column with a header. When creating table in module using this
     * class, it uses this method to create table columns.
     *
     * @param <C> - the cell type
     * @param cell - the cell used to render the column
     * @param sort - define whether column will be allowed to be sorted
     * @param width - define column width
     * @param headerText - the header string
     * @param getter - the value getter for the cell
     */
    public <C> Column<T, C> addColumn(Cell<C> cell,
        String headerText, boolean sort, String width, final GetValue<C> getter) {
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
        setColumnWidth(column, width);
        return column;
    }

    /**
     * Add given column to table.
     *
     * Comment: Column must be as last attribute becuse there is already
     * one method with <Column, String, String> attributes.
     *
     * @param headerText header text
     * @param width of the column
     * @param column to be added
     * @return added column
     */
    public Column addColumn(String headerText, String widthStyleName, Column column) {
        addColumn(column, headerText);
        addColumnStyleName(getColumnIndex(column), widthStyleName);
        return column;
    }

    public Column addColumn(Header header, String widthStyleName, Column column) {
        addColumn(column, header);
        addColumnStyleName(getColumnIndex(column), widthStyleName);
        return column;
    }

//    Martin - commented 10.11.2013 - archived this methods for future needs
//    public Column<T, ImageResource> addIconColumn(final ImageResource imageResource, String explanationText) {
//        Column<T, ImageResource> col = new Column<T, ImageResource>(new CustomImageCell(explanationText)) {
//            @Override
//            public ImageResource getValue(T object) {
//                return imageResource;
//            }
//        };
//        //set column style
//        col.setCellStyleNames(Storage.GRSCS.dataGridStyle().cellTableIconColumn());
//        addColumn(col);
//        setColumnWidth(col, Constants.COL_WIDTH_ICON);
//        return col;
//    }
    /**************************************************************************/
    /* Setter metods                                                          */
    /**************************************************************************/
    private void setTableHeight(int rowCount) {
        //include table padding
        int height = (2 * TABLE_PADDING_HEIGHT);
        //include one row for a table header
        height += ROW_HEIGHT;
        //include table rows
        height += (rowCount > 0 ? (rowCount * ROW_HEIGHT) : ROW_HEIGHT);
        setHeight(height + "px");
    }

    // ***********************************************************************
    // Getter metods
    // ***********************************************************************
    /**
     * Gets asynchronous data provider provided by UniversalAsyncGrid class.
     *
     * @return data provider
     */
    public AsyncDataProvider<T> getDataProvider() {
        return dataProvider;
    }

    /**
     * Gets asynchronous sorting handler provided by UniversalAsyncGrid class.
     *
     * @return
     */
    public AsyncHandler getSortHandler() {
        return sortHandler;
    }

    /**
     * Gets sort order.
     *
     * @return
     */
    public SortDataHolder getSort() {
        return sort;
    }

    /**
     * Gets start index of pagination.
     *
     * @return start index
     */
    public int getStart() {
        return start;
    }

    public void cancelRangeChangedEvent() {
        cancelRangeChangedEvent = true;
    }

    public void setGridColumns(SortDataHolder sort) {
        this.sort = sort;
    }

    public void refresh() {
        Range range = getVisibleRange();
        setVisibleRangeAndClearData(range, true); //1st way
//        RangeChangeEvent.fire(this, range);              //2nd way
    }
}
