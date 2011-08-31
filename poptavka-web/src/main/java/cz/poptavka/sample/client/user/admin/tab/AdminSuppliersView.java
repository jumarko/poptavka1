/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import cz.poptavka.sample.domain.user.BusinessType;
import cz.poptavka.sample.domain.user.Verification;

import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.ArrayList;

/**
 *
 * @author ivan.vlcek
 */
public class AdminSuppliersView extends Composite implements AdminSuppliersPresenter.AdminSuppliersInterface {

    private static AdminSuppliersViewUiBinder uiBinder = GWT.create(AdminSuppliersViewUiBinder.class);

    interface AdminSuppliersViewUiBinder extends UiBinder<Widget, AdminSuppliersView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    CellTable<FullSupplierDetail> cellTable;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    /**
     * The list of cells that are editable.
     */
//    private List<AbstractEditableCell<?, ?>> editableCells;
    /**
     * Detail of selected Supplier.
     */
    @UiField
    SimplePanel adminSupplierDetail;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Data provider that will cell table with data.
     */
//    private AsyncDataProvider<FullSupplierDetail> dataProvider = null;
    private SingleSelectionModel<FullSupplierDetail> selectionModel;
    /** Editable Columns in CellTable. **/
    private Column<FullSupplierDetail, String> supplierIdColumn;
    private Column<FullSupplierDetail, String> supplierNameColumn;
    private Column<FullSupplierDetail, String> supplierTypeColumn;
    private Column<FullSupplierDetail, Boolean> certifiedColumn;
    private Column<FullSupplierDetail, String> verificationColumn;
    private final BusinessType[] businessTypes = BusinessType.values();

    /**
     * @return the clientIdColumn
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierIdColumn() {
        return supplierIdColumn;
    }

    /**
     * @return the SupplierNameColumn
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    /**
     * @return the SupplierTypeColumn
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierTypeColumn() {
        return supplierTypeColumn;
    }

    /**
     * @return the SupplierTypeColumn
     */
    @Override
    public Column<FullSupplierDetail, Boolean> getCertifiedColumn() {
        return certifiedColumn;
    }

