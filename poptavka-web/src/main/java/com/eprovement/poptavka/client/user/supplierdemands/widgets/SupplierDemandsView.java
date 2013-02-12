package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableGrid;
import com.eprovement.poptavka.resources.datagrid.AsyncDataGrid;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.github.gwtbootstrap.client.ui.DropdownButton;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierDemandsView extends Composite
        implements SupplierDemandsPresenter.SupplierDemandsLayoutInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierDemandsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierDemandsLayoutViewUiBinder.class);

    interface SupplierDemandsLayoutViewUiBinder extends UiBinder<Widget, SupplierDemandsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField(provided = true) UniversalTableGrid dataGrid;
    @UiField(provided = true) UniversalPagerWidget pager;
    @UiField DropdownButton actionBox;
    @UiField NavLink actionRead, actionUnread, actionStar, actionUnstar;
    @UiField SimplePanel detailPanel;
    @UiField HorizontalPanel toolBar;
    @UiField Label tableNameLabel;
    /** Class attribute. **/
    private LoadingDiv loadingDiv = new LoadingDiv();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        initTable();
        initWidget(uiBinder.createAndBindUi(this));

        tableNameLabel.setText(Storage.MSGS.supplierPotentialDemandsTableTitle());
    }

    private void initTable() {
        pager = new UniversalPagerWidget();
        DataGrid.Resources resource = GWT.create(AsyncDataGrid.class);
        dataGrid = new UniversalTableGrid(
                SupplierPotentialDemandDetail.KEY_PROVIDER,
                Constants.SUPPLIER_POTENTIAL_DEMANDS,
                pager.getPageSize(),
                resource);
        dataGrid.setWidth("100%");
        dataGrid.setHeight("100%");
        pager.setDisplay(dataGrid);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void loadingDivShow(Widget holderWidget) {
        GWT.log("  - loading div created");
        if (loadingDiv == null) {
            loadingDiv = new LoadingDiv();
        }
        holderWidget.getElement().appendChild(loadingDiv.getElement());
    }

    @Override
    public void loadingDivHide(Widget holderWidget) {
        GWT.log("  - loading div removed");
        if (holderWidget.getElement().isOrHasChild(loadingDiv.getElement())) {
            holderWidget.getElement().removeChild(loadingDiv.getElement());
        }
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

    //Action Box
    @Override
    public DropdownButton getActionBox() {
        return actionBox;
    }

    @Override
    public NavLink getActionRead() {
        return actionRead;
    }

    @Override
    public NavLink getActionUnread() {
        return actionUnread;
    }

    @Override
    public NavLink getActionStar() {
        return actionStar;
    }

    @Override
    public NavLink getActionUnstar() {
        return actionUnstar;
    }

    //Widget view
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
