package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;
import com.google.inject.Inject;

public class AdminView extends OverflowComposite implements AdminPresenter.AdminModuleInterface {

    private static AdminModuleViewUiBinder uiBinder = GWT.create(AdminModuleViewUiBinder.class);

    interface AdminModuleViewUiBinder extends UiBinder<Widget, AdminView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField SimplePanel contentContainer;
    @UiField Button newDemandsBtn, activeDemandsBtn;
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
    /** Class attributes. **/
    @Inject
    private AdminToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setContent(Widget contentWidget) {
        contentContainer.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Button getActiveDemandsBtn() {
        return activeDemandsBtn;
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
    public SimplePanel getContentContainer() {
        return contentContainer;
    }

    @Override
    public AdminToolbarView getToolbarContent() {
        return toolbar;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
