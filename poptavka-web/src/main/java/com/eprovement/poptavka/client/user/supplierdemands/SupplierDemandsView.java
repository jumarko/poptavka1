package com.eprovement.poptavka.client.user.supplierdemands;


import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


public class SupplierDemandsView extends Composite
    implements SupplierDemandsPresenter.SupplierDemandsLayoutInterface {

    private static SupplierDemandsLayoutViewUiBinder uiBinder = GWT.create(SupplierDemandsLayoutViewUiBinder.class);

    interface SupplierDemandsLayoutViewUiBinder extends UiBinder<Widget, SupplierDemandsView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;
    @UiField Button supNewDemands, supOffers, supAssignedDemands,
    supCreateDemand, supCreateSupplier, allDemands, allSuppliers;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
