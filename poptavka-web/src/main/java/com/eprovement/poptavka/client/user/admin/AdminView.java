/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
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

/**
 * Admin view consists of left vertical menu and body where admin widgets are set.
 *
 * @author Martin Slavkovsky
 */
public class AdminView extends OverflowComposite implements AdminPresenter.AdminModuleInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
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
    /**
     * Creates Admin view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets content/body widget.
     * @param contentWidget
     */
    @Override
    public void setContent(Widget contentWidget) {
        contentContainer.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the activate demands button
     */
    @Override
    public Button getActiveDemandsBtn() {
        return activeDemandsBtn;
    }

    /**
     * @return the demands button
     */
    @Override
    public Button getDemandsButton() {
        return demandsButton;
    }

    /**
     * @return the clients button
     */
    @Override
    public Button getClientsButton() {
        return clientsButton;
    }

    /**
     * @return the offers button
     */
    @Override
    public Button getOffersButton() {
        return offersButton;
    }

    /**
     * @return the suppliers button
     */
    @Override
    public Button getSuppliersButton() {
        return suppliersButton;
    }

    /**
     * @return the access roles button
     */
    @Override
    public Button getAccessRoleButton() {
        return accessRolesButton;
    }

    /**
     * @return the email activation button
     */
    @Override
    public Button getEmailActivationButton() {
        return emailActivationsButton;
    }

    /**
     * @return the invoices button
     */
    @Override
    public Button getInvoiceButton() {
        return invoicesButton;
    }

    /**
     * @return the messages button
     */
    @Override
    public Button getMessageButton() {
        return messagesButton;
    }

    /**
     * @return the new demands button
     */
    @Override
    public Button getNewDemandsBtn() {
        return newDemandsBtn;
    }
//TODO Martin - refactor
//    @Override
//    public Button getOurPaymentDetailsButton() {
//        ourPaymentDetailsButton.getTargetHistoryButton(ButtonString);
//    }
    /**
     * @return the payment method button
     */
    @Override
    public Button getPaymentMethodButton() {
        return paymentMethodsButton;
    }

    /**
     * @return the permission button
     */
    @Override
    public Button getPermissionButton() {
        return permissionsButton;
    }

    /**
     * @return the preference button
     */
    @Override
    public Button getPreferenceButton() {
        return preferencesButton;
    }

    /**
     * @return the problem button
     */
    @Override
    public Button getProblemButton() {
        return problemsButton;
    }

    /**
     * @return the content container button
     */
    @Override
    public SimplePanel getContentContainer() {
        return contentContainer;
    }

    /**
     * @return the AdminToolbar view
     */
    @Override
    public AdminToolbarView getToolbarContent() {
        return toolbar;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
