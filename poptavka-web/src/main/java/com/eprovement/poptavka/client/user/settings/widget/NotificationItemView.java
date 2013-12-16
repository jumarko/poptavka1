/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ui.WSListBox;
import com.eprovement.poptavka.client.common.ui.WSListBoxData;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.settings.NotificationDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author mato
 */
public class NotificationItemView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static NotificationItemVIewUiBinder uiBinder = GWT.create(NotificationItemVIewUiBinder.class);

    interface NotificationItemVIewUiBinder extends UiBinder<Widget, NotificationItemView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField Label name;
    @UiField HTMLPanel notificationChoicePanel;
    @UiField Button onBtn;
    @UiField Button offBtn;
    @UiField(provided = true) WSListBox period;
    private boolean onSelected;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public NotificationItemView(NotificationDetail item) {
        createPeriodListBox(item.getPeriod().getValue());

        initWidget(uiBinder.createAndBindUi(this));

        name.setText(item.getName());
        onSelected = item.isEnabled();
        period.setSelected(item.getPeriod().getValue());

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void createPeriodListBox(String periodName) {
        WSListBoxData demandTypeData = new WSListBoxData();
        int key = 0;
        int selectIdx = 0;
        for (Period item : Period.values()) {
            if (item.getValue().equals(periodName)) {
                selectIdx = key;
            }
            demandTypeData.insertItem(item.getValue(), key++);
        }
        period = WSListBox.createListBox(demandTypeData, selectIdx);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("onBtn")
    public void onBtnClickHandler(ClickEvent e) {
        onSelected = false;
        notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchRight());
        notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchLeft());
    }

    @UiHandler("offBtn")
    public void offBtnClickHandler(ClickEvent e) {
        onSelected = true;
        notificationChoicePanel.addStyleName(StyleResource.INSTANCE.common().switchRight());
        notificationChoicePanel.removeStyleName(StyleResource.INSTANCE.common().switchLeft());
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Label getName() {
        return name;
    }

    public boolean getEnabled() {
        return onSelected;
    }

    public String getPeriod() {
        return period.getSelected();
    }
}
