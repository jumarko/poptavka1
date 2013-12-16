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
    @Override
    public void setClientButtonVisibility(boolean visible) {
        menuSupplierBtn.setVisible(visible);
    }

    public void settingsUserStyleChange() {
        menuUserBtn.addStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    public void settingsClientStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.addStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    public void settingsSupplierStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.addStyleName(Constants.ACT);
        menuSystemBtn.removeStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

    public void settingsSystemsStyleChange() {
        menuUserBtn.removeStyleName(Constants.ACT);
        menuClientBtn.removeStyleName(Constants.ACT);
        menuSupplierBtn.removeStyleName(Constants.ACT);
        menuSystemBtn.addStyleName(Constants.ACT);
        menuSecurityBtn.removeStyleName(Constants.ACT);
    }

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
    @Override
    public SimplePanel getContentPanel() {
        return contentContainer;
    }

    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /** BUTTONS. **/
    @Override
    public Button getMenuUserBtn() {
        return menuUserBtn;
    }

    @Override
    public Button getMenuClientBtn() {
        return menuClientBtn;
    }

    @Override
    public Button getMenuSupplierBtn() {
        return menuSupplierBtn;
    }

    @Override
    public Button getMenuSystemBtn() {
        return menuSystemBtn;
    }

    @Override
    public Button getMenuSecurityBtn() {
        return menuSecurityBtn;
    }

    @Override
    public Widget getToolbarContent() {
        return toolbar;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
