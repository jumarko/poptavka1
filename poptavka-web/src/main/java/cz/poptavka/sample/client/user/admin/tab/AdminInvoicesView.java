/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
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

import cz.poptavka.sample.shared.domain.InvoiceDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminInvoicesView extends Composite implements AdminInvoicesPresenter.AdminInvoicesInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    /**
     * @return the ID Column
     */
    @Override
    public Column<InvoiceDetail, String> getIdColumn() {
        return idColumn;
    }

    /**
     * @return the price Column
     */
    @Override
    public Column<InvoiceDetail, String> getPriceColumn() {
        return priceColumn;
    }

    /**
     * @return the invoice number Column
     */
    @Override
    public Column<InvoiceDetail, String> getInvoiceNumberColumn() {
        return invoiceNumberColumn;
    }

    /**
     * @return the variable symbol Column
     */
    @Override
    public Column<InvoiceDetail, String> getVariableSymbolColumn() {
        return varSymbolColumn;
    }

    /**
     * @return the payment method sColumn
     */
    @Override
    public Column<InvoiceDetail, String> getPaymentMethodColumn() {
        return payMethodColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<InvoiceDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<InvoiceDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminDemandDetail
     */
    @Override
    public SimplePanel getAdminDemandDetail() {
        return adminDemandDetail;
    }

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminInvoicesView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<InvoiceDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Detail of selected Demand.
     */
    @UiField
    SimplePanel adminDemandDetail;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<InvoiceDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<InvoiceDetail, String> idColumn;
    private Column<InvoiceDetail, String> priceColumn;
    private Column<InvoiceDetail, String> varSymbolColumn;
    private Column<InvoiceDetail, String> payMethodColumn;
    private Column<InvoiceDetail, String> invoiceNumberColumn;

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
        dataGrid = new DataGrid<InvoiceDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<InvoiceDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<InvoiceDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        // ID
        idColumn = addColumn(new MyClickCell(), "ID", 50, new GetValue<String>() {

            @Override
            public String getValue(InvoiceDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // Invoice number
        invoiceNumberColumn = addColumn(new EditTextCell(), "InvoiceNumber", 50, new GetValue<String>() {

            @Override
            public String getValue(InvoiceDetail object) {
                return String.valueOf(object.getInvoiceNumber());
            }
        });

        // variable symbol
        varSymbolColumn = addColumn(new EditTextCell(), "Var. Symb", 60, new GetValue<String>() {

            @Override
            public String getValue(InvoiceDetail object) {
                return String.valueOf(object.getVariableSymbol());
            }
        });

        // total price
        priceColumn = addColumn(new EditTextCell(), "Total price", 60, new GetValue<String>() {

            @Override
            public String getValue(InvoiceDetail object) {
                return Long.toString(object.getTotalPrice().longValue());
            }
        });

        // DemandStatus.
        payMethodColumn = addColumn(new TextCell(), "Pay Method", 60, new GetValue<String>() {

            @Override
            public String getValue(InvoiceDetail object) {
                return object.getPaymentMethod().getName();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(InvoiceDetail invoiceDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<InvoiceDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<InvoiceDetail, C> column = new Column<InvoiceDetail, C>(cell) {

            @Override
            public C getValue(InvoiceDetail object) {
                return getter.getValue(object);
            }
        };
        column.setSortable(true);
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a InvoiceDetail.
     */
    private static final ProvidesKey<InvoiceDetail> KEY_PROVIDER = new ProvidesKey<InvoiceDetail>() {

        @Override
        public Object getKey(InvoiceDetail item) {
            return item == null ? null : item.getId();
        }
    };

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