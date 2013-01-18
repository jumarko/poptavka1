package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.StatusIconLabel;
import com.eprovement.poptavka.client.common.StatusIconLabel.State;
import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
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
    StackLayoutPanel stackPanel;
    @UiField
    SimplePanel userSettingsPanel, clientSettingsPanel, supplierSettingsPanel;
    @UiField
    Button updateButton;
    @UiField
    StatusIconLabel userInfoStatus, clientInfoStatus, supplierInfoStatus;
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
        //StackLayoutPanel
        if (stackPanel.getVisibleIndex() == 0) {
            BeforeSelectionEvent.fire(stackPanel, 0);
        } else {
            stackPanel.showWidget(0);
        }
        //StatusIconLables
        userInfoStatus.setState(State.ACCEPT_16);
        userInfoStatus.setDescription("No changes made to user settings.");
        clientInfoStatus.setState(State.ACCEPT_16);
        clientInfoStatus.setDescription("No changes made to client settings.");
        supplierInfoStatus.setState(State.ACCEPT_16);
        supplierInfoStatus.setDescription("No changes made to supplier settings.");
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
        notifyInfoMessage.setMessage(message);
        notifyInfoMessage.setPassedSmall(updated);
        notify.center();
    }

    @Override
    public void updateUserStatus(boolean isChange) {
        if (isChange) {
            userInfoStatus.setDescription("User profile has changed.");
            userInfoStatus.setState(State.INFO_16);
        } else {
            userInfoStatus.setDescription("No changes to user profile.");
            userInfoStatus.setState(State.ACCEPT_16);
        }
    }

    @Override
    public void updateClientStatus(boolean isChange) {
        if (isChange) {
            clientInfoStatus.setDescription("Client profile has changed.");
            clientInfoStatus.setState(State.INFO_16);
        } else {
            clientInfoStatus.setDescription("No changes to client profile.");
            clientInfoStatus.setState(State.ACCEPT_16);
        }
    }

    @Override
    public void updateSupplierStatus(boolean isChange) {
        if (isChange) {
            supplierInfoStatus.setDescription("Supplier profile has changed.");
            supplierInfoStatus.setState(State.INFO_16);
        } else {
            supplierInfoStatus.setDescription("No changes to supplier profile.");
            supplierInfoStatus.setState(State.ACCEPT_16);
        }
    }

    /**************************************************************************/
    /*  Getters                                                               */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public StackLayoutPanel getStackPanel() {
        return stackPanel;
    }

    @Override
    public SimplePanel getUserSettingsPanel() {
        return userSettingsPanel;
    }

    @Override
    public SimplePanel getClientSettingsPanel() {
        return clientSettingsPanel;
    }

    @Override
    public SimplePanel getSupplierSettingsPanel() {
        return supplierSettingsPanel;
    }

    /** STATUS LABELS. **/
    @Override
    public StatusIconLabel getUserInfoStatus() {
        return userInfoStatus;
    }

    /** BUTTONS. **/
    @Override
    public Button getUpdateButton() {
        return updateButton;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
