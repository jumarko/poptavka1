package com.eprovement.poptavka.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.StyleInterface;
import com.eprovement.poptavka.shared.domain.UserDetail.Role;

public class DemandView extends Composite
    implements DemandPresenter.DemandsLayoutInterface, StyleInterface {

    private static DemandsLayoutViewUiBinder uiBinder = GWT.create(DemandsLayoutViewUiBinder.class);

    interface DemandsLayoutViewUiBinder extends UiBinder<Widget, DemandView> {
    }

    private static final Logger LOGGER = Logger.getLogger(DemandView.class.getName());

//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField
    SimplePanel contentPanel;


    // CLI menu
    @UiField Button cliNewDemands, cliOffers, cliAssignedDemands, cliCreateDemand,
    allDemands, allSuppliers, cliCreateSupplier;
    // SUP menu
    @UiField Button supNewDemands, supOffers, supAssignedDemands;
    // hidden or displayed according to the role
    @UiField DivElement supMenu;

    //BEHO devel button, delete after work is done

    public DemandView() {
        //Zaisti pridanie stylu - zeleny backgroud + horizontalne menu
        StyleResource.INSTANCE.layout().ensureInjected();
        //Zaisti pridanie styl - pre nastavenie velkosti body po prohlaseni
        StyleResource.INSTANCE.common().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setContent(IsWidget contentWidget) {
        contentPanel.setWidget(contentWidget);
    }

    /** toggle visible actions/buttons for current user decided by his role. **/
    @Override
    public void setRoleInterface(Role role) {
        LOGGER.fine("Set User style for role " + role.toString());
        switch (role) {
            case SUPPLIER:
                    //cascade, include client below, because supplier is always client too
                supMenu.getStyle().setDisplay(Display.BLOCK);
            case CLIENT:
//                administration.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
//                myDemandsOperatorLink.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
                break;
            default:
                break;
        }
    }
/**
    @Override
    public void setPotentialDemandsToken(String link) {
        supDemandsLink.setTargetHistoryToken(link);
    }
*/
    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public Button getSupNewDemandsButton() {
        return supNewDemands;
    }

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
    public Button getSupOffersButton() {
        return supOffers;
    }

    @Override
    public Button getSupAssignedButton() {
        return supAssignedDemands;
    }
}
