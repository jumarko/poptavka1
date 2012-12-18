/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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

import com.eprovement.poptavka.shared.domain.adminModule.ActivationEmailDetail;
import java.util.Arrays;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminEmailActivationsView extends Composite
        implements AdminEmailActivationsPresenter.AdminEmailActivationsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminEmailActivationsView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final int ID_COL_WIDTH = 50;
    private static final int ACTIVATION_COL_WIDTH = 100;
    private static final int TIMEOUT_COL_WIDTH = 100;
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
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<ActivationEmailDetail> dataGrid;
    // Editable Columns
    private Column<ActivationEmailDetail, String> activationColumn;
    private Column<ActivationEmailDetail, Date> timeoutColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "activationCode", "timeout"
            });
    // The key provider that provides the unique ID of a EmailActivationDetail.
    private static final ProvidesKey<ActivationEmailDetail> KEY_PROVIDER = new ProvidesKey<ActivationEmailDetail>() {

        @Override
        public Object getKey(ActivationEmailDetail item) {
            return item == null ? null : item.getId();
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
        GWT.log("init AdminEmailActivations DataGrid initialized");

        // TABLE
        dataGrid = new UniversalAsyncGrid<ActivationEmailDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // COLUMNS
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        dataGrid.addColumn(new TextCell(), Storage.MSGS.id(), true, ID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((ActivationEmailDetail) object).getId());
                    }
                });

        // Activation link
        activationColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.activationCode(), true, ACTIVATION_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((ActivationEmailDetail) object).getActivationCode();
                    }
                });

        // timeout
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        timeoutColumn = dataGrid.addColumn(
                new DatePickerCell(dateFormat), Storage.MSGS.timeout(), true, TIMEOUT_COL_WIDTH,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((ActivationEmailDetail) object).getTimeout();
                    }
                });
    }

    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<ActivationEmailDetail> getDataGrid() {
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
    public Column<ActivationEmailDetail, String> getActivationCodeColumn() {
        return activationColumn;
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