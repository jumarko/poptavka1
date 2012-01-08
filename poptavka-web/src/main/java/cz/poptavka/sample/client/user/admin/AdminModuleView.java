package cz.poptavka.sample.client.user.admin;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

public class AdminModuleView extends Composite implements AdminModulePresenter.AdminModuleInterface, StyleInterface {

    private static AdminModuleViewUiBinder uiBinder = GWT.create(AdminModuleViewUiBinder.class);

    interface AdminModuleViewUiBinder extends UiBinder<Widget, AdminModuleView> {
    }
    private static final Logger LOGGER = Logger.getLogger(AdminModuleView.class.getName());
    @UiField
    SimplePanel contentPanel;
    @UiField
    Button demandsButton, offersButton, clientsButton, suppliersButton,
    accessRolesButton, emailActivationsButton, invoicesButton, messagesButton,
    paymentMethodsButton, permissionsButton, preferencesButton, problemsButton; //ourPaymentDetailsButton,

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
    public Button getDemandsButton() {
        return demandsButton;
    }

    @Override
    public Button getClientsButton() {
        return clientsButton;
    }

    @Override
    public Button getOffersButton() {
        return offersButton;
    }

    @Override
    public Button getSuppliersButton() {
        return suppliersButton;
    }

    @Override
    public Button getAccessRoleButton() {
        return accessRolesButton;
    }

    @Override
    public Button getEmailActivationButton() {
        return emailActivationsButton;
    }

    @Override
    public Button getInvoiceButton() {
        return invoicesButton;
    }

    @Override
    public Button getMessageButton() {
        return messagesButton;
    }

//    @Override
//    public Button getOurPaymentDetailsButton() {
//        ourPaymentDetailsButton.getTargetHistoryButton(ButtonString);
//    }

    @Override
    public Button getPaymentMethodButton() {
        return paymentMethodsButton;
    }

    @Override
    public Button getPermissionButton() {
        return permissionsButton;
    }

    @Override
    public Button getPreferenceButton() {
        return preferencesButton;
    }

    @Override
    public Button getProblemButton() {
        return problemsButton;
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
//                myDemandsOperatorButton.setStyleName(StyleResource.INSTANCE.common().elemHiddenOn());
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
