package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierAssignedDemandsView extends Composite
        implements SupplierAssignedDemandsPresenter.SupplierAssignedDemandsLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierAssignedDemandsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierAssignedDemandsLayoutViewUiBinder.class);

    interface SupplierAssignedDemandsLayoutViewUiBinder extends UiBinder<Widget, SupplierAssignedDemandsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField(provided = true) UniversalTableGrid dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField SimplePanel detailPanel, actionBox;
    @UiField HTMLPanel toolBar;
    @UiField Label tableNameLabel;
    @UiField Button finnishBtn;

    /**************************************************************************/
    /* Initialization                                                            */
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
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalTableGrid(
                SupplierOffersDetail.KEY_PROVIDER,
                Constants.SUPPLIER_ASSIGNED_DEMANDS,
                pager.getPageSize(),
                resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
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
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public Button getFinnishBtn() {
        return finnishBtn;
    }

    @Override
    public SimplePanel getActionBox() {
        return actionBox;
    }

    //Widget view
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}