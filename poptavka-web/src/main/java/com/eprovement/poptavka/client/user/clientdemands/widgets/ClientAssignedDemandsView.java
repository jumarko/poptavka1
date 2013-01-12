package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientAssignedDemandsView extends Composite
        implements ClientAssignedDemandsPresenter.ClientAssignedDemandsLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientAssignedDemandsLayoutViewUiBinder uiBinder =
            GWT.create(ClientAssignedDemandsLayoutViewUiBinder.class);

    interface ClientAssignedDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientAssignedDemandsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField(provided = true) UniversalTableGrid dataGrid;
    @UiField UniversalPagerWidget pager;
    @UiField SimplePanel wrapperPanel;
    @UiField HorizontalPanel toolBar;
    @UiField ListBox actionBox;
    @UiField Label tableNameLabel;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTable();
        initWidget(uiBinder.createAndBindUi(this));

        tableNameLabel.setText(Storage.MSGS.clientAssignedDemandsTitle());
    }

    private void initTable() {
        pager = new UniversalPagerWidget();
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalTableGrid(
                ClientOfferedDemandOffersDetail.KEY_PROVIDER,
                Constants.CLIENT_ASSIGNED_DEMANDS,
                pager.getPageSize(),
                resource);
        pager.setDisplay(dataGrid);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalTableGrid getDataGrid() {
        return dataGrid;
    }

    @Override
    public SimplePager getPager() {
        return pager.getPager();
    }

    @Override
    public ListBox getActionBox() {
        return actionBox;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
