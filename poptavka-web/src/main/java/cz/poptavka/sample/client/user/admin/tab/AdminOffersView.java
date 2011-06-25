/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.type.OfferStateType;



/**
 *
 * @author ivan.vlcek
 */
public class AdminOffersView extends Composite implements
        AdminOffersPresenter.AdminOffersInterface {
    private static AdministrationViewUiBinder uiBinder =
            GWT.create(AdministrationViewUiBinder.class);

    /**
     * @return the dataProvider
     */
    @Override
    public ListDataProvider<OfferDetail> getDataProvider() {
//        ColumnSortEvent fire = ColumnSortEvent.fire(this, cellTable.getColumnSortList());
//        cellTable.fireEvent(fire);
        return dataProvider;
    }

    /**
     * @return the clientIdColumn
     */
    @Override
    public Column<OfferDetail, String> getSupplierIdColumn() {
        return supplierIdColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public CellTable<OfferDetail> getCellTable() {
        return cellTable;
    }

    /**
     * @return the demandStatuses
     */
    @Override
    public OfferStateType[] getOfferStatuses() {
        return offerStatuses;
    }

    /**
     * @return the demandStatusColumn
     */
    @Override
    public Column<OfferDetail, String> getOfferStatusColumn() {
        return offerStatusColumn;
    }

    /**
     * @return the demandEndColumn
     */
    @Override
    public Column<OfferDetail, Date> getOfferFinishColumn() {
        return offerFinishColumn;
    }

    /**
     * @return the selectionModel
     */
    public SingleSelectionModel<OfferDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminDemandDetail
     */
    public SimplePanel getAdminDemandDetail() {
        return adminDemandDetail;
    }

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdminOffersView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    CellTable<OfferDetail> cellTable;
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
     * Detail of selected Demand.
     */
    @UiField
    SimplePanel adminDemandDetail;
    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<OfferDetail> dataProvider = new ListDataProvider<OfferDetail>();
    private SingleSelectionModel<OfferDetail> selectionModel;
    /** Editable Columns in CellTable. **/
    private Column<OfferDetail, String> supplierIdColumn;
    private Column<OfferDetail, String> offerStatusColumn;
    private Column<OfferDetail, Date> offerFinishColumn;
    private final OfferStateType[] offerStatuses = OfferStateType.values();

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
        cellTable = new CellTable<OfferDetail>(KEY_PROVIDER);
        cellTable.setWidth("100%", true);
//        cellTable.setRowCount(2, true);

        // TODO ivlcek - premysliet kedy a kde sa ma vytvarat DataProvider
        // Connect the table to the data provider.
        dataProvider.addDataDisplay(cellTable);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<OfferDetail> sortHandler = new ListHandler<OfferDetail>(
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
        selectionModel = new SingleSelectionModel<OfferDetail>(KEY_PROVIDER);
        cellTable.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<OfferDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns(getSelectionModel(), sortHandler);
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<OfferDetail> selectionModel,
            ListHandler<OfferDetail> sortHandler) {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
        // mouse selection.
        Column<OfferDetail, Boolean> checkColumn = new Column<OfferDetail, Boolean>(
                new CheckboxCell(true, false)) {
            @Override
            public Boolean getValue(OfferDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        cellTable.setColumnWidth(checkColumn, 40, Unit.PX);

        // Demand ID.
        Column<OfferDetail, String> idColumn = new Column<OfferDetail, String>(new TextCell()) {
            @Override
            public String getValue(OfferDetail object) {
                return String.valueOf(object.getMessageId());
            }
        };
        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, new Comparator<OfferDetail>() {
            @Override
            public int compare(OfferDetail o1, OfferDetail o2) {
                return Long.valueOf(o1.getMessageId()).compareTo(Long.valueOf(o2.getMessageId()));
            }
        });
        cellTable.addColumn(idColumn, "DID");
        cellTable.setColumnWidth(idColumn, 50, Unit.PX);

        // Clietn ID.
        supplierIdColumn = new Column<OfferDetail, String>(
                new EditTextCell()) {
            @Override
            public String getValue(OfferDetail object) {
                return String.valueOf(object.getSupplierId());
            }
        };
        getSupplierIdColumn().setSortable(true);
        sortHandler.setComparator(getSupplierIdColumn(), new Comparator<OfferDetail>() {
            @Override
            public int compare(OfferDetail o1, OfferDetail o2) {
                return Long.valueOf(o1.getSupplierId()).compareTo(Long.valueOf(o2.getSupplierId()));
            }
        });
        cellTable.addColumn(getSupplierIdColumn(), "CID");
        cellTable.setColumnWidth(getSupplierIdColumn(), 50, Unit.PX);


        // OfferStatus.
        List<String> offerStatesNames = new ArrayList<String>();
        for (OfferStateType offerStatusDetail : offerStatuses) {
            // TODO ivlcek - add Localizable name of DemandTypeDetail enum
            offerStatesNames.add(offerStatusDetail.getValue());
        }
        SelectionCell offerStatusCell = new SelectionCell(offerStatesNames);
        offerStatusColumn = new Column<OfferDetail, String>(
                offerStatusCell) {
            @Override
            public String getValue(OfferDetail object) {
                // TODO ivlcek - localize message
                return object.getState();
            }
        };
        cellTable.addColumn(offerStatusColumn, "State");
        cellTable.setColumnWidth(offerStatusColumn, 160, Unit.PX);

        // Demand expiration date.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);

        // Demand end date.
        offerFinishColumn = addColumn(new DatePickerCell(dateFormat), "End",
                new GetValue<Date>() {
                    @Override
                    public Date getValue(OfferDetail offerDetail) {
                        return offerDetail.getFinishDate();
                    }
                }, null);
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {
        C getValue(OfferDetail offerDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<OfferDetail, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<OfferDetail, C> fieldUpdater) {
        Column<OfferDetail, C> column = new Column<OfferDetail, C>(cell) {
            @Override
            public C getValue(OfferDetail object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
//        if (cell instanceof AbstractEditableCell<?, ?>) {
//            editableCells.add((AbstractEditableCell<?, ?>) cell);
//        }
        cellTable.addColumn(column, headerText);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a DemandDetail.
     */
    private static final ProvidesKey<OfferDetail> KEY_PROVIDER = new ProvidesKey<OfferDetail>() {
        @Override
        public Object getKey(OfferDetail item) {
            return item == null ? null : item.getMessageId();
        }
    };

    @Override
    public SimplePanel getAdminOfferDetail() {
        // TODO Auto-generated method stub
        return null;
    }
}
