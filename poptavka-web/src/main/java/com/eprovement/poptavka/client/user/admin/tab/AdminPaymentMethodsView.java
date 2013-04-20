/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.domain.enums.OrderType;
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

import com.eprovement.poptavka.shared.domain.adminModule.PaymentMethodDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminPaymentMethodsView extends Composite
        implements AdminPaymentMethodsPresenter.AdminPaymentMethodsInterface {

    private static AdminPaymentMethodsViewUiBinder uiBinder = GWT.create(AdminPaymentMethodsViewUiBinder.class);

    interface AdminPaymentMethodsViewUiBinder extends UiBinder<Widget, AdminPaymentMethodsView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final String NAME_COL_WIDTH = "30%";
    private static final String DESCRIPTION_COL_WIDTH = "70%";
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
    UniversalAsyncGrid<PaymentMethodDetail> dataGrid;
    // Editable Columns
    private Column<PaymentMethodDetail, String> nameColumn;
    private Column<PaymentMethodDetail, String> descriptionColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "name", "description"
            });
    // The key provider that provides the unique ID of a PaymentMethodDetail.
    private static final ProvidesKey<PaymentMethodDetail> KEY_PROVIDER = new ProvidesKey<PaymentMethodDetail>() {

        @Override
        public Object getKey(PaymentMethodDetail item) {
            return item == null ? null : item.getId();
        }
    };

    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************///
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
        dataGrid = new UniversalAsyncGrid<PaymentMethodDetail>(KEY_PROVIDER, initSort());
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

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
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((PaymentMethodDetail) object).getId());
                    }
                });

        // Name
        nameColumn = dataGrid.addColumn(new EditTextCell(), Storage.MSGS.columnName(), true, NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((PaymentMethodDetail) object).getName());
                    }
                });

        // Description
        descriptionColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.columDescription(), true, DESCRIPTION_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((PaymentMethodDetail) object).getDescription();
                    }
                });
    }

    private SortDataHolder initSort() {
        List<SortPair> sortPairs = Arrays.asList(new SortPair(gridColumns.get(0), OrderType.DESC));
        return new SortDataHolder(sortPairs, gridColumns);
    }

    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<PaymentMethodDetail> getDataGrid() {
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