    /**
     * @return the SupplierTypeColumn
     */
    @Override
    public Column<FullSupplierDetail, String> getVerificationColumn() {
        return verificationColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public CellTable<FullSupplierDetail> getCellTable() {
        return cellTable;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    /**
     * @return the SupplierTypes
     */
    @Override
    public BusinessType[] getBusinessTypes() {
        return businessTypes;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<FullSupplierDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminSupplierDetail
     */
    @Override
    public SimplePanel getAdminSupplierDetail() {
        return adminSupplierDetail;
    }

    public AdminSuppliersView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(3);
        initCellTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

//    @Override
//    public void createView() {
//        initCellTable();
//        initWidget(uiBinder.createAndBindUi(this));
//    }
    private void initCellTable() {
        // Create a CellTable.
        GWT.log("Admin Suppliers initCellTable initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        cellTable = new CellTable<FullSupplierDetail>(KEY_PROVIDER);
        cellTable.setWidth("100%", true);
//        cellTable.setRowCount(2, true);

        // TODO ivlcek - premysliet kedy a kde sa ma vytvarat DataProvider
        // Connect the table to the data provider.
//        dataProvider.addDataDisplay(cellTable);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the list.

//                dataProvider.getList());
//        cellTable.addColumnSortHandler(sortHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(cellTable);
        // TODO ivlcek - nastavit pocet zaznamov v pagery na mensi pocet ako 15
//        pager.setPageSize(5);

        // Add a selection model to handle user selection.
//        final MultiSelectionModel<SupplierDetailForDisplaySuppliers> selectionModel =
//        new MultiSelectionModel<SupplierDetailForDisplaySuppliers>(KEY_PROVIDER);
        // Add a single selection model to handle user selection.
        selectionModel = new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER);
        cellTable.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullSupplierDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns(getSelectionModel());
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<FullSupplierDetail> selectionModel) {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call cellTable.setSelectionEnabled(true) to enable
        // mouse selection.
        Column<FullSupplierDetail, Boolean> checkColumn = new Column<FullSupplierDetail, Boolean>(
                new CheckboxCell(true, false)) {

            @Override
            public Boolean getValue(FullSupplierDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        cellTable.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        cellTable.setColumnWidth(checkColumn, 15, Unit.PX);

        // Supplier ID.
        supplierIdColumn = new Column<FullSupplierDetail, String>(new TextCell()) {

            @Override
            public String getValue(FullSupplierDetail object) {
                return String.valueOf(object.getSupplierId());
            }
        };
        supplierIdColumn.setSortable(true);
//        sortHandler.setComparator(supplierIdColumn, new Comparator<FullSupplierDetail>() {
//            @Override
//            public int compare(FullSupplierDetail o1, FullSupplierDetail o2) {
//                return Long.valueOf(o1.getSupplierId()).compareTo(Long.valueOf(o2.getSupplierId()));
//            }
//        });
        cellTable.addColumn(supplierIdColumn, "SID");
        cellTable.setColumnWidth(supplierIdColumn, 30, Unit.PX);

        // Company name.
        supplierNameColumn = new Column<FullSupplierDetail, String>(
                new EditTextCell()) {

            @Override
            public String getValue(FullSupplierDetail object) {
                return String.valueOf(object.getCompanyName());
            }
        };
        getSupplierNameColumn().setSortable(true);
//        sortHandler.setComparator(getSupplierNameColumn(), new Comparator<FullSupplierDetail>() {
//            @Override
//            public int compare(FullSupplierDetail o1, FullSupplierDetail o2) {
//                return (o1.getCompanyName()).compareToIgnoreCase(o2.getCompanyName());
//            }
//        });
        cellTable.addColumn(getSupplierNameColumn(), "Name");
        cellTable.setColumnWidth(getSupplierNameColumn(), 50, Unit.PX);

        // SupplierType.
        ArrayList<String> types = new ArrayList<String>();
        for (BusinessType type : BusinessType.values()) {
            types.add(type.getValue());
        }
        supplierTypeColumn = new Column<FullSupplierDetail, String>(
                new SelectionCell(types)) {

            @Override
            public String getValue(FullSupplierDetail object) {
                return object.getBusinessType();
            }
        };
        getSupplierTypeColumn().setSortable(true);
//        sortHandler.setComparator(getSupplierTypeColumn(), new Comparator<FullSupplierDetail>() {
//            @Override
//            public int compare(FullSupplierDetail o1, FullSupplierDetail o2) {
//                return o1.getBusinessType().compareToIgnoreCase(o2.getBusinessType());
//            }
//        });
        cellTable.addColumn(supplierTypeColumn, "Type");
        cellTable.setColumnWidth(supplierTypeColumn, 50, Unit.PX);

        // Certified.
        certifiedColumn = new Column<FullSupplierDetail, Boolean>(
                new CheckboxCell()) {

            @Override
            public Boolean getValue(FullSupplierDetail object) {
                return object.isCertified();
            }
        };
        getSupplierTypeColumn().setSortable(true);
//        sortHandler.setComparator(getSupplierTypeColumn(), new Comparator<FullSupplierDetail>() {
//            @Override
//            public int compare(FullSupplierDetail o1, FullSupplierDetail o2) {
//                return Boolean.toString(o1.isCertified()).compareTo(Boolean.toString(o2.isCertified()));
//            }
//        });
        cellTable.addColumn(certifiedColumn, "Cert.");
        cellTable.setColumnWidth(certifiedColumn, 15, Unit.PX);

        // Verification.
        ArrayList<String> verTypes = new ArrayList<String>();
        for (Verification type : Verification.values()) {
            verTypes.add(type.name());
        }
        verificationColumn = new Column<FullSupplierDetail, String>(
                new SelectionCell(verTypes)) {

            @Override
            public String getValue(FullSupplierDetail object) {
                return object.getVerification();
            }
        };
        getSupplierTypeColumn().setSortable(true);
//        sortHandler.setComparator(getSupplierTypeColumn(), new Comparator<FullSupplierDetail>() {
//            @Override
//            public int compare(FullSupplierDetail o1, FullSupplierDetail o2) {
//                return o1.getVerification().compareToIgnoreCase(o2.getVerification());
//            }
//        });
        cellTable.addColumn(verificationColumn, "Verified");
        cellTable.setColumnWidth(verificationColumn, 50, Unit.PX);
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(FullSupplierDetail supplierDetailForDisplaySuppliers);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<FullSupplierDetail, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<FullSupplierDetail, C> fieldUpdater) {
        Column<FullSupplierDetail, C> column = new Column<FullSupplierDetail, C>(cell) {

            @Override
            public C getValue(FullSupplierDetail object) {
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
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
    private static final ProvidesKey<FullSupplierDetail> KEY_PROVIDER = new ProvidesKey<FullSupplierDetail>() {

        @Override
        public Object getKey(FullSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };
}
