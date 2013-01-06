package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
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
    //table column width constatnts
    private static final int TITLE_COL_WIDTH = 50;
    private static final int PRICE_COL_WIDTH = 30;
    private static final int FINNISH_DATE_COL_WIDTH = 30;
    private static final int VALID_TO_DATE_COL_WIDTH = 30;
    //pager definition
    @UiField(provided = true)
    UniversalPagerWidget demandPager;
    /**************************************************************************/
    /* DemandOfferTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalTableWidget contestGrid;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //TODO Martin - ako i18n format datumu?
    private DateTimeFormat formatter = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_SHORT);
    //table handling buttons
    @UiField
    Button backBtn;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label demandTitlelabel;
    @UiField
    HorizontalPanel demandHeader, contestHeader;

    /**************************************************************************/
    /* Initialization                                                            */
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
        List<String> gridColumns = Arrays.asList(
                new String[]{
                    "title", "price", "finnishDate", "validTo"
                });
        // Create a Pager.
        demandPager = new UniversalPagerWidget();
        // Create a Table.
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        demandGrid = new UniversalAsyncGrid<ClientDemandDetail>(
                gridColumns, demandPager.getPageSize(), resource);
        demandGrid.setWidth("800px");
        demandGrid.setHeight("500px");
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
        // Create a CellTable.
        contestGrid = new UniversalTableWidget(Constants.CLIENT_OFFERED_DEMANDS);
    }

    /**
     * Create all columns to the grid.
     */
    public void initDemandTableColumns() {
        // Demand title column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.title(), true, TITLE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
                                clientDetail.getDemandTitle(),
                                clientDetail.getUnreadSubmessages());
                    }
                });

        // Demand price column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.price(), true, PRICE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
                                clientDetail.getPrice(),
                                clientDetail.getUnreadSubmessages());
                    }
                });

        // Finnish date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.finnishDate(), true, FINNISH_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
                                formatter.format(clientDetail.getEndDate()),
                                clientDetail.getUnreadSubmessages());
                    }
                });

        // Valid-to date column
        demandGrid.addColumn(
                demandGrid.TABLE_CLICKABLE_TEXT_CELL, Storage.MSGS.validTo(), true, VALID_TO_DATE_COL_WIDTH,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        ClientDemandDetail clientDetail = (ClientDemandDetail) object;
                        return ClientDemandDetail.displayHtml(
                                formatter.format(clientDetail.getValidToDate()),
                                clientDetail.getUnreadSubmessages());
                    }
                });
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
    public UniversalTableWidget getOfferGrid() {
        return contestGrid;
    }

    //Pager
    @Override
    public SimplePager getDemandPager() {
        return demandPager.getPager();
    }

    @Override
    public SimplePager getOfferPager() {
        return contestGrid.getPager();
    }

    //Buttons
    @Override
    public Button getBackBtn() {
        return backBtn;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

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
//        demandGrid.redraw();
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
        contestGrid.setVisible(visible);
        contestGrid.setSize("600px", "800px");
//        contestGrid.getGrid().redraw();
        contestHeader.setVisible(visible);
    }

    @Override
    public void setDemandTitleLabel(String text) {
        demandTitlelabel.setText(text);
    }
}
