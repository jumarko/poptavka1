package com.eprovement.poptavka.client.user.clientdemands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientDemandsModuleView extends Composite
        implements ClientDemandsModulePresenter.ClientDemandsLayoutInterface {

    private static ClientDemandsLayoutViewUiBinder uiBinder = GWT.create(ClientDemandsLayoutViewUiBinder.class);

    interface ClientDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientDemandsModuleView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button clientNewDemands, clientOffers, clientAssignedDemands;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getClientNewDemandsButton() {
        return clientNewDemands;
    }

    @Override
    public Button getClientOffersButton() {
        return clientOffers;
    }

    @Override
    public Button getClientAssignedDemandsButton() {
        return clientAssignedDemands;
    }

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
