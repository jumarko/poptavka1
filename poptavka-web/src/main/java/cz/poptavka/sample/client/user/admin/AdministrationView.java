/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import cz.poptavka.sample.shared.domain.DemandDetail;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class AdministrationView extends Composite implements
        AdministrationPresenter.AdministrationInterface, HasValueChangeHandlers<String> {
    private static AdministrationViewUiBinder uiBinder =
            GWT.create(AdministrationViewUiBinder.class);

    /**
     * @return the dataProvider
     */
    @Override
    public ListDataProvider<DemandDetail> getDataProvider() {
//        ColumnSortEvent fire = ColumnSortEvent.fire(this, cellTable.getColumnSortList());
//        cellTable.fireEvent(fire);
        return dataProvider;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(
            ValueChangeHandler<String> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdministrationView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    CellTable<DemandDetail> cellTable;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    /**
     * The list of cells that are editable.
     */
    private List<AbstractEditableCell<?, ?>> editableCells;
    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public CellTable<DemandDetail> getCellTable() {
        return cellTable;
    }

    /** Editable Columns in CellTable. **/
    private Column<DemandDetail, String> demandTitleColumn;
    private Column<DemandDetail, String> clientIdColumn;

    @Override
    public void sortTitle() {
        // TODO ivlcek - sort data according to latest Date
        // We know that the data is sorted alphabetically by default.
//        ColumnSortInfo columnSortInfo = new ColumnSortInfo(title, true);
        cellTable.getColumnSortList().push(demandTitleColumn);
    }

//    @Override
//    public SimplePager getPager() {
//        return pager;
//    }
    @Override
    public void createView() {
        initCellTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initCellTable() {
        // Create a CellTable.
        GWT.log("initCellTable initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        cellTable = new CellTable<DemandDetail>(KEY_PROVIDER);
        cellTable.setWidth("100%", true);
//        cellTable.setRowCount(2, true);

        // TODO ivlcek - premysliet kedy a kde sa ma vytvarat DataProvider
        // Connect the table to the data provider.
        dataProvider.addDataDisplay(cellTable);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<DemandDetail> sortHandler = new ListHandler<DemandDetail>(
                dataProvider.getList());
        cellTable.addColumnSortHandler(sortHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);
        // TODO ivlcek - nastavit pocet zaznamov v pagery na mensi pocet ako 15
//        pager.setPageSize(5);

        // Add a selection model to handle user selection.
//        final MultiSelectionModel<DemandDetail> selectionModel = new MultiSelectionModel<DemandDetail>(KEY_PROVIDER);
        // Add a single selection model to handle user selection.
        final SingleSelectionModel<DemandDetail> selectionModel =
                new SingleSelectionModel<DemandDetail>(KEY_PROVIDER);
//        cellTable.setSelectionModel(selectionModel); - This worked without KEY_PROVIDER
        cellTable.setSelectionModel(selectionModel,
                DefaultSelectionEventManager.<DemandDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns(selectionModel, sortHandler);
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<DemandDetail> selectionModel,
            ListHandler<DemandDetail> sortHandler) {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
        // mouse selection.
        Column<DemandDetail, Boolean> checkColumn = new Column<DemandDetail, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(DemandDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

        // Demand ID.
        Column<DemandDetail, String> idColumn = new Column<DemandDetail, String>(new TextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return String.valueOf(object.getId());
            }
        };
        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                return Long.valueOf(o1.getId()).compareTo(Long.valueOf(o2.getId()));
            }
        });
        cellTable.addColumn(idColumn, "DID");
        cellTable.setColumnWidth(idColumn, 50, Unit.PX);

        // Clietn ID.
        clientIdColumn = new Column<DemandDetail, String>(
                new EditTextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return String.valueOf(object.getClientId());
            }
        };
        clientIdColumn.setSortable(true);
        sortHandler.setComparator(clientIdColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                return Long.valueOf(o1.getClientId()).compareTo(Long.valueOf(o2.getClientId()));
            }
        });
        clientIdColumn.setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
            @Override
            public void update(int index, DemandDetail object, String value) {
                // Called when the user changes the value.
                object.setClientId(Long.valueOf(value));
                dataProvider.refresh();
            }
        });
        cellTable.addColumn(clientIdColumn, "CID");
        cellTable.setColumnWidth(clientIdColumn, 50, Unit.PX);

        // Demand title.
