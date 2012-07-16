/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
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

import com.eprovement.poptavka.shared.domain.adminModule.InvoiceDetail;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminInvoicesView extends Composite implements AdminInvoicesPresenter.AdminInvoicesInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminInvoicesView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final int ID_COL_WIDTH = 50;
    private static final int INVOICE_COL_WIDTH = 50;
    private static final int VAR_SYMB_COL_WIDTH = 60;
    private static final int TOTAL_PRICE_COL_WIDTH = 60;
    private static final int PAY_METHOD_COL_WIDTH = 60;
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
    AdminInvoiceInfoView adminInvoiceDetail;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<InvoiceDetail> dataGrid;
    // Editable Columns
    private Column<InvoiceDetail, String> idColumn;
    private Column<InvoiceDetail, String> priceColumn;
    private Column<InvoiceDetail, String> varSymbolColumn;
    private Column<InvoiceDetail, String> payMethodColumn;
    private Column<InvoiceDetail, String> invoiceNumberColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "invoiceNumber", "variableSymbol", "totalPrice", "paymentMethod"
            });
    // i18n
    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());
    // The key provider that provides the unique ID of a InvoiceDetail.
    private static final ProvidesKey<InvoiceDetail> KEY_PROVIDER = new ProvidesKey<InvoiceDetail>() {

        @Override
        public Object getKey(InvoiceDetail item) {
            return item == null ? null : item.getId();
        }
    };

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
    /**
     * creates WIDGET view
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
     * Creates table with accessories - columns, pager, selection model
     */
    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new UniversalAsyncGrid<InvoiceDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

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
        // ID
        idColumn = dataGrid.addColumn(
                new ClickableTextCell(), Storage.MSGS.id(), true, ID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((InvoiceDetail) object).getId());
                    }
                });

        // Invoice number
        invoiceNumberColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.invoiceNumber(), true, INVOICE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((InvoiceDetail) object).getInvoiceNumber());
                    }
                });

        // variable symbol
        varSymbolColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.varSymb(), true, VAR_SYMB_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((InvoiceDetail) object).getVariableSymbol());
                    }
                });

        // total price
        priceColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.totalPrice(), true, TOTAL_PRICE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return currencyFormat.format(((InvoiceDetail) object).getTotalPrice().longValue());
                    }
                });

        // DemandStatus.
        payMethodColumn = dataGrid.addColumn(
                new TextCell(), Storage.MSGS.payMethod(), false, PAY_METHOD_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((InvoiceDetail) object).getPaymentMethod().getName();
                    }
                });
    }

    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<InvoiceDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<InvoiceDetail, String> getIdColumn() {
        return idColumn;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<InvoiceDetail, String> getPriceColumn() {
        return priceColumn;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<InvoiceDetail, String> getInvoiceNumberColumn() {
        return invoiceNumberColumn;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<InvoiceDetail, String> getVariableSymbolColumn() {
        return varSymbolColumn;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<InvoiceDetail, String> getPaymentMethodColumn() {
        return payMethodColumn;
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
     * @return widget AdminClientInfoView as it is
     */
    @Override
    public AdminInvoiceInfoView getAdminInvoiceDetail() {
        return adminInvoiceDetail;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}