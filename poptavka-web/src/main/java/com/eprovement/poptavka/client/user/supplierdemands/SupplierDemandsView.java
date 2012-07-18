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
    @UiField
    Button supplierNewDemands, supplierOffers, supplierAssignedDemands,
    supplierCreateDemand, supplierCreateSupplier, allDemands, allSuppliers;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setContent(IsWidget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getSupplierNewDemandsButton() {
        return supplierNewDemands;
    }

    @Override
    public Button getSupplierOffersButton() {
        return supplierOffers;
    }

    @Override
    public Button getSupplierAssignedDemandsButton() {
        return supplierAssignedDemands;
    }

    @Override
    public Button getSupplierCreateDemand() {
        return supplierCreateDemand;
    }

    @Override
    public Button getSupplierCreateSupplier() {
        return supplierCreateSupplier;
    }

    @Override
    public Button getAllDemands() {
        return allDemands;
    }

    @Override
    public Button getAllSuppliers() {
        return allSuppliers;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
