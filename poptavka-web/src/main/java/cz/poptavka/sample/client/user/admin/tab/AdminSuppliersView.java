/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
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
    DataGrid<FullSupplierDetail> dataGrid;
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
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<FullSupplierDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<FullSupplierDetail, String> supplierNameColumn;
    private Column<FullSupplierDetail, String> supplierTypeColumn;
    private Column<FullSupplierDetail, Boolean> certifiedColumn;
    private Column<FullSupplierDetail, String> verificationColumn;

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
    public DataGrid<FullSupplierDetail> getDataGrid() {
        return dataGrid;
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

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    @Override
    public Label getChangesLabel() {
        return changesLabel;
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
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        changesLabel.setText("0");
    }

//    @Override
//    public void createView() {
//        initDataGrid();
//        initWidget(uiBinder.createAndBindUi(this));
//    }
    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("Admin Suppliers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<FullSupplierDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Add a selection model to handle user selection.
        selectionModel = new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullSupplierDetail>createCheckboxManager());

        // Initialize the columns.
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Supplier ID.
        addColumn(new TextCell(), "SID", 30, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return String.valueOf(object.getSupplierId());
            }
        });

        // Company name.
        supplierNameColumn = addColumn(new EditTextCell(), "Name", 50, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return String.valueOf(object.getCompanyName());
            }
        });

        // SupplierType.
        ArrayList<String> types = new ArrayList<String>();
        for (BusinessType type : BusinessType.values()) {
            types.add(type.getValue());
        }
        supplierTypeColumn = addColumn(new SelectionCell(types), "Type", 50, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return object.getBusinessType();
            }
        });

        // Certified.
        certifiedColumn = addColumn(new CheckboxCell(), "Cert.", 15, new GetValue<Boolean>() {

            @Override
            public Boolean getValue(FullSupplierDetail object) {
                return object.isCertified();
            }
        });

        // Verification.
        ArrayList<String> verTypes = new ArrayList<String>();
        for (Verification type : Verification.values()) {
            verTypes.add(type.name());
        }
        verificationColumn = addColumn(new SelectionCell(verTypes), "Verified", 50, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return object.getVerification();
            }
        });
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
    private <C> Column<FullSupplierDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<FullSupplierDetail, C> column = new Column<FullSupplierDetail, C>(cell) {

            @Override
            public C getValue(FullSupplierDetail object) {
                return getter.getValue(object);
            }
        };
        if (headerText.endsWith("<br/>")) {
            dataGrid.addColumn(column, SafeHtmlUtils.fromSafeConstant("<br/>"));
        } else {
            dataGrid.addColumn(column, headerText);
        }
        dataGrid.setColumnWidth(column, width, Unit.PX);
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
