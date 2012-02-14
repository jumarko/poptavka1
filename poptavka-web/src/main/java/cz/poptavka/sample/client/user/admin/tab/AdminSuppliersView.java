/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField Button commit, rollback, refresh;
    @UiField Label changesLabel;
    // PAGER
    @UiField(provided = true) SimplePager pager;
    @UiField(provided = true) ListBox pageSizeCombo;
    // DETAIL
    @UiField SimplePanel adminSupplierDetail;
    // TABLE
    @UiField(provided = true) DataGrid<FullSupplierDetail> dataGrid;
    private SingleSelectionModel<FullSupplierDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<FullSupplierDetail, String> idColumn;
    private Column<FullSupplierDetail, String> supplierNameColumn;
    private Column<FullSupplierDetail, String> supplierTypeColumn;
    private Column<FullSupplierDetail, Boolean> certifiedColumn;
    private Column<FullSupplierDetail, String> verificationColumn;
    // The key provider that provides the unique ID of a FullSupplierDetail.
    private static final ProvidesKey<FullSupplierDetail> KEY_PROVIDER = new ProvidesKey<FullSupplierDetail>() {

        @Override
        public Object getKey(FullSupplierDetail item) {
            return item == null ? null : item.getSupplierId();
        }
    };
    //
    //                          ***** INITIALIZATION *****
    //
    /**
     * creates WIDGET view.
     */
    @Override
    public void createView() {
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

    /**
     * Creates table with accessories - columns, pager, selection model.
     */
    private void initDataGrid() {
        GWT.log("init AdminSuppliers DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<FullSupplierDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // SELECTION MODEL
        selectionModel = new SingleSelectionModel<FullSupplierDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullSupplierDetail>createCheckboxManager());

        // COLUMNS
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Supplier ID.
        idColumn = addColumn(new ClickableTextCell(), "ID", true, 30, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return String.valueOf(object.getSupplierId());
            }
        });

        // Company name.
        supplierNameColumn = addColumn(new EditTextCell(), "CompanyName", true, 50, new GetValue<String>() {

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
        supplierTypeColumn = addColumn(new SelectionCell(types), "Type", true, 50, new GetValue<String>() {

            @Override
            public String getValue(FullSupplierDetail object) {
                return object.getBusinessType();
            }
        });

        // Certified.
        certifiedColumn = addColumn(new CheckboxCell(), "Cert.", true, 15, new GetValue<Boolean>() {

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
        verificationColumn = addColumn(new SelectionCell(verTypes), "Verified", true, 50, new GetValue<String>() {

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
    private interface GetValue<C> {

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
    private <C> Column<FullSupplierDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<FullSupplierDetail, C> column = new Column<FullSupplierDetail, C>(cell) {

            @Override
            public C getValue(FullSupplierDetail object) {
                return getter.getValue(object);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    //******************* GETTER METHODS (defined by interface) ****************
    //
    //                          *** TABLE ***

    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public DataGrid<FullSupplierDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: ID
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierIdColumn() {
        return idColumn;
    }

    /**
     * @return table column: NAME
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierNameColumn() {
        return supplierNameColumn;
    }

    /**
     * @return table column: TYPE
     */
    @Override
    public Column<FullSupplierDetail, String> getSupplierTypeColumn() {
        return supplierTypeColumn;
    }

    /**
     * @return table column: CERTIFIED
     */
    @Override
    public Column<FullSupplierDetail, Boolean> getCertifiedColumn() {
        return certifiedColumn;
    }

    /**
     * @return table column: VERIFIED
     */
    @Override
    public Column<FullSupplierDetail, String> getVerificationColumn() {
        return verificationColumn;

    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<FullSupplierDetail> getSelectionModel() {
        return selectionModel;
    }

    //                         *** PAGER ***
    /*
     * @return pager
     */
    @Override
    public SimplePager getPager() {
        return pager;
    }

    /**
     * @return table/pager size: COMBO
     */
    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    /**
     * @return table/pager size: VALUE
     */
    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    //                          *** BUTTONS ***
    /**
     * @return COMMIT button
     */
    @Override
    public Button getCommitBtn() {
        return commit;
    }

    /**
     * @return ROLLBACK button
     */
    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    /**
     * @return REFRESH button
     */
    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    //                          *** OTHER ***
    /**
     * @return label for displaying informations for user
     */
    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }

    /**
     * @return widget AdminSupplierInfoView as it is
     */
    @Override
    public SimplePanel getAdminSupplierDetail() {
        return adminSupplierDetail;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}