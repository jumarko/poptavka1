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
    Button supNewDemands, supOffers, supAssignedDemands,
    supCreateDemand, supCreateSupplier, allDemands, allSuppliers;

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
    public Button getSupNewDemandsButton() {
        return supNewDemands;
    }

    @Override
    public Button getSupOffersButton() {
        return supOffers;
    }

    @Override
    public Button getSupAssignedDemandsButton() {
        return supAssignedDemands;
    }

    @Override
    public Button getSupCreateDemand() {
        return supCreateDemand;
    }

    @Override
    public Button getSupCreateSupplier() {
        return supCreateSupplier;
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
