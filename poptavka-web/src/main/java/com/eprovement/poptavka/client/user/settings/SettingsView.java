package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SettingsView extends Composite implements
        SettingsPresenter.HomeSettingsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static HomeSettingsViewUiBinder uiBinder = GWT
            .create(HomeSettingsViewUiBinder.class);

    interface HomeSettingsViewUiBinder extends
            UiBinder<Widget, SettingsView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    Button menuUserBtn, menuClientBtn, menuSupplierBtn, menuSystemBtn;
    @UiField
    SimplePanel contentPanel, footerHolder;
    @UiField
    Button updateButton;
    @UiField
    StatusIconLabel infoStatus1, infoStatus2, infoStatus3, infoStatus4;
    //
    private PopupPanel notify;
    private StatusIconLabel notifyInfoMessage;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        if (Storage.getBusinessUserDetail() != null
                && !Storage.getBusinessUserDetail().getBusinessRoles().contains(
                    BusinessUserDetail.BusinessRole.CLIENT)) {
            infoStatus2.setVisible(false);
            menuClientBtn.setVisible(false);
        }
        StyleResource.INSTANCE.detailViews().ensureInjected();
        createNotifyPopup();
    }

    @Override
    public void initWidgetDefaults() {
        //StatusIconLables
        setInfoStatusState(false, infoStatus1, Storage.MSGS.settingsUserSettings());
        setInfoStatusState(false, infoStatus2, Storage.MSGS.settingsClientSettings());
        setInfoStatusState(false, infoStatus3, Storage.MSGS.settingsSupplierSettings());
        setInfoStatusState(false, infoStatus4, Storage.MSGS.settingsSystemSettings());
    }

    public void createNotifyPopup() {
        notify = new PopupPanel(true);
        notify.setGlassEnabled(true);
        VerticalPanel vp = new VerticalPanel();
        //info message
        notifyInfoMessage = new StatusIconLabel();
        vp.add(notifyInfoMessage);
        //Button
        Button closeBtn = new Button(Storage.MSGS.commonBtnClose());
        closeBtn.addStyleName(StyleResource.INSTANCE.common().buttonGrey());
        closeBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                notify.hide();
            }
        });
        vp.add(closeBtn);
        notify.add(vp);
    }

    /**************************************************************************/
    /*  Methods handled by view                                               */
    /**************************************************************************/
    @Override
    public void showNofity(String message, Boolean updated) {
        updateUserStatus(!updated);
        updateClientStatus(!updated);
        updateSupplierStatus(!updated);
        updateSystemStatus(!updated);
        notifyInfoMessage.setMessage(message);
        notifyInfoMessage.setPassedSmall(updated);
        notify.center();
    }

    @Override
    public void updateUserStatus(boolean isChange) {
        setInfoStatusState(isChange, infoStatus1, Storage.MSGS.settingsUserSettings());
    }

    @Override
    public void updateClientStatus(boolean isChange) {
        setInfoStatusState(isChange, infoStatus2, Storage.MSGS.settingsClientSettings());
    }

    @Override
    public void updateSupplierStatus(boolean isChange) {
        setInfoStatusState(isChange, infoStatus3, Storage.MSGS.settingsSupplierSettings());
    }

    @Override
    public void updateSystemStatus(boolean isChange) {
        setInfoStatusState(isChange, infoStatus4, Storage.MSGS.settingsSystemSettings());
    }

    @Override
    public void setClientButtonVisibility(boolean visible) {
        menuSupplierBtn.setVisible(visible);
        infoStatus3.setVisible(visible);
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
    }

    @Override
    public SimplePanel getFooterHolder() {
        return footerHolder;
    }

    /** BUTTONS. **/
    @Override
    public Button getUpdateButton() {
        return updateButton;
    }

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
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /*  Helper methods                                                        */
    /**************************************************************************/
    private void setInfoStatusState(boolean isChange, StatusIconLabel iconLabel, String message) {
        if (isChange) {
            iconLabel.setDescription(message + " has changed.");
            iconLabel.setState(State.INFO_16);
        } else {
            iconLabel.setDescription("No changes to " + message);
            iconLabel.setState(State.ACCEPT_16);
        }
    }
}
