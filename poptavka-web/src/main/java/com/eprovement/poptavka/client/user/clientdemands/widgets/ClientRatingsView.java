package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import java.util.Arrays;
import java.util.List;

public class ClientRatingsView extends Composite
        implements ClientRatingsPresenter.ClientRatingsLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientRatingsLayoutViewUiBinder uiBinder =
            GWT.create(ClientRatingsLayoutViewUiBinder.class);

    interface ClientRatingsLayoutViewUiBinder extends UiBinder<Widget, ClientRatingsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField SimplePanel wrapperPanel;
    @UiField HorizontalPanel toolBar;
    @UiField Label tableNameLabel;
    /** Class attributes. **/
    private static final List<String> GRID_COLUMNS = Arrays.asList(
            new String[]{
                "createdDate", "title", "locality", "endDate"
            });

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTable();
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void initTable() {
        pager = new UniversalPagerWidget();
        dataGrid = new UniversalAsyncGrid<DemandRatingsDetail>(DemandRatingsDetail.KEY_PROVIDER, GRID_COLUMNS);
        dataGrid.setSelectionModel(new SingleSelectionModel<FullDemandDetail>());
        dataGrid.setPageSize(pager.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");

        pager.setDisplay(dataGrid);

        initTableColumns();
    }

    private void initTableColumns() {
        // Demand title
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnDemandTitle(),
                true, Constants.COL_WIDTH_TITLE,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return ((DemandRatingsDetail) object).getDemandTitle();
                    }
                });
        // Client's rating
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnRatingClient(),
                true, Constants.COL_WIDTH_RATING,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Integer.toString(((DemandRatingsDetail) object).getRatingClient());
                    }
                });
        // Supplier's rating
        /**************************************************************************/
        dataGrid.addColumn(new TextCell(), Storage.MSGS.columnRatingSupplier(),
                true, Constants.COL_WIDTH_RATING,
                new UniversalAsyncGrid.GetValue<String>() {
                    @Override
                    public String getValue(Object object) {
                        return Integer.toString(((DemandRatingsDetail) object).getRatingSupplier());
                    }
                });

    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalAsyncGrid getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    //Widget view
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