//        Column<DemandDetail, String> title = new Column<DemandDetail, String>(
        demandTitleColumn = new Column<DemandDetail, String>(
                new EditTextCell()) {
            @Override
            public String getValue(DemandDetail object) {
                return object.getTitle();
            }
        };
        demandTitleColumn.setSortable(true);
        sortHandler.setComparator(demandTitleColumn, new Comparator<DemandDetail>() {
            @Override
            public int compare(DemandDetail o1, DemandDetail o2) {
                if (o1 == o2) {
                    return 0;
                }

                // Compare the name columns.
                if (o1 != null) {
                    return (o2 != null) ? o1.getTitle().compareTo(o2.getTitle()) : 1;
                }
                return -1;
            }
        });
        // TODO ivlcek - load columnn name from resources
        cellTable.addColumn(demandTitleColumn, "Title");
        demandTitleColumn.setFieldUpdater(new FieldUpdater<DemandDetail, String>() {
            @Override
            public void update(int index, DemandDetail object, String value) {
                // Called when the user changes the value.
                object.setTitle(value);
                dataProvider.refresh();
            }
        });

//        cellTable.setColumnWidth(title, 20, Unit.PCT);


//
//        // EditTextCell.
//        addColumn(new EditTextCell(new SafeHtmlRenderer<String>() {
//
//            @Override
//            public SafeHtml render(String object) {
//                // If the value comes from the user, we escape it to avoid XSS attacks.
//                SafeHtml safeValue = SafeHtmlUtils.fromString(object);
//                return safeValue;
//            }
//
//            @Override
//            public void render(String object, SafeHtmlBuilder builder) {
//                // Always do a null check on the value. Cell widgets can pass null to cells
//                // if the underlying data contains a null, or if the data arrives out of order.
//                if (object == null) {
//                    return;
//                }
//
//                // If the value comes from the user, we escape it to avoid XSS attacks.
//                SafeHtml safeValue = SafeHtmlUtils.fromString(object);
//
//                // Append some HTML that sets the text color.
//                builder.appendHtmlConstant("<div style=\"color:" + safeValue.asString()
//                        + "\">");
//                builder.append(safeValue);
//                builder.appendHtmlConstant("</div>");
//
//            }
//        }), "EditText", new GetValue<String>() {
//
//            @Override
//            public String getValue(DemandDetail demandDetail) {
////                dataProvider.refresh();
//                return demandDetail.getTitle();
//            }
//        }, new FieldUpdater<DemandDetail, String>() {
//
//            @Override
//            public void update(int index, DemandDetail demandDetail, String value) {
////                dataProvider.refresh();
//                pendingChanges.add(new DemandTitleChange(demandDetail, value));
//            }
//        });
    }

//
//    /**
//     * Updates the first name.
//     */
//    private static class DemandTitleChange extends PendingChange<String> {
//
//        public DemandTitleChange(DemandDetail demandDetail, String value) {
//            super(demandDetail, value);
//        }
//
//        @Override
//        protected void doCommit(DemandDetail demandDetail, String value) {
//            demandDetail.setTitle(value);
//        }
//    }
//    /**
//     * The list of pending changes.
//     */
//    private List<PendingChange<?>> pendingChanges = new ArrayList<
//      PendingChange<?>>();
//
//    /**
//     * A pending change to a {@link ContactInfo}. Changes aren't committed
//     * immediately to illustrate that cells can remember their pending changes.
//     *
//     * @param <T> the data type being changed
//     */
//    private abstract static class PendingChange<T> {
//
//        private final DemandDetail demandDetail;
//
//        private final T value;
//
//        public PendingChange(DemandDetail demandDetail, T value) {
//            this.demandDetail = demandDetail;
//            this.value = value;
//        }
//
//        /**
//         * Update the appropriate field in the {@link ContactInfo}.
//         *
//         * @param contact the contact to update
//         * @param value the new value
//         */
//        protected abstract void doCommit(DemandDetail demandDetail, T value);
//    }
    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {
        C getValue(DemandDetail demandDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<DemandDetail, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<DemandDetail, C> fieldUpdater) {
        Column<DemandDetail, C> column = new Column<DemandDetail, C>(cell) {
            @Override
            public C getValue(DemandDetail object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
        if (cell instanceof AbstractEditableCell<?, ?>) {
            editableCells.add((AbstractEditableCell<?, ?>) cell);
        }
        cellTable.addColumn(column, headerText);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a DemandDetail.
     */
    private static final ProvidesKey<DemandDetail> KEY_PROVIDER = new ProvidesKey<DemandDetail>() {
        @Override
        public Object getKey(DemandDetail item) {
            return item == null ? null : item.getId();
        }
    };
}
