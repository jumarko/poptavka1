package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
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
    @UiField SimplePanel contentPanel, footerHolder;
    @UiField Button clientNewDemands, clientOffers, clientAssignedDemands, clientClosedDemands, clientRatings;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Loads right styles to menu buttons.
     * @param loadedWidget - use module constants from class Contants.
     */
    @Override
    public void clientMenuStyleChange(int loadedWidget) {
        switch (loadedWidget) {
            case Constants.CLIENT_DEMANDS:
                clientDemandsMenuStyleChange();
                break;
            case Constants.CLIENT_OFFERED_DEMANDS:
                clientOffersMenuStyleChange();
                break;
            case Constants.CLIENT_ASSIGNED_DEMANDS:
                clientAssignedDemandsMenuStyleChange();
                break;
            case Constants.CLIENT_CLOSED_DEMANDS:
                clientClosedDemandsMenuStyleChange();
                break;
            case Constants.CLIENT_RATINGS:
                clientRatingMenuStyleChange();
                break;
            default:
                removeMenuStyleChange();
                break;
        }
    }

    /**************************************************************************/
    /* Helper methods.                                                        */
    /**************************************************************************/
    private void removeMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
    }

    private void clientDemandsMenuStyleChange() {
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientNewDemands.addStyleName(Constants.ACT);
    }

    private void clientOffersMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientOffers.addStyleName(Constants.ACT);
    }

    private void clientAssignedDemandsMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientAssignedDemands.addStyleName(Constants.ACT);
    }

    private void clientClosedDemandsMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientClosedDemands.addStyleName(Constants.ACT);
    }

    private void clientRatingMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.addStyleName(Constants.ACT);
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
    public Button getClientClosedDemandsButton() {
        return clientClosedDemands;
    }

    @Override
    public Button getClientRatingsButton() {
        return clientRatings;
    }

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public SimplePanel getFooterHolder() {
        return footerHolder;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
