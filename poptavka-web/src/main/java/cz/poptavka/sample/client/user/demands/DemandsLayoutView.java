package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class DemandsLayoutView extends Composite
    implements DemandsLayoutPresenter.DemandsLayoutInterface, StyleInterface {

    private static DemandsLayoutViewUiBinder uiBinder = GWT.create(DemandsLayoutViewUiBinder.class);

    interface DemandsLayoutViewUiBinder extends UiBinder<Widget, DemandsLayoutView> {
    }

    private static final Logger LOGGER = Logger.getLogger(DemandsLayoutView.class.getName());

//    @UiField Button myDemandsBtn, offersBtn, createDemandBtn;
    @UiField
    SimplePanel contentPanel;


    // CLI menu
    @UiField Hyperlink cliDemandsLink, cliOffersLink, cliAssignedDemandsLink,
    newDemandLink, allDemandsLink, allSuppliersLink;
    // SUP menu
    @UiField Hyperlink supDemandsLink, supOffersLink, supAssignedDemandsLink;
    // hidden or displayed according to the role
    @UiField DivElement supMenu;


    public DemandsLayoutView() {
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

    @Override
    public void setMyDemandsToken(String linkString) {
        cliDemandsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setOffersToken(String linkString) {
        cliOffersLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setNewDemandToken(String linkString) {
        newDemandLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setAllDemandsToken(String linkString) {
        allDemandsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setAllSuppliersToken(String linkString) {
        allSuppliersLink.setTargetHistoryToken(linkString);
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

    @Override
    public void setPotentialDemandsToken(String link) {
        supDemandsLink.setTargetHistoryToken(link);
    }

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }
}
