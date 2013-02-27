/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.Period;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author mato
 */
public class NotificationItemView extends Composite implements HasChangeHandlers {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static NotificationItemView.NotificationItemVIewUiBinder uiBinder = GWT
            .create(NotificationItemView.NotificationItemVIewUiBinder.class);
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField FluidRow panel;
    @UiField Anchor revert;
    @UiField Label name;
    @UiField CheckBox enabled;
    @UiField(provided = true) ListBox period;
    //
    private static final String ENABLED = "enabled";
    private static final String PERIOD = "period";
    private boolean originaEnabled;
    private int originalPeriodIdx;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public NotificationItemView() {
        period = new ListBox();
        for (Period item : Period.values()) {
            period.addItem(item.getValue());
        }
        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    /**************************************************************************/
    /* Has handlers                                                           */
    /**************************************************************************/
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return addDomHandler(handler, ChangeEvent.getType());
    }

    interface NotificationItemVIewUiBinder extends
            UiBinder<Widget, NotificationItemView> {
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("enabled")
    public void addEnabledClickHandler(ClickEvent event) {
        setChangedStyles(isNotificationChange());
    }

    @UiHandler("period")
    public void addPeriodChangeHandler(ChangeEvent event) {
        setChangedStyles(isNotificationChange());
    }

    @UiHandler("revert")
    public void revertClickHandler(ClickEvent e) {
        revert();
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), this);
    }

    /**************************************************************************/
    /* Methods                                                                */
    /**************************************************************************/
    public void revert() {
        if (isNotificationChange()) {
            enabled.setValue(originaEnabled);
            period.setSelectedIndex(originalPeriodIdx);
            reset();
        }
    }

    public void reset() {
        setChangedStyles(false);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /** Setters. **/
    public void setEnabledBothValues(boolean value) {
        originaEnabled = value;
        enabled.setValue(value);
    }

    public void setEnableValue(boolean value) {
        enabled.setValue(value);
    }

    public void setPerioddBothValues(int value) {
        originalPeriodIdx = value;
        period.setSelectedIndex(value);
    }

    public void setPeriodValue(int value) {
        period.setSelectedIndex(value);
    }

    private void setChangedStyles(boolean changed) {
        if (changed) {
            period.setStyleName(Storage.RSCS.common().changed());
            revert.setVisible(true);
        } else {
            period.removeStyleName(Storage.RSCS.common().changed());
            revert.setVisible(false);
        }
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Label getName() {
        return name;
    }

    public boolean getOriginalEnabled() {
        return originaEnabled;
    }

    public boolean getEnabled() {
        return enabled.getValue();
    }

    public int getOriginalPeriod() {
        return originalPeriodIdx;
    }

    public int getPeriod() {
        return period.getSelectedIndex();
    }

    public ChangeDetail getEnabledChangeDetail() {
        ChangeDetail changeDetail = new ChangeDetail(ENABLED);
        changeDetail.setOriginalValue(originaEnabled);
        changeDetail.setValue(enabled.getValue());
        return changeDetail;
    }

    public ChangeDetail getPeriodChangeDetail() {
        ChangeDetail changeDetail = new ChangeDetail(PERIOD);
        changeDetail.setOriginalValue(originalPeriodIdx);
        changeDetail.setValue(period.getSelectedIndex());
        return changeDetail;
    }

    public boolean isNotificationChange() {
        return (originaEnabled != enabled.getValue())
                || (originalPeriodIdx != period.getSelectedIndex());
    }
}
