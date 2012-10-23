package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.cell.CustomImageCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.DemandStatusImageCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.OfferStateImageCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.StarCell;
import com.eprovement.poptavka.client.user.widget.grid.cell.UrgentImageCell;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ProvidesKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //*************************************************************************
    //                              INTERFACES                                *
    //*************************************************************************
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
    //*************************************************************************/
    //                          ATTRIBUTES                                    */
    //*************************************************************************/
    private static final int COLUMN_WIDTH_ICON = 7;
    private static final int COLUMN_WIDTH_URGENT = 15;
    /**
     * Asynchronous Data Provider. When all data count is known, asynchronous
     * data provider is created {@link #createAsyncDataProvider(final int resultCount)}.
     * Asks for new data when user choose different page.
     */
    private AsyncDataProvider<T> dataProvider = null;
    private int start = 0;
    /**
     * Asynchronous sorting handler. Provides handler for sorting data where
     * asynchronous data provider is used. When asynchronous data provider is
     * created, creates asynchronous sorting handler too. See {@link #createAsyncSortHandler()}.
     */
    private AsyncHandler sortHandler = null;
    /**
     * Holds information about sorting. Represent pairs <String, OrderType>
     * where key is <b>column name</b> and value: <b>ASC, DESC</b>. Column Names
     * are provided by
     * (@link orderColumns) attribute.
     */
    private Map<String, OrderType> orderColumns = new HashMap<String, OrderType>();
    /**
     * Represents table column names. Each widget using this class
     * (UniversalAsyncGrid) must define column names when creating
     * UniversalAsyncGrid. Column names are then used as key in orderColumns
     * attribute.
     */
    private List<String> gridColumns = null;
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
    // for EVERY text display
    // providing HTML safe display
    public static final ClickableTextCell TABLE_CLICKABLE_TEXT_CELL = new ClickableTextCell(
            new SafeHtmlRenderer<String>() {
                @Override
                public SafeHtml render(String object) {
                    return SafeHtmlUtils.fromTrustedString(object);
                }

                @Override
                public void render(String object, SafeHtmlBuilder builder) {
                    builder.appendHtmlConstant(object);
                }
            });

    /**
     * Constructor of UniversalAsyncGrid.
     *
     * @param gridColumns - define table column names
     */
    public UniversalAsyncGrid(List<String> gridColumns) {
        super();
        this.gridColumns = gridColumns;
        this.setCustomEmptyTableNotification();
    }

    /**
     * Constructor of UniversalAsyncGrid.
     *
     * @param keyProvider - define key provider for selection model
     * @param gridColumns - define table column names
     */
    public UniversalAsyncGrid(ProvidesKey<T> keyProvider, List<String> gridColumns) {
        super(keyProvider);
        this.gridColumns = gridColumns;
        this.setCustomEmptyTableNotification();
    }

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
        if (searchDefinition != null) {
            this.searchDataHolder = searchDefinition.getFilter();
        }
        clearData();

        eventBus.getDataCount(this, searchDefinition);
    }

    /**
     * Creates asynchronous data provider for a table. Must be called when count
     * of all data is known. Method onRangeChanged it called when user choose
     * different page.
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
                orderColumns.put(gridColumns.get(1), OrderType.ASC);
                //Aks for new data
                eventBus.getData(new SearchDefinition(start, start + length, searchDataHolder, orderColumns));
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

                eventBus.getData(new SearchDefinition(start, getPageSize(), searchDataHolder, orderColumns));
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

    /**
     * Create checkbox column providing selecting whole row/rows.
     *
     * @param selectionModel
     * @return checkColumn
     */
    public Column<T, Boolean> addCheckboxColumn(Header header) {
        Column<T, Boolean> checkColumn = new Column<T, Boolean>(new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(T object) {
                // Get the value from the selection model.
                return getSelectionModel().isSelected(object);
            }
        };
        addColumn(checkColumn, header);
        setColumnWidth(checkColumn, COLUMN_WIDTH_ICON, Unit.PX);
        return checkColumn;
    }

    /**
     * Creates offer state image column.
     *
     * @return created offer state image column
     */
    public Column<T, OfferStateType> addOfferStateColumn(String headerText) {
        Column<T, OfferStateType> col = new Column<T, OfferStateType>(new OfferStateImageCell()) {
            @Override
            public OfferStateType getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getOfferState();
            }
        };
        //set column style
        col.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        addColumn(col, headerText);
        setColumnWidth(col, COLUMN_WIDTH_ICON, Unit.PX);
        return col;
    }

    public Column<T, ImageResource> addIconColumn(final ImageResource imageResource, String explanationText) {
        Column<T, ImageResource> col = new Column<T, ImageResource>(new CustomImageCell(explanationText)) {
            @Override
            public ImageResource getValue(T object) {
                return imageResource;
            }
        };
        //set column style
        col.setCellStyleNames(Storage.RSCS.grid().cellTableIconColumn());
        addColumn(col);
        setColumnWidth(col, COLUMN_WIDTH_ICON, Unit.PX);
        return col;
    }

    /**
     * Creates demand status image column.
     *
     * @return created demands status image column
     */
    public Column<T, DemandStatus> addDemandStatusColumn(String headerText) {
        Column<T, DemandStatus> col = new Column<T, DemandStatus>(new DemandStatusImageCell()) {
            @Override
            public DemandStatus getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getDemandStatus();
            }
        };
        //set column style
        col.setCellStyleNames(Storage.RSCS.grid().cellTableIconColumn());
