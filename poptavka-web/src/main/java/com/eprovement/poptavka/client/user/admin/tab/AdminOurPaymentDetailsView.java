/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.shared.domain.adminModule.PaymentDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
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
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminOurPaymentDetailsView extends Composite
        implements AdminOurPaymentDetailsPresenter.AdminOurPaymentDetailsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminOurPaymentDetailsView> {
    }
//*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    // PAGER
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<PaymentDetail> dataGrid;
    // Editable Columns in dataGrid. *
    private Column<PaymentDetail, String> bankAccountColumn;
    private Column<PaymentDetail, String> bankCodeColumn;
    private Column<PaymentDetail, String> ibanColumn;
    private Column<PaymentDetail, String> swiftCodeColumn;
    // The key provider that provides the unique ID of a PaymentDetail.
    private static final ProvidesKey<PaymentDetail> KEY_PROVIDER = new ProvidesKey<PaymentDetail>() {

        @Override
        public Object getKey(PaymentDetail item) {
            return item == null ? null : item.getId();
        }
    };

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

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new UniversalAsyncGrid<PaymentDetail>(KEY_PROVIDER, initSort());
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        //TODO LATER Martin - i18n
        // Demand ID.
        dataGrid.addColumn(new TextCell(), "ID", true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((PaymentDetail) object).getId());
                    }
                });

        // Bank Account
        bankAccountColumn = dataGrid.addColumn(new EditTextCell(), "BankAccount", true, "50px",
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((PaymentDetail) object).getBankAccount());
                    }
                });

        // Bank Code
        bankCodeColumn = dataGrid.addColumn(new EditTextCell(), "BankCode", true, "160px",
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((PaymentDetail) object).getBankCode());
                    }
                });

        // Iban
        ibanColumn = dataGrid.addColumn(new EditTextCell(), "Iban", true, "100px",
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((PaymentDetail) object).getIban();
                    }
                });

        // Swift Code
        swiftCodeColumn = dataGrid.addColumn(new EditTextCell(), "SwiftCode", true, "140px",
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((PaymentDetail) object).getSwiftCode();
                    }
                });

    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList();
        List<SortPair> defaultSort = Arrays.asList();
        return new SortDataHolder(defaultSort, sortColumns);
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public UniversalAsyncGrid<PaymentDetail> getDataGrid() {
        return dataGrid;
    }

    public Column<PaymentDetail, String> getBankAccountColumn() {
        return bankAccountColumn;
    }

    public Column<PaymentDetail, String> getBankCodeColumn() {
        return bankCodeColumn;
    }

    public Column<PaymentDetail, String> getIbanColumn() {
        return ibanColumn;
    }

    public Column<PaymentDetail, String> getSwiftCodeColumn() {
        return swiftCodeColumn;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
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
}