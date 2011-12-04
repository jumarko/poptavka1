package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class DemandModuleView extends Composite
    implements DemandModulePresenter.DemandsLayoutInterface, StyleInterface {

    private static DemandsLayoutViewUiBinder uiBinder = GWT.create(DemandsLayoutViewUiBinder.class);

    interface DemandsLayoutViewUiBinder extends UiBinder<Widget, DemandModuleView> {
    }

    private static final Logger LOGGER = Logger.getLogger(DemandModuleView.class.getName());

//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField
    SimplePanel contentPanel;


    // CLI menu
    @UiField Anchor cliNewDemands, cliOffers, cliAssignedDemands, cliCreateDemand,
    allDemands, allSuppliers;
    // SUP menu
    @UiField Anchor supNewDemands, supOffers, supAssignedDemands;
    // hidden or displayed according to the role
    @UiField DivElement supMenu;

    //BEHO devel button, delete after work is done

    public DemandModuleView() {
        StyleResource.INSTANCE.common().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public void setContent(Widget contentWidget) {
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
    public Anchor getSupNewDemandsAnchor() {
        return supNewDemands;
    }

    @Override
    public Anchor getCliNewDemandsAnchor() {
        return cliNewDemands;
    }

    @Override
    public Anchor getCliOffersAnchor() {
        return cliOffers;
    }

    @Override
    public Anchor getCliAssignedDemandsAnchor() {
        return cliAssignedDemands;
    }

    @Override
    public Anchor getCliCreateDemand() {
        return cliCreateDemand;
    }

    @Override
    public Anchor getAllDemands() {
        return allDemands;
    }

    @Override
    public Anchor getAllSuppliers() {
        return allSuppliers;
    }

    @Override
    public Anchor getSupOffersAnchor() {
        return supOffers;
    }

    @Override
    public Anchor getSupAssignedAnchor() {
        return supAssignedDemands;
    }
}
