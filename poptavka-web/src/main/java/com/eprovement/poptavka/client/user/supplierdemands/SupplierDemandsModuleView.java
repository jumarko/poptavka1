package com.eprovement.poptavka.client.user.supplierdemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierDemandsModuleView extends Composite
        implements SupplierDemandsModulePresenter.SupplierDemandsLayoutInterface {

    private static SupplierDemandsLayoutViewUiBinder uiBinder = GWT.create(SupplierDemandsLayoutViewUiBinder.class);

    interface SupplierDemandsLayoutViewUiBinder extends UiBinder<Widget, SupplierDemandsModuleView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button supplierNewDemands, supplierOffers, supplierAssignedDemands, supplierClosedDemands, supplierRatings;

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
    public Button getSupplierClosedDemandsButton() {
        return supplierClosedDemands;
    }

    @Override
    public Button getSupplierRatingsButton() {
        return supplierRatings;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
