/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.shared.domain.adminModule.ClientDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
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
public class AdminClientsView extends Composite implements AdminClientsPresenter.AdminClientsInterface {

    private static AdminClientsViewUiBinder uiBinder = GWT.create(AdminClientsViewUiBinder.class);

    interface AdminClientsViewUiBinder extends UiBinder<Widget, AdminClientsView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final String COMPANY_COL_WIDTH = "50px";
    private static final String FIRST_NAME_COL_WIDTH = "80px";
    private static final String LAST_NAME_COL_WIDTH = "80px";
    private static final String RATING_COL_WIDTH = "40px";
    //
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    // DETAIL
    @UiField
    AdminClientInfoView adminClientDetail;
    // PAGER
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<ClientDetail> dataGrid;
    // Editable Columns
    private Column<ClientDetail, String> idColumn;
    private Column<ClientDetail, String> companyColumn;
    private Column<ClientDetail, String> firstNameColumn;
    private Column<ClientDetail, String> lastNameColumn;
    private Column<ClientDetail, String> ratingColumn;
    // The key provider that provides the unique ID of a ClientDetail.
    private static final ProvidesKey<ClientDetail> KEY_PROVIDER = new ProvidesKey<ClientDetail>() {

        @Override
        public Object getKey(ClientDetail item) {
            return item == null ? null : item.getId();
        }
    };
    //*************************************************************************/
    //                              INITIALIZATION                            */
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
        GWT.log("init AdminClientsView DataGrid initialized");

        // TABLE
        dataGrid = new UniversalAsyncGrid<ClientDetail>(KEY_PROVIDER, initSort());
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
        idColumn = dataGrid.addColumn(new ClickableTextCell(), Storage.MSGS.columnID(),
                true, Constants.COL_WIDTH_ID,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((ClientDetail) object).getId());
                    }
                });

        // company
        companyColumn = dataGrid.addColumn(new EditTextCell(), Storage.MSGS.columnCompany(), true, COMPANY_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((ClientDetail) object).getUserDetail().getCompanyName());
                    }
                });

        // firstName
        firstNameColumn = dataGrid.addColumn(new EditTextCell(),
                Storage.MSGS.columnFirstName(), true, FIRST_NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((ClientDetail) object).getUserDetail().getPersonFirstName();
                    }
                });

        // lastName
        lastNameColumn = dataGrid.addColumn(new EditTextCell(),
                Storage.MSGS.columnLastName(), true, LAST_NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((ClientDetail) object).getUserDetail().getPersonLastName();
                    }
                });

        // rating
        ratingColumn = dataGrid.addColumn(new EditTextCell(), Storage.MSGS.columnRating(), true, RATING_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        if (((ClientDetail) object).getOveralRating() == -1) {
                            return "";
                        } else {
                            return Integer.toString(((ClientDetail) object).getOveralRating());
                        }
                    }
                });
    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList();
        List<SortPair> defaultSort = Arrays.asList();
        return new SortDataHolder(defaultSort, sortColumns);
    }

    //*************************************************************************/
    //                     GETTER METHODS (defined by interface)              */
    //*************************************************************************/
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<ClientDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: ID
     */
    @Override
    public Column<ClientDetail, String> getIdColumn() {
        return idColumn;
    }

    /**
     * @return table column: COMPANY
     */
    @Override
    public Column<ClientDetail, String> getCompanyColumn() {
        return companyColumn;
    }

    /**
     * @return table column: FIRST NAME
     */
    @Override
    public Column<ClientDetail, String> getFirstNameColumn() {
        return firstNameColumn;
    }

    /**
     * @return table column: LAST NAME
     */
    @Override
    public Column<ClientDetail, String> getLastNameColumn() {
        return lastNameColumn;
    }

    /**
     * @return table column: RATING
     */
    @Override
    public Column<ClientDetail, String> getRatingColumn() {
        return ratingColumn;
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
     * @return widget AdminClientDetailView as it is
     */
    @Override
    public AdminClientInfoView getAdminClientDetail() {
        return adminClientDetail;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}