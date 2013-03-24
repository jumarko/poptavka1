package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.OverflowComposite;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;

public class AdminView extends OverflowComposite implements AdminPresenter.AdminModuleInterface {

    private static AdminModuleViewUiBinder uiBinder = GWT.create(AdminModuleViewUiBinder.class);

    interface AdminModuleViewUiBinder extends UiBinder<Widget, AdminView> {
    }
    private static final Logger LOGGER = Logger.getLogger(AdminView.class.getName());
    @UiField SimplePanel contentPanel;
    @UiField Button newDemandsBtn;
    @UiField SimplePanel footerHolder;
    //TODO LATER Martin - finnish admin interface for other tables
    //Temporary initialzie manually because in uiBinder are those buttons commented
    Button demandsButton = new Button();
    Button offersButton = new Button();
    Button clientsButton = new Button();
    Button suppliersButton = new Button();
    Button accessRolesButton = new Button();
    Button emailActivationsButton = new Button();
    Button invoicesButton = new Button();
    Button messagesButton = new Button();
    Button paymentMethodsButton = new Button();
    Button permissionsButton = new Button();
    Button preferencesButton = new Button();
    Button problemsButton = new Button();
    //ourPaymentDetailsButton,

    @Override
    public void createView() {
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

    @Override
    public Button getNewDemandsBtn() {
        return newDemandsBtn;
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

    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public SimplePanel getFooterHolder() {
        return footerHolder;
    }
}
