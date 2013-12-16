/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.toolbar.SettingsToolbarView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * View consists of left vertical menu and content area for placing module's widgets.
 * @author Martin Slavkovsky
 */
public class SettingsView extends Composite implements
        SettingsPresenter.SttingsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SettingsViewUiBinder uiBinder = GWT.create(SettingsViewUiBinder.class);

    interface SettingsViewUiBinder extends UiBinder<Widget, SettingsView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Button menuUserBtn, menuClientBtn, menuSupplierBtn, menuSystemBtn, menuSecurityBtn;
    @UiField SimplePanel contentContainer, footerContainer;
    /** Class attribute. **/
    @Inject
    private SettingsToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates Settings view's compontents.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        if (Storage.getBusinessUserDetail() != null
                && !Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.CLIENT)) {
            menuClientBtn.setVisible(false);
        }

        CssInjector.INSTANCE.ensureCommonStylesInjected();
        StyleResource.INSTANCE.modal().ensureInjected();
    }

    /**************************************************************************/
    /*  Methods handled by view                                               */
    /**************************************************************************/
    /**
     * Allows supplier settings button in menu if logged user is a supplier.
     * @param visible true to display, false to hide
     */
    @Override
    public void allowSupplierSettings(boolean visible) {
        menuSupplierBtn.setVisible(visible);
    }

    /**
     * Sets active style for user menu button style.
     */
    @Override
    public void settingsUserStyleChange() {
        menuUserBtn.addStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for client menu button style.
     */
    @Override
    public void settingsClientStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.addStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for supplier menu button style.
     */
    @Override
    public void settingsSupplierStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.addStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for system menu button style.
     */
    @Override
    public void settingsSystemsStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.addStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    /**
     * Sets active style for security menu button style.
     */
    @Override
    public void settingsSecurityStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.addStyleName(Constants.ACT);
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /** PANELS. **/
    /**
     * @return the content container
     */
    @Override
    public SimplePanel getContentPanel() {
        return contentContainer;
    }

    /**
     * @return the footer container
     */
    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /** BUTTONS. **/
    /**
     * @return the use menu button
     */
    @Override
    public Button getMenuUserBtn() {
        return menuUserBtn;
    }

    /**
     * @return the client menu button
     */
    @Override
    public Button getMenuClientBtn() {
        return menuClientBtn;
    }

    /**
     * @return the supplier menu button
     */
    @Override
    public Button getMenuSupplierBtn() {
        return menuSupplierBtn;
    }

    /**
     * @return the system menu button
     */
    @Override
    public Button getMenuSystemBtn() {
        return menuSystemBtn;
    }

    /**
     * @return the security menut button
     */
    @Override
    public Button getMenuSecurityBtn() {
        return menuSecurityBtn;
    }

    /**
     * @return the custom toolbar widget
     */
    @Override
    public Widget getToolbarContent() {
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
