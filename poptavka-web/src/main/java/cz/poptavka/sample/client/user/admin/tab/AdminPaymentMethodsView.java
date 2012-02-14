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

    interface AdminPaymentMethodsViewUiBinder extends UiBinder<Widget, AdminPaymentMethodsView> {
    }
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField Button commit, rollback, refresh;
    @UiField Label changesLabel;
    // PAGER
    @UiField(provided = true) SimplePager pager;
    @UiField(provided = true) ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true) DataGrid<PaymentMethodDetail> dataGrid;
    private SingleSelectionModel<PaymentMethodDetail> selectionModel;
    // Editable Columns
    private Column<PaymentMethodDetail, String> nameColumn;
    private Column<PaymentMethodDetail, String> descriptionColumn;
    // The key provider that provides the unique ID of a PaymentMethodDetail.
    private static final ProvidesKey<PaymentMethodDetail> KEY_PROVIDER = new ProvidesKey<PaymentMethodDetail>() {

        @Override
        public Object getKey(PaymentMethodDetail item) {
            return item == null ? null : item.getId();
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
        GWT.log("init AdminPaymentMethods DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<PaymentMethodDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // SELECTION MODEL
        selectionModel = new SingleSelectionModel<PaymentMethodDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<PaymentMethodDetail>createCheckboxManager());

        // COLUMNS
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
    private interface GetValue<C> {

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

    //******************* GETTER METHODS (defined by interface) ****************
    //
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public DataGrid<PaymentMethodDetail> getDataGrid() {
        return dataGrid;
    }
    /*
     * @return table column: NAME
     */

    @Override
    public Column<PaymentMethodDetail, String> getNameColumn() {
        return nameColumn;
    }

    /**
     * @return table column: DESCRIPTION
     */
    @Override
    public Column<PaymentMethodDetail, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<PaymentMethodDetail> getSelectionModel() {
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
     * @return ROLBACK button
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
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}