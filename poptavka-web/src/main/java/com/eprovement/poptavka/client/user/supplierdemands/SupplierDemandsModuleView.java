package com.eprovement.poptavka.client.user.supplierdemands;

import com.eprovement.poptavka.client.common.session.Constants;
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
    @UiField SimplePanel contentPanel;
    @UiField Button supplierDemands, supplierOffers, supplierAssignedDemands, supplierClosedDemands, supplierRatings;

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
    private void removeMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
    }

    private void supplierDemandsMenuStyleChange() {
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierDemands.addStyleName(Constants.ACT);
    }

    private void supplierOffersMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierOffers.addStyleName(Constants.ACT);
    }

    private void supplierAssignedDemandsMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierClosedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierAssignedDemands.addStyleName(Constants.ACT);
    }

    private void supplierClosedDemandsMenuStyleChange() {
        supplierDemands.removeStyleName(Constants.ACT);
        supplierOffers.removeStyleName(Constants.ACT);
        supplierAssignedDemands.removeStyleName(Constants.ACT);
        supplierRatings.removeStyleName(Constants.ACT);
        supplierClosedDemands.addStyleName(Constants.ACT);
    }

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
    @Override
    public void setContent(IsWidget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getSupplierNewDemandsButton() {
        return supplierDemands;
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
