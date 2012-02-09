/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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

import cz.poptavka.sample.shared.domain.EmailActivationDetail;

import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminEmailActivationsView extends Composite
        implements AdminEmailActivationsPresenter.AdminEmailActivationsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    /**
     * @return the demandTypeColumn
     */
    @Override
    public Column<EmailActivationDetail, Date> getTimeoutColumn() {
        return timeoutColumn;
    }

    /**
     * @return the demandTypeColumn
     */
    @Override
    public Column<EmailActivationDetail, String> getActivationLinkColumn() {
        return activationColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<EmailActivationDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<EmailActivationDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminDemandDetail
     */
    @Override
    public SimplePanel getAdminDemandDetail() {
        return adminDemandDetail;
    }

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminEmailActivationsView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<EmailActivationDetail> dataGrid;
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
    private SingleSelectionModel<EmailActivationDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<EmailActivationDetail, String> activationColumn;
    private Column<EmailActivationDetail, Date> timeoutColumn;

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
        dataGrid = new DataGrid<EmailActivationDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<EmailActivationDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<EmailActivationDetail>createCheckboxManager());

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
            public String getValue(EmailActivationDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // Activation link
        activationColumn = addColumn(new EditTextCell(), "ActivationLink", 100, new GetValue<String>() {

            @Override
            public String getValue(EmailActivationDetail object) {
                return object.getActivationLink();
            }
        });

        // timeout
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        timeoutColumn = addColumn(new DatePickerCell(dateFormat), "Timeout", 100, new GetValue<Date>() {

            @Override
            public Date getValue(EmailActivationDetail object) {
                return object.getTimeout();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

        C getValue(EmailActivationDetail emailActivationDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<EmailActivationDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<EmailActivationDetail, C> column = new Column<EmailActivationDetail, C>(cell) {

            @Override
            public C getValue(EmailActivationDetail object) {
                return getter.getValue(object);
            }
        };
        column.setSortable(true);
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a EmailActivationDetail.
     */
    private static final ProvidesKey<EmailActivationDetail> KEY_PROVIDER = new ProvidesKey<EmailActivationDetail>() {

        @Override
        public Object getKey(EmailActivationDetail item) {
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