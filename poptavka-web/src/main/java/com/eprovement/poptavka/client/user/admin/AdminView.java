/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.OverflowComposite;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule.AdminWidget;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;

/**
 * Admin view consists of left vertical menu and body where admin widgets are set.
 *
 * @author Martin Slavkovsky
 */
public class AdminView extends OverflowComposite implements IAdminModule.View {

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
    @UiField Button newDemandsBtn, assigendDemandsBtn, activeDemandsBtn, clientsBtn;
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
     * Sets ACT style to client menu buttons.
     * @param widget defines loaded clien't widget
     */
    @Override
    public void setClientMenuActStyle(AdminWidget widget) {
        newDemandsBtn.removeStyleName(Constants.ACT);
        assigendDemandsBtn.removeStyleName(Constants.ACT);
        activeDemandsBtn.removeStyleName(Constants.ACT);
        clientsBtn.removeStyleName(Constants.ACT);

        switch (widget) {
            case NEW_DEMANDS:
                newDemandsBtn.addStyleName(Constants.ACT);
                break;
            case ASSIGNED_DEMANDS:
                assigendDemandsBtn.addStyleName(Constants.ACT);
                break;
            case ACTIVE_DEMANDS:
                activeDemandsBtn.addStyleName(Constants.ACT);
                break;
            case CLIENTS:
                clientsBtn.addStyleName(Constants.ACT);
                break;
            default:
                break;
        }
    }

    /**
     * Sets content/body widget.
     * @param contentWidget
     */
    @Override
    public void setContent(IsWidget contentWidget) {
        contentContainer.setWidget(contentWidget);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the new demands button
     */
    @Override
    public Button getNewDemandsBtn() {
        return newDemandsBtn;
    }

    /**
     * @return the assigned demands button
     */
    @Override
    public Button getAssignedDemandsBtn() {
        return assigendDemandsBtn;
    }

    /**
     * @return the activate demands button
     */
    @Override
    public Button getActiveDemandsBtn() {
        return activeDemandsBtn;
    }

    /**
     * @return the clients button
     */
    @Override
    public Button getClientsBtn() {
        return clientsBtn;
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
}
