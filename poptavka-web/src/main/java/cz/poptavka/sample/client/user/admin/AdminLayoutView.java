package cz.poptavka.sample.client.user.admin;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class AdminLayoutView extends Composite
        implements AdminLayoutPresenter.AdminLayoutInterface, StyleInterface {

    private static AdminLayoutViewUiBinder uiBinder = GWT.create(AdminLayoutViewUiBinder.class);

    interface AdminLayoutViewUiBinder extends UiBinder<Widget, AdminLayoutView> {
    }
    private static final Logger LOGGER = Logger.getLogger(AdminLayoutView.class.getName());
    @UiField
    SimplePanel contentPanel;
    @UiField
    Hyperlink demandsLink, offersLink, clientsLink, suppliersLink,
    accessRolesLink, emailActivationsLink, invoicesLink, messagesLink,
    ourPaymentDetailsLink, paymentMethodsLink, permissionsLink,
    preferencesLink, problemsLink;

    public AdminLayoutView() {
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
    public void setDemandsToken(String linkString) {
        demandsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setClientsToken(String linkString) {
        clientsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setOffersToken(String linkString) {
        offersLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setSuppliersToken(String linkString) {
        suppliersLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setAccessRoleToken(String linkString) {
        accessRolesLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setEmailActivationToken(String linkString) {
        emailActivationsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setInvoiceToken(String linkString) {
        invoicesLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setMessageToken(String linkString) {
        messagesLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setOurPaymentDetailsToken(String linkString) {
        ourPaymentDetailsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setPaymentMethodToken(String linkString) {
        paymentMethodsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setPermissionToken(String linkString) {
        permissionsLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setPreferenceToken(String linkString) {
        preferencesLink.setTargetHistoryToken(linkString);
    }

    @Override
    public void setProblemToken(String linkString) {
        problemsLink.setTargetHistoryToken(linkString);
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
//                myDemandsOperatorLink.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
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
