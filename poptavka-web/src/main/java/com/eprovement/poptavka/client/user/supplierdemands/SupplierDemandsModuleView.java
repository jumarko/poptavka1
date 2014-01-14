/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.supplierdemands.interfaces.ISupplierDemands;
import com.eprovement.poptavka.client.user.supplierdemands.toolbar.SupplierToolbarView;
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
 * View consists of left vertical menu and content area for placing SupplierDmenads module's widgets.
 *
 * @author Martin Slavkovsky
 */
public class SupplierDemandsModuleView extends Composite implements ISupplierDemands.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierDemandsLayoutViewUiBinder uiBinder = GWT.create(SupplierDemandsLayoutViewUiBinder.class);

    interface SupplierDemandsLayoutViewUiBinder extends UiBinder<Widget, SupplierDemandsModuleView> {
    }

    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentContainer;
    @UiField Button supplierDemands, supplierOffers, supplierAssignedDemands, supplierClosedDemands, supplierRatings;
    /** Class attribute. **/
    @Inject
    private SupplierToolbarView toolbar;


    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates SupplierDemandsModule view's compotents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Style change methods.                                                  */
    /**************************************************************************/
    /**
     * Sets active style to menu buttons styles.
     * @param loadedWidget id
     */
    @Override
    public void supplierMenuStyleChange(int loadedWidget) {
        switch (loadedWidget) {
            case Constants.SUPPLIER_POTENTIAL_DEMANDS:
                supplierDemandsMenuStyleChange();
                break;
            case Constants.SUPPLIER_OFFERS:
                supplierOffersMenuStyleChange();
                break;
            case Constants.SUPPLIER_ASSIGNED_DEMANDS:
                supplierAssignedDemandsMenuStyleChange();
                break;
            case Constants.SUPPLIER_CLOSED_DEMANDS:
                supplierClosedDemandsMenuStyleChange();
                break;
            case Constants.SUPPLIER_RATINGS:
                supplierRatingMenuStyleChange();
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
     * Remove all active styles from menu buttons.
     */
    private void removeMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for potential demands menu button.
     */
    private void supplierDemandsMenuStyleChange() {
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for offers menu button.
     */
    private void supplierOffersMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierOffers.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for assigend demands menu button.
     */
    private void supplierAssignedDemandsMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierAssignedDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for closed demands menu button.
     */
    private void supplierClosedDemandsMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierClosedDemands.addStyleName(Constants.ACT);
    }

    /**
     * Sets active style for ratings menu button.
     */
    private void supplierRatingMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.addStyleName(Constants.ACT);
    }


    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets given widget to content area.
     * @param contentWidget to set
     */
    @Override
    public void setContent(IsWidget contentWidget) {
        contentContainer.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the potential demands menu button
     */
    @Override
    public Button getSupplierNewDemandsButton() {
        return supplierDemands;
    }

    /**
     * @return the offers menu button
     */
    @Override
    public Button getSupplierOffersButton() {
        return supplierOffers;
    }

    /**
     * @return the assigned demands menu button
     */
    @Override
    public Button getSupplierAssignedDemandsButton() {
        return supplierAssignedDemands;
    }

    /**
     * @return the closed demands menu button
     */
    @Override
    public Button getSupplierClosedDemandsButton() {
        return supplierClosedDemands;
    }

    /**
     * @return the ratings menu button
     */
    @Override
    public Button getSupplierRatingsButton() {
        return supplierRatings;
    }

    /**
     * @return the SupplierToolabrView
     */
    @Override
    public SupplierToolbarView getToolbarContent() {
        return toolbar;
    }

    /**
     * @return the widget view
     */
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
