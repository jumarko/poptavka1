package cz.poptavka.sample.client.user.admin;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class AdminModuleView extends Composite
        implements AdminModulePresenter.AdminModuleInterface, StyleInterface {

    private static AdminModuleViewUiBinder uiBinder = GWT.create(AdminModuleViewUiBinder.class);

    interface AdminModuleViewUiBinder extends UiBinder<Widget, AdminModuleView> {
    }
    private static final Logger LOGGER = Logger.getLogger(AdminModuleView.class.getName());
    @UiField
    SimplePanel contentPanel;
    @UiField
    Anchor demandsAnchor, offersAnchor, clientsAnchor, suppliersAnchor,
    accessRolesAnchor, emailActivationsAnchor, invoicesAnchor, messagesAnchor,
    paymentMethodsAnchor, permissionsAnchor, preferencesAnchor, problemsAnchor; //ourPaymentDetailsAnchor,

    public AdminModuleView() {
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
    public Anchor getDemandsAnchor() {
        return demandsAnchor;
    }

    @Override
    public Anchor getClientsAnchor() {
        return clientsAnchor;
    }

    @Override
    public Anchor getOffersAnchor() {
        return offersAnchor;
    }

    @Override
    public Anchor getSuppliersAnchor() {
        return suppliersAnchor;
    }

    @Override
    public Anchor getAccessRoleAnchor() {
        return accessRolesAnchor;
    }

    @Override
    public Anchor getEmailActivationAnchor() {
        return emailActivationsAnchor;
    }

    @Override
    public Anchor getInvoiceAnchor() {
        return invoicesAnchor;
    }

    @Override
    public Anchor getMessageAnchor() {
        return messagesAnchor;
    }

//    @Override
//    public Anchor getOurPaymentDetailsAnchor() {
//        ourPaymentDetailsAnchor.getTargetHistoryAnchor(AnchorString);
//    }

    @Override
    public Anchor getPaymentMethodAnchor() {
        return paymentMethodsAnchor;
    }

    @Override
    public Anchor getPermissionAnchor() {
        return permissionsAnchor;
    }

    @Override
    public Anchor getPreferenceAnchor() {
        return preferencesAnchor;
    }

    @Override
    public Anchor getProblemAnchor() {
        return problemsAnchor;
    }

    /** toggle visible actions/buttons for current user decided by his role. **/
    @Override
    public void setRoleInterface(Role role) {
        LOGGER.fine("Set User style for role " + role.toString());
        switch (role) {
            case SUPPLIER:
            //cascade, include client below, because supplier is always client too
            case CLIENT:
//                administration.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
//                myDemandsOperatorAnchor.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
                break;
            default:
                break;
        }
    }

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }
}
