package com.eprovement.poptavka.client.user.clientdemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientDemandsView extends Composite
        implements ClientDemandsPresenter.ClientDemandsLayoutInterface {

    private static ClientDemandsLayoutViewUiBinder uiBinder = GWT.create(ClientDemandsLayoutViewUiBinder.class);

    interface ClientDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientDemandsView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button cliNewDemands, cliOffers, cliAssignedDemands, cliCreateDemand,
    allDemands, allSuppliers, cliCreateSupplier;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
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
    public Button getCliNewDemandsButton() {
        return cliNewDemands;
    }

    @Override
    public Button getCliOffersButton() {
        return cliOffers;
    }

    @Override
    public Button getCliAssignedDemandsButton() {
        return cliAssignedDemands;
    }

    @Override
    public Button getCliCreateDemand() {
        return cliCreateDemand;
    }

    @Override
    public Button getCliCreateSupplier() {
        return cliCreateSupplier;
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
