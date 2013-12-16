/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class AdminSuppliersView extends Composite implements AdminSuppliersPresenter.AdminSuppliersInterface {

    private static AdminSuppliersViewUiBinder uiBinder = GWT.create(AdminSuppliersViewUiBinder.class);

    interface AdminSuppliersViewUiBinder extends UiBinder<Widget, AdminSuppliersView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final String COMPANY_NAME_COL_WIDTH = "50px";
    private static final String BUSINESS_TYPE_COL_WIDTH = "50px";
    private static final String CERTIFIED_COL_WIDTH = "15px";
    private static final String VERIFICATION_COL_WIDTH = "50px";
    //
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    // PAGER
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    // DETAIL
    @UiField
    AdminSupplierInfoView adminSupplierDetail;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<FullSupplierDetail> dataGrid;
    // Editable Columns in dataGrid.
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

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
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
        dataGrid = new UniversalAsyncGrid<FullSupplierDetail>(KEY_PROVIDER, initSort());
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // COLUMNS
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Supplier ID.
        idColumn = dataGrid.addColumn(
                new ClickableTextCell(), Storage.MSGS.columnID(),
                true, Storage.GRSCS.dataGridStyle().colWidthId(),
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((FullSupplierDetail) object).getSupplierId());
                    }
                });

        // Company name.
        supplierNameColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.columCompanyName(), true, COMPANY_NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((FullSupplierDetail) object).getUserData().getCompanyName());
                    }
                });

        // SupplierType.
        ArrayList<String> types = new ArrayList<String>();
        for (BusinessType type : BusinessType.values()) {
            types.add(type.getValue());
        }
        supplierTypeColumn = dataGrid.addColumn(
                new SelectionCell(types), Storage.MSGS.columBusinessType(), true, BUSINESS_TYPE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((FullSupplierDetail) object).getUserData().getBusinessType().getValue();
                    }
                });

        // Certified.
        certifiedColumn = dataGrid.addColumn(
                new CheckboxCell(), Storage.MSGS.columCertified(), true, CERTIFIED_COL_WIDTH,
                new GetValue<Boolean>() {

                    @Override
                    public Boolean getValue(Object object) {
                        return ((FullSupplierDetail) object).isCertified();
                    }
                });

        // Verification.
        ArrayList<String> verTypes = new ArrayList<String>();
        for (Verification type : Verification.values()) {
            verTypes.add(type.name());
        }
        verificationColumn = dataGrid.addColumn(
                new SelectionCell(verTypes), Storage.MSGS.columVerified(), true, VERIFICATION_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((FullSupplierDetail) object).getUserData().getVerification().name();
                    }
                });
    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList();
        List<SortPair> defaultSort = Arrays.asList();
        return new SortDataHolder(defaultSort, sortColumns);
    }

    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<FullSupplierDetail> getDataGrid() {
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
    public AdminSupplierInfoView getAdminSupplierDetail() {
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