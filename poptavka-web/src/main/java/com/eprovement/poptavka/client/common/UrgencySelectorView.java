/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 */
public class UrgencySelectorView extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static UrgencySelectorViewUiBinder uiBinder = GWT.create(UrgencySelectorViewUiBinder.class);

    interface UrgencySelectorViewUiBinder extends UiBinder<Widget, UrgencySelectorView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField FluidContainer fluidContainer;
    @UiField RadioButton urgency1, urgency2, urgency3;
    @UiField Anchor revert;
    /** Class attributes. **/
    private Date validToOriginal;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public UrgencySelectorView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("revert")
    public void revert(ClickEvent e) {
        setValidTo(validToOriginal);
        setChangeStyle(false);
    }

    @UiHandler(value = {"urgency1", "urgency2", "urgency3" })
    public void checkBoxClickHandlers(ClickEvent e) {
        setChangeStyle(isChanged());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets valid to date. According to passed "valid to" date will be selected
     * appropriate check boxes representing appropriate urgency level.
     * @param validTo
     */
    public void setValidTo(Date validTo) {
        Date now = new Date();
        this.validToOriginal = validTo;
        int daysBetween = CalendarUtil.getDaysBetween(now, validTo);
        if (daysBetween <= Constants.DAYS_URGENCY_HIGH) {
            urgency3.setValue(Boolean.TRUE);
        } else if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
            urgency2.setValue(Boolean.TRUE);
        } else {
            urgency1.setValue(Boolean.TRUE);
        }
    }

    public void setChangeStyle(boolean isChange) {
        if (isChange) {
            fluidContainer.addStyleName(Storage.RSCS.common().changed());
            revert.setVisible(true);
        } else {
            fluidContainer.removeStyleName(Storage.RSCS.common().changed());
            revert.setVisible(false);
        }
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** Methods. **/
    /**
     * Construct valid to date to represent urgency level of demand.
     * Date is constructed by adding days to current date. For HIGH urgency level
     * are added less days than to HIGHER or NORMAL. See appropriate constants
     * in Constants class.
     *
     * @return valid to date
     */
    public Date getValidTo() {
        Date validTo = new Date();
        if (urgency3.getValue()) {
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGH);
        } else if (urgency2.getValue()) {
            CalendarUtil.addDaysToDate(validTo, Constants.DAYS_URGENCY_HIGHER);
        } else {
            CalendarUtil.addMonthsToDate(validTo, Constants.MONTHS_URGENCY_NORMAL);
        }
        return validTo;
    }

    public boolean isChanged() {
        return !CalendarUtil.isSameDate(validToOriginal, getValidTo());
    }

    /** CheckBoxes. **/
    public RadioButton getUrgency1() {
        return urgency1;
    }

    public RadioButton getUrgency2() {
        return urgency2;
    }

    public RadioButton getUrgency3() {
        return urgency3;
    }
}
