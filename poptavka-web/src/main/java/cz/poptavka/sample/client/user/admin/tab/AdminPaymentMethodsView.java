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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.PaymentMethodDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminPaymentMethodsView extends Composite
        implements AdminPaymentMethodsPresenter.AdminPaymentMethodsInterface {

    private static AdminPaymentMethodsViewUiBinder uiBinder = GWT.create(AdminPaymentMethodsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    @Override
    public Column<PaymentMethodDetail, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    @Override
    public Column<PaymentMethodDetail, String> getNameColumn() {
        return nameColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<PaymentMethodDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<PaymentMethodDetail> getSelectionModel() {
        return selectionModel;
    }

    interface AdminPaymentMethodsViewUiBinder extends UiBinder<Widget, AdminPaymentMethodsView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<PaymentMethodDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<PaymentMethodDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<PaymentMethodDetail, String> nameColumn;
    private Column<PaymentMethodDetail, String> descriptionColumn;

    public AdminPaymentMethodsView() {
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
        dataGrid = new DataGrid<PaymentMethodDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<PaymentMethodDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<PaymentMethodDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        addColumn(new TextCell(), "ID", 50, new GetValue<String>() {

            @Override
            public String getValue(PaymentMethodDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // Name
        nameColumn = addColumn(new EditTextCell(), "Name", 100, new GetValue<String>() {

            @Override
            public String getValue(PaymentMethodDetail object) {
                return String.valueOf(object.getName());
            }
        });

        // Description
        descriptionColumn = addColumn(new EditTextCell(), "Description", 100, new GetValue<String>() {

            @Override
            public String getValue(PaymentMethodDetail object) {
                return object.getDescription();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(PaymentMethodDetail paymentMethodDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<PaymentMethodDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<PaymentMethodDetail, C> column = new Column<PaymentMethodDetail, C>(cell) {

            @Override
            public C getValue(PaymentMethodDetail object) {
                return getter.getValue(object);
            }
        };
        column.setSortable(true);
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a PaymentMethodDetail.
     */
    private static final ProvidesKey<PaymentMethodDetail> KEY_PROVIDER = new ProvidesKey<PaymentMethodDetail>() {

        @Override
        public Object getKey(PaymentMethodDetail item) {
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