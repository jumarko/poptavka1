/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.clientdemands.toolbar.ClientToolbarView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * ClientDemands view consists of left vertical menu and content for ClientDemands widgets.
 *
 * @author Martin Slavkovsky
 */
public class ClientDemandsModuleView extends Composite
        implements ClientDemandsModulePresenter.ClientDemandsViewInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    private static ClientDemandsLayoutViewUiBinder uiBinder = GWT.create(ClientDemandsLayoutViewUiBinder.class);

    interface ClientDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientDemandsModuleView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField SimplePanel contentContainer;
    @UiField Button clientNewDemands, clientOffers, clientAssignedDemands, clientClosedDemands, clientRatings;
    /** Class attribute. **/
    @Inject
    private ClientToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ClientDemandsModule view's compontents.
     */
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
    /**
     * Clears active style from menu styles.
     */
    private void removeMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for newDemands menu button.
     */
    private void clientDemandsMenuStyleChange() {
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientNewDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for offers menu button.
     */
    private void clientOffersMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientOffers.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for assinedDemands menu button.
     */
    private void clientAssignedDemandsMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientClosedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientAssignedDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for closedDemands menu button.
     */
    private void clientClosedDemandsMenuStyleChange() {
        clientNewDemands.removeStyleName(Constants.ACT);
        clientOffers.removeStyleName(Constants.ACT);
        clientAssignedDemands.removeStyleName(Constants.ACT);
        clientRatings.removeStyleName(Constants.ACT);
        clientClosedDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for rating menu button.
     */
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
    /**
     * @return the new demands button.
     */
    @Override
    public Button getClientNewDemandsButton() {
        return clientNewDemands;
    }

    /**
     * @return the offers button
     */
    @Override
    public Button getClientOffersButton() {
        return clientOffers;
    }

    /**
     * @return the assigned demands button
     */
    @Override
    public Button getClientAssignedDemandsButton() {
        return clientAssignedDemands;
    }

    /**
     * @return the closed demands button
     */
    @Override
    public Button getClientClosedDemandsButton() {
        return clientClosedDemands;
    }

    /**
     * @return the rating button
     */
    @Override
    public Button getClientRatingsButton() {
        return clientRatings;
    }

    /**
     * @return the ClientToolbar view
     */
    @Override
    public ClientToolbarView getToolbarContent() {
        return toolbar;
    }

    /**
     * @return the content container
     */
    @Override
    public SimplePanel getContentContainer() {
        return contentContainer;
    }

    /**
     * @return the widget view
     */
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
