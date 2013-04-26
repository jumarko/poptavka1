package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;
import java.util.List;

public class ClientOffersView extends Composite
        implements ClientOffersPresenter.ClientOffersLayoutInterface {

    private static ClientOffersLayoutViewUiBinder uiBinder = GWT.create(ClientOffersLayoutViewUiBinder.class);

    interface ClientOffersLayoutViewUiBinder extends UiBinder<Widget, ClientOffersView> {
    }
    /**************************************************************************/
    /* OfferedDemandsTable Attributes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalAsyncGrid<ClientDemandDetail> demandGrid;
    /**************************************************************************/
    /* DemandOfferTable Attrinbutes                                           */
    /**************************************************************************/
    @UiField(provided = true) UniversalTableGrid offerGrid;
    /**************************************************************************/
    /* Other Attrinbutes                                                      */
    /**************************************************************************/
    /** UiFieds. **/
    @UiField(provided = true) UniversalPagerWidget demandPager, offerPager;
    @UiField Button backBtn, acceptBtn;
    @UiField SimplePanel detailPanel, actionBox;
    @UiField Label offerTableTitleLabel;
    @UiField HorizontalPanel demandHeader, offerHeader, offerToolBar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initDemandTable();
        initOfferTable();
        initWidget(uiBinder.createAndBindUi(this));

        setOfferTableVisible(false);
    }

    /**
     * Initialize this example.
     */
    private void initDemandTable() {
        // Create a Pager.
        demandPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        demandGrid = new UniversalAsyncGrid<ClientDemandDetail>(
                initSort(), demandPager.getPageSize(), resource);
        demandGrid.setWidth("100%");
        demandGrid.setHeight("100%");
        // Selection Model - must define different from default which is used in UniversalAsyncGrid
        // Add a selection model so we can select cells.
        final SelectionModel<ClientDemandDetail> selectionModel =
                new SingleSelectionModel<ClientDemandDetail>(ClientDemandDetail.KEY_PROVIDER);
        demandGrid.setSelectionModel(selectionModel);

        // Bind pager to Table.
        demandPager.setDisplay(demandGrid);

        initDemandTableColumns();
    }

    /**
     * Initialize this example.
     */
    private void initOfferTable() {
        // Create a Pager.
        offerPager = new UniversalPagerWidget();
        // Create a DataGrid.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        offerGrid = new UniversalTableGrid(
                ClientOfferedDemandOffersDetail.KEY_PROVIDER,
                Constants.CLIENT_OFFERED_DEMANDS,
                offerPager.getPageSize(),
                resource);
        offerGrid.setWidth("100%");
        offerGrid.setHeight("100%");
        // bind pager to grid
        offerPager.setDisplay(offerGrid);
    }

    private SortDataHolder initSort() {
        List<SortPair> sortColumns = Arrays.asList(
                new SortPair(DemandField.TITLE),
                new SortPair(DemandField.PRICE),
                new SortPair(DemandField.END_DATE),
                new SortPair(DemandField.VALID_TO));
        List<SortPair> defaultSort = Arrays.asList(
                new SortPair(DemandField.VALID_TO));
        return new SortDataHolder(defaultSort, sortColumns);
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand title column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        ClientDemandDetail detail = (ClientDemandDetail) object;
                        str.append(detail.getDemandTitle());
                        if (detail.getUnreadSubmessagesCount() > 0) {
                            str.append(" (");
                            str.append(detail.getUnreadSubmessagesCount());
                            str.append(")");
                        }
                        return str.toString();
                    }
                });

        // Demand price column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnPrice(),
                true, Constants.COL_WIDTH_PRICE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.CURRENCY_FORMAT.format(((ClientDemandDetail) object).getPrice());
                    }
                });

        // End date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.columnEndDate(),
                true, Constants.COL_WIDTH_DATE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Storage.DATE_FORMAT_SHORT.format(((ClientDemandDetail) object).getEndDate());
                    }
                });

        // Urgency (Valid-to date) column
        demandGrid.addUrgentColumn();
    }

    /**
     * Create all columns to the grid.
     */
    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table
    @Override
    public UniversalAsyncGrid<ClientDemandDetail> getDemandGrid() {
        return demandGrid;
    }

    @Override
    public UniversalTableGrid getOfferGrid() {
        return offerGrid;
    }

    //Pager
    @Override
    public SimplePager getDemandPager() {
        return demandPager.getPager();
    }

    @Override
    public SimplePager getOfferPager() {
        return offerPager.getPager();
    }

    //Buttons
    @Override
    public Button getBackBtn() {
        return backBtn;
    }

    @Override
    public Button getAcceptBtn() {
        return acceptBtn;
    }

    /** Panels. **/
    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public SimplePanel getActionBox() {
        return actionBox;
    }

    @Override
    public HorizontalPanel getOfferToolBar() {
        return offerToolBar;
    }

    /** Others. **/
    @Override
    public IsWidget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setDemandTableVisible(boolean visible) {
        demandGrid.setVisible(visible);
        demandGrid.redraw();
        if (!visible) {
            SingleSelectionModel selectionModel = (SingleSelectionModel) demandGrid.getSelectionModel();
            if (selectionModel.getSelectedObject() != null) {
                selectionModel.setSelected(selectionModel.getSelectedObject(), false);
            }
        }
        demandHeader.setVisible(visible);
    }

    @Override
    public void setOfferTableVisible(boolean visible) {
        offerGrid.setVisible(visible);
        offerGrid.redraw();
        offerHeader.setVisible(visible);
        offerToolBar.setVisible(visible);
        offerTableTitleLabel.setVisible(visible);
        backBtn.setVisible(visible);
    }

    @Override
    public void setOfferTitleLabel(String text) {
        offerTableTitleLabel.setText(text);
    }
}
