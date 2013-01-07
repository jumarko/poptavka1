package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalPagerWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
    @UiField(provided = true) UniversalTableWidget tableWidget;
    @UiField ListBox actionBox;
    @UiField UniversalPagerWidget pager;
    @UiField SimplePanel detailPanel;
    @UiField HorizontalPanel toolBar;
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

        tableNameLabel.setText(Storage.MSGS.supplierPotentialDemandsTableTitle());
    }

    private void initTable() {
        pager = new UniversalPagerWidget();
        tableWidget = new UniversalTableWidget(Constants.SUPPLIER_POTENTIAL_DEMANDS, pager.getPageSize());
        pager.setDisplay(tableWidget.getGrid());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public UniversalTableWidget getTableWidget() {
        return tableWidget;
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
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
