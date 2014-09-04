/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import java.util.List;

/**
 * Represent DataGrid with asynchronous data retrieving.
 * Class implements commono functionality for asynchronous data retrieving,
 * asynchronous sort data retrieving and some additional helpfull methods.
 *
 * To use this class, eventBus of a witget that uses this class, must extend
 * interface IEventBusData. Therefore getDataCount and getData methods must be
 * implemented in module, which uses this class.
 * Then to get data call "view.getDataGrid().getDataCount(eventBus, searchDataHolder);"
 * in presenter.
 *
 * @author Martin Slavkovsky
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
         * @param searchDefinition define filtering criteria
         */
        void getDataCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition);

        /**
         * Gets data that satisfy filtering criteria represented by given
         * SearchModuleDataHolder. Supports pagination and ordering.
         *
         * @param grid
         * @param searchDefinition define filtering criteria
         * @param requestId
         */
        void getData(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId);
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
    /**
     * Asynchronous Data Provider. When all data count is known, asynchronous
     * data provider is created {@link #createAsyncDataProvider(final int resultCount)}.
     * Asks for new data when user choose different page.
     */
    private AsyncDataProvider<T> dataProvider = null;
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
    private int requestId = 0;

    /**************************************************************************/
    /* Constructors of UniversalAsyncGrid                                     */
    /**************************************************************************/
    /**
     * Constructs a table with the given page size with the specified Resources.
     * @param pageSize - the page size
     * @param resources - the resources to use for this widget
     */
    public UniversalAsyncGrid(int pageSize, Resources resources) {
        super(pageSize, resources);
        universalAsyncGridCommonSettings();
    }

    /**
     * Constructs a table with the given sort definition, page size with the specified Resources.
     * @param pageSize - the page size
     * @param resources - the resources to use for this widget
     */
    public UniversalAsyncGrid(SortDataHolder sort, int pageSize, Resources resources) {
        super(pageSize, resources);
        this.sort = sort;
        universalAsyncGridCommonSettings();
    }

    /**
     * Common method called in all constructors. It sets the no result Label.
     */
    public final void universalAsyncGridCommonSettings() {
        //Create empty table widget
        Label noResultsLabel = new Label(Storage.MSGS.commonNoData());
        noResultsLabel.addStyleName("no-results-label");
        setEmptyTableWidget(noResultsLabel);
        //Create async data provider
        createAsyncDataProvider();
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
     * @param searchDefinition define filtering criteria
     */
    public void getDataCount(IEventBusData eventBus, SearchDefinition searchDefinition) {
        this.eventBus = eventBus;
        showLoading();
        if (searchDefinition != null) {
            this.searchDataHolder = searchDefinition.getFilter();
            searchDefinition.setFirstResult(getPageStart());
            searchDefinition.setMaxResult(getPageSize());
        }
        eventBus.getDataCount(getGrid(), searchDefinition);
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
        //if first called, just create dataProvider, but don't call eventBus.getData
        cancelRangeChangedEvent = true;
        this.dataProvider = new AsyncDataProvider<T>() {
            /**
             * On range change, request for next/previous page data.
             */
            @Override
            protected void onRangeChanged(HasData<T> display) {
                resize(Document.get().getClientWidth());
                if (!cancelRangeChangedEvent) {
                    //Aks for new data
                    if (display.getRowCount() > 0) {
                        eventBus.getData(
                            getGrid(),
                            new SearchDefinition(
                                getPageStart(),
                                getPageSize(),
                                searchDataHolder,
                                sort.getSortOrder()),
                            ++requestId);
                    }
                }
                cancelRangeChangedEvent = false;
            }

            /**
             * Updates table rows count.
             * Don't create asynchronous call when rows count is 0.
             */
            @Override
            public void updateRowCount(int size, boolean exact) {
                if (size > 0) {
                    super.updateRowCount(size, exact);
                    eventBus.getData(
                        getGrid(),
                        new SearchDefinition(getPageStart(),
                                getPageSize(),
                                searchDataHolder,
                                sort.getSortOrder()),
                        ++requestId);
                } else {
                    super.updateRowCount(size, true);
                    resize(Document.get().getClientWidth());
                }
            }
        };
        this.dataProvider.addDataDisplay(this);
        this.createAsyncSortHandler();

    }

    /**
     * Sets requested data to table starting form <b>start</b>.
     */
    public void updateRowData(List<T> values, int requestId) {
        if (this.requestId == requestId) {
            dataProvider.updateRowData(getPageStart(), values);
            resize(Document.get().getClientWidth());
        }
    }

    /**
     * Creates aynchronous handler for sorting. Its called when asynchronous
     * data provider is created. See (@link createAsyncDataProvider(final int resultCount)).
     * Method onColumnSort it called when user choose different column to sort.
     */
    private void createAsyncSortHandler() {
        //Moze byt hned na zaciatku? Ak ano , tak potom aj asynchdataprovider by mohol nie?
        sortHandler = new AsyncHandler(this) {
            /**
             * On sort change, request for data in new sort order.
             */
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

                eventBus.getData(
                    getGrid(),
                    new SearchDefinition(getPageStart(), getPageSize(), searchDataHolder, sort.getSortOrder()),
                    ++requestId);
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
     * @param widthStyleName of the column
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

    /**************************************************************************/
    /* Setter metods                                                          */
    /**************************************************************************/
    /**
     * Recalculate table height if resize event occurs.
     * Usually paddings or margins changes on smaller resolutions.
     *
     * @param actualWidth
     */
    public void resize(int actualWidth) {
        int height = 0;
        //include table padding
        if (actualWidth < 480) {
            height = Constants.TABLE_MARGINS_TINY;
        } else if (actualWidth < 1200) {
            height = Constants.TABLE_MARGINS_SMALL;
        } else {
            height = Constants.TABLE_MARGINS_LARGE;
        }
        height += 40; //header
        //include table rows
        int rows = getVisibleItemCount();
        if (rows > 0) {
            height += rows * getRowElement(0).getOffsetHeight();
        } else {
            height += 80;
        }
        setHeight(height + "px");
    }

    /**************************************************************************/
    /* Getter metods                                                          */
    /**************************************************************************/
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
     * @return grid instance
     */
    public UniversalAsyncGrid getGrid() {
        return this;
    }

    /**
     * Sets cancelRangeChangedEvent flag to true causes next table rancheChangeEvent to be ignored.
     */
    public void cancelRangeChangedEvent() {
        cancelRangeChangedEvent = true;
    }

    /**
     * Sets sortable grid columns in order.
     * @param sort definition
     */
    public void setGridColumns(SortDataHolder sort) {
        this.sort = sort;
    }

    /**
     * Corrects grid height and shows loading.
     */
    public void showLoading() {
        getDataProvider().updateRowCount(0, false);
        resize(Document.get().getClientWidth());
    }

    /**
     * Reloads current table page.
     */
    public void refresh() {
        Range range = getVisibleRange();
        setVisibleRangeAndClearData(range, true); //1st way
        //Martin 22.4.2012 - should work too, but doesn't - why?
        //RangeChangeEvent.fire(this, range);              //2nd way
    }
}
