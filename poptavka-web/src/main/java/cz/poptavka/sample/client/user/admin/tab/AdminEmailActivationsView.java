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
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.adminModule.ActivationEmailDetail;

import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminEmailActivationsView extends Composite
        implements AdminEmailActivationsPresenter.AdminEmailActivationsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminEmailActivationsView> {
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
    @UiField(provided = true) DataGrid<ActivationEmailDetail> dataGrid;
    // Editable Columns
    private Column<ActivationEmailDetail, String> activationColumn;
    private Column<ActivationEmailDetail, Date> timeoutColumn;
    private SingleSelectionModel<ActivationEmailDetail> selectionModel;
    // The key provider that provides the unique ID of a EmailActivationDetail.
    private static final ProvidesKey<ActivationEmailDetail> KEY_PROVIDER = new ProvidesKey<ActivationEmailDetail>() {

        @Override
        public Object getKey(ActivationEmailDetail item) {
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
        GWT.log("init AdminEmailActivations DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<ActivationEmailDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        //SELECTION MODEL
        selectionModel = new SingleSelectionModel<ActivationEmailDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<ActivationEmailDetail>createCheckboxManager());

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
            public String getValue(ActivationEmailDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // Activation link
        activationColumn = addColumn(new EditTextCell(), "ActivationLink", 100, new GetValue<String>() {

            @Override
            public String getValue(ActivationEmailDetail object) {
                return object.getActivationLink();
            }
        });

        // timeout
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        timeoutColumn = addColumn(new DatePickerCell(dateFormat), "Timeout", 100, new GetValue<Date>() {

            @Override
            public Date getValue(ActivationEmailDetail object) {
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

        C getValue(ActivationEmailDetail emailActivationDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<ActivationEmailDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<ActivationEmailDetail, C> column = new Column<ActivationEmailDetail, C>(cell) {

            @Override
            public C getValue(ActivationEmailDetail object) {
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
    public DataGrid<ActivationEmailDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: TIMEOUT
     */
    @Override
    public Column<ActivationEmailDetail, Date> getTimeoutColumn() {
        return timeoutColumn;
    }

    /**
     * @return table column: ACTIVATION LINK
     */
    @Override
    public Column<ActivationEmailDetail, String> getActivationLinkColumn() {
        return activationColumn;
    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<ActivationEmailDetail> getSelectionModel() {
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
    /*
     * @return COMMIT button
     */

    @Override
    public Button getCommitBtn() {
        return commit;
    }
    /*
     * @return ROLLBACK button
     */

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }
    /*
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