//        addColumn(col, headerText);
        addColumn(col);
        setColumnWidth(col, COLUMN_WIDTH_ICON, Unit.PX);
        return col;
    }

    /**
     * Creates urgencyDisplay column.
     *
     * @param sortHandler
     * @return urgencyColumn
     */
    public Column<T, Date> addUrgentColumn(String headerText) {
        Column<T, Date> urgencyColumn = new Column<T, Date>(new UrgentImageCell()) {
            @Override
            public Date getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.getEndDate();
            }
        };
        urgencyColumn.setSortable(true);
        addColumn(urgencyColumn, headerText);
        setColumnWidth(urgencyColumn, COLUMN_WIDTH_URGENT, Unit.PX);
        return urgencyColumn;
    }

    /**
     * Creates star-column depending on messages' isRead value. By clicking this cell, STAR attribute is immediately
     * updated in database.
     *
     * NOTE:
     * Sorting is not implemented now.
     * //TODO
     * Implement sorting according to star status
     *
     * @return star column
     */
    public Column<T, Boolean> addStarColumn() {
        Column<T, Boolean> col = new Column<T, Boolean>(new StarCell()) {
            @Override
            public Boolean getValue(T object) {
                TableDisplay obj = (TableDisplay) object;
                return obj.isStarred();
            }
        };
        //set column style
        col.setCellStyleNames(Storage.RSCS.grid().cellTableHandCursor());
        addColumn(col);
        setColumnWidth(col, COLUMN_WIDTH_ICON, Unit.PX);
        return col;
    }

    //*************************************************************************/
    // "OVERRIDE" METHODS
    //*************************************************************************/
    /**
     * Updates data provider content. When new data for are available, call this
     * method for display it.
     *
     * @param list
     */
    public void updateRowData(List<T> list) {
        if (dataProvider != null) {
            dataProvider.updateRowData(start, list);
            flush();
            redraw();
        }
    }

    /**
     * Clear data before displaying new.
     */
    public void clearData() {
        if (dataProvider != null) {
            //dataProvider.updateRowData(0, new ArrayList<T>());
            dataProvider.updateRowCount(0, false);
            flush();
            redraw();
        }
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
     * Displays message when no data are available for displaying. Customize
     * method content for user delight. Calls when instance of this class is
     * created.
     */
    public void setCustomEmptyTableNotification() {
        this.setEmptyTableWidget(new HTML("<div style=\"text-align: center;\">"
                + Storage.MSGS.emptyTable() + "</div>"));
    }

    /**
     * Gets start index of pagination.
     *
     * @return start index
     */
    public int getStart() {
        return start;
    }
}