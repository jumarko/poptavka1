package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.session.Storage;
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
    SimplePanel contentPanel;
    @UiField
    Button updateButton;
    @UiField
    StatusIconLabel infoStatus1, infoStatus2, infoStatus3, infoStatus4;
    //
    private PopupPanel notify;
    private StatusIconLabel notifyInfoMessage;

    /**************************************************************************/
    /* Initialization */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
        createNotifyPopup();
    }

    @Override
    public void initWidgetDefaults() {
        //StatusIconLables
        infoStatus1.setState(State.ACCEPT_16);
        infoStatus1.setDescription("No changes made to user settings.");
        infoStatus2.setState(State.ACCEPT_16);
        infoStatus2.setDescription("No changes made to client settings.");
        infoStatus3.setState(State.ACCEPT_16);
        infoStatus3.setDescription("No changes made to supplier settings.");
        infoStatus4.setState(State.ACCEPT_16);
        infoStatus4.setDescription("No changes made to system settings.");
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
        if (isChange) {
            infoStatus1.setDescription("User profile has changed.");
            infoStatus1.setState(State.INFO_16);
        } else {
            infoStatus1.setDescription("No changes to user profile.");
            infoStatus1.setState(State.ACCEPT_16);
        }
    }

    @Override
    public void updateClientStatus(boolean isChange) {
        if (isChange) {
            infoStatus2.setDescription("Client profile has changed.");
            infoStatus2.setState(State.INFO_16);
        } else {
            infoStatus2.setDescription("No changes to client profile.");
            infoStatus2.setState(State.ACCEPT_16);
        }
    }

    @Override
    public void updateSupplierStatus(boolean isChange) {
        if (isChange) {
            infoStatus3.setDescription("Supplier profile has changed.");
            infoStatus3.setState(State.INFO_16);
        } else {
            infoStatus3.setDescription("No changes to supplier profile.");
            infoStatus3.setState(State.ACCEPT_16);
        }
    }

    @Override
    public void updateSystemStatus(boolean isChange) {
        if (isChange) {
            infoStatus4.setDescription("System profile has changed.");
            infoStatus4.setState(State.INFO_16);
        } else {
            infoStatus4.setDescription("No changes to system profile.");
            infoStatus4.setState(State.ACCEPT_16);
        }
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public SimplePanel getContentPanel() {
        return contentPanel;
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
}
