/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DataGrid;
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
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.type.OfferStateType;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminOffersView extends Composite implements AdminOffersPresenter.AdminOffersInterface {

    private static AdministrationViewUiBinder uiBinder = GWT.create(AdministrationViewUiBinder.class);

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdminOffersView> {
    }
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField Button commit, rollback, refresh;
    @UiField Label changesLabel;
    // PAGER
    @UiField(provided = true) SimplePager pager;
    @UiField(provided = true) ListBox pageSizeCombo;
    // DETAIL
//    @UiField AdminOfferInfoView adminOfferDetail;
    // TABLE
    @UiField(provided = true) DataGrid<OfferDetail> dataGrid;
    private SingleSelectionModel<OfferDetail> selectionModel;
    // Editable Columns
    private Column<OfferDetail, String> priceColumn;
    private Column<OfferDetail, String> offerStatusColumn;
    private Column<OfferDetail, Date> offerCreationDateColumn;
    private Column<OfferDetail, Date> offerFinishDateColumn;

    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);

    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.currencyFormat());
    // The key provider that provides the unique ID of a DemandDetail.
    private static final ProvidesKey<OfferDetail> KEY_PROVIDER = new ProvidesKey<OfferDetail>() {

        @Override
        public Object getKey(OfferDetail item) {
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
        // Create a dataGrid.
        GWT.log("Admin Offers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<OfferDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Add a selection model to handle user selection.
        selectionModel = new SingleSelectionModel<OfferDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<OfferDetail>createCheckboxManager());

        // Initialize the columns.
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Offer ID
        addColumn(new TextCell(), "OID", true, 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getId());
            }
        });
        // Demand ID.
        addColumn(new TextCell(), "DID", true, 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getDemandId());
            }
        });
        // Supplier ID
        addColumn(new TextCell(), "SID", true, 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getSupplierId());
            }
        });
        priceColumn = addColumn(new EditTextCell(), "Price", true, 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return currencyFormat.format(offerDetail.getPrice());
            }
        });

        // OfferStatus.
        ArrayList<String> stateList = new ArrayList<String>();
        for (OfferStateType offerStatusDetail : OfferStateType.values()) {
            stateList.add(offerStatusDetail.getValue());
        }
        offerStatusColumn = addColumn(new SelectionCell(stateList), "State", true, 60, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return offerDetail.getState();
            }
        });

        // Creation date
        offerCreationDateColumn = addColumn(new DateCell(), "Created", false, 60, new GetValue<Date>() {

            @Override
            public Date getValue(OfferDetail offerDetail) {
                return offerDetail.getFinishDate(); //TODO Martin - dorobit ceration date
            }
        });

        // Demand end date.
        offerFinishDateColumn = addColumn(new DateCell(), "Finnish", true, 60, new GetValue<Date>() {

            @Override
            public Date getValue(OfferDetail offerDetail) {
                return offerDetail.getFinishDate();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

        C getValue(OfferDetail offerDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<OfferDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<OfferDetail, C> column = new Column<OfferDetail, C>(cell) {

            @Override
            public C getValue(OfferDetail object) {
                return getter.getValue(object);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
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
    public DataGrid<OfferDetail> getDataGrid() {
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

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<OfferDetail> getSelectionModel() {
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
