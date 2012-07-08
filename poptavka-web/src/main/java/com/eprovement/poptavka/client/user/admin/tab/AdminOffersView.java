/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
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
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.type.OfferStateType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminOffersView extends Composite implements AdminOffersPresenter.AdminOffersInterface {

    private static AdministrationViewUiBinder uiBinder = GWT.create(AdministrationViewUiBinder.class);

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdminOffersView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final int OID_COL_WIDTH = 40;
    private static final int DID_COL_WIDTH = 40;
    private static final int SID_COL_WIDTH = 40;
    private static final int PRICE_COL_WIDTH = 40;
    private static final int STATE_COL_WIDTH = 50;
    private static final int CREATED_COL_WIDTH = 50;
    private static final int FINNISTH_COL_WIDTH = 50;
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
//    @UiField AdminOfferInfoView adminOfferDetail;
    // TABLE
    @UiField(provided = true)
    UniversalAsyncGrid<OfferDetail> dataGrid;
    // Editable Columns
    private Column<OfferDetail, String> priceColumn;
    private Column<OfferDetail, String> offerStatusColumn;
    private Column<OfferDetail, Date> offerCreationDateColumn;
    private Column<OfferDetail, Date> offerFinishDateColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "demand.id", "supplier.id", "price", "state", "", "finnishDate"
            });
    // i18n
    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);
    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());
    // The key provider that provides the unique ID of a DemandDetail.
    private static final ProvidesKey<OfferDetail> KEY_PROVIDER = new ProvidesKey<OfferDetail>() {

        @Override
        public Object getKey(OfferDetail item) {
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
        // Create a dataGrid.
        GWT.log("Admin Offers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new UniversalAsyncGrid<OfferDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Initialize the columns.
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Offer ID
        dataGrid.addColumn(new TextCell(), Storage.MSGS.oid(), true, OID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return Long.toString(((OfferDetail) object).getId());
                    }
                });
        // Demand ID.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.did(), true, DID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return Long.toString(((OfferDetail) object).getDemandId());
                    }
                });
        // Supplier ID
        dataGrid.addColumn(new TextCell(), Storage.MSGS.sid(), true, SID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return Long.toString(((OfferDetail) object).getSupplierId());
                    }
                });
        priceColumn = dataGrid.addColumn(new EditTextCell(), Storage.MSGS.price(), true, PRICE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return currencyFormat.format(((OfferDetail) object).getPrice());
                    }
                });

        // OfferStatus.
        ArrayList<String> stateList = new ArrayList<String>();
        for (OfferStateType offerStatusDetail : OfferStateType.values()) {
            stateList.add(offerStatusDetail.getValue());
        }
        offerStatusColumn = dataGrid.addColumn(
                new SelectionCell(stateList), Storage.MSGS.state(), true, STATE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((OfferDetail) object).getState();
                    }
                });

        // Creation date
        offerCreationDateColumn = dataGrid.addColumn(
                new DateCell(), Storage.MSGS.createdDate(), false, CREATED_COL_WIDTH,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((OfferDetail) object).getFinishDate();
                        //TODO Martin - dorobit creation date
                    }
                });

        // Demand end date.
        offerFinishDateColumn = dataGrid.addColumn(
                new DateCell(), Storage.MSGS.finnishDate(), true, FINNISTH_COL_WIDTH,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(Object object) {
                        return ((OfferDetail) object).getFinishDate();
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
    public UniversalAsyncGrid<OfferDetail> getDataGrid() {
        return dataGrid;
    }
    /*
     * @return table column: PRICE
     */

    @Override
    public Column<OfferDetail, String> getPriceColumn() {
        return priceColumn;
    }
    /*
     * @return table column: STATUS
     */

    @Override
    public Column<OfferDetail, String> getOfferStatusColumn() {
        return offerStatusColumn;
    }
    /*
     * @return table column: CREATION DATE
     */

    @Override
    public Column<OfferDetail, Date> getOfferCreationDateColumn() {
        return offerCreationDateColumn;
    }
    /*
     * @return table column: FINNIDH DATE
     */

    @Override
    public Column<OfferDetail, Date> getOfferFinishDateColumn() {
        return offerFinishDateColumn;
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
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
