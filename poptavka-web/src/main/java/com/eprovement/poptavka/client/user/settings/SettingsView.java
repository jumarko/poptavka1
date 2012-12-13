package com.eprovement.poptavka.client.user.settings;

import com.eprovement.poptavka.client.common.StatusIconLabel;
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

    public void createNotifyPopup() {
        notify = new PopupPanel(true);
        notify.setGlassEnabled(true);
        VerticalPanel vp = new VerticalPanel();
        //info message
        notifyInfoMessage = new StatusIconLabel();
        vp.add(notifyInfoMessage);
        //Button
        Button closeBtn = new Button(Storage.MSGS.close());
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
    /*  Getters                                                               */
    /**************************************************************************/
    public void showNofity(Boolean updated) {
        if (updated) {
            notifyInfoMessage.setMessage(Storage.MSGS.updatedOK());
            notifyInfoMessage.setPassedSmall(updated);
        } else {
            notifyInfoMessage.setMessage(Storage.MSGS.updatedNotOK());
            notifyInfoMessage.setPassedSmall(updated);
        }
        notify.center();
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
