/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.domain.enums.Period;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mato
 */
public class NotificationItemView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static NotificationItemView.NotificationItemVIewUiBinder uiBinder = GWT
            .create(NotificationItemView.NotificationItemVIewUiBinder.class);

    interface NotificationItemVIewUiBinder extends
            UiBinder<Widget, NotificationItemView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField
    Label name;
    @UiField
    CheckBox enabled;
    @UiField(provided = true)
    ListBox period;
    //
    private String stringStorage;
    private List<String> originalsStorage = new ArrayList<String>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public NotificationItemView() {
        period = new ListBox();
        for (Period item : Period.values()) {
            period.addItem(item.getValue());
        }
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("enabled")
    public void addEnabledFocusHandler(FocusEvent event) {
        stringStorage = enabled.getValue().toString();
    }

    @UiHandler("enabled")
    public void addEnabledBlurHandler(BlurEvent event) {
        if (stringStorage.contains(enabled.getValue().toString())) {
            originalsStorage.remove(stringStorage);
        } else {
            originalsStorage.add(stringStorage);
        }
    }

    @UiHandler("period")
    public void addEnabledPeriodHandler(FocusEvent event) {
        stringStorage = period.getItemText(period.getSelectedIndex());
    }

    @UiHandler("period")
    public void addEnabledPeriodHandler(BlurEvent event) {
        if (stringStorage.contains(period.getItemText(period.getSelectedIndex()))) {
            originalsStorage.remove(stringStorage);
        } else {
            originalsStorage.add(stringStorage);
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Label getName() {
        return name;
    }

    public CheckBox getEnabled() {
        return enabled;
    }

    public ListBox getPeriod() {
        return period;
    }

    public boolean isNotificationChange() {
        return !originalsStorage.isEmpty();
    }
}
