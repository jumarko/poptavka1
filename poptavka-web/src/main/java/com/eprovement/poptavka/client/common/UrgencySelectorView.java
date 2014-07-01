/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.github.gwtbootstrap.client.ui.Column;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import java.util.Date;

/**
 * Urgency selector used to define demand urgency levels <b>low, medium, hight, expired</b>.
 * Each level coresponds to time period calculated as difference between validTo and current date.
 *
 * @author Martin Slavkovsky
 */
public class UrgencySelectorView extends Composite implements ProvidesValidate {

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
    @UiField RadioButton urgency1, urgency2, urgency3, urgency4;
    @UiField Column labelColumn4, buttonColumn4, creditsColumn4;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates UrgencySelector view's compontents.
     * @param advanced True displays fourth choice - expired
     *                 False otherwise
     */
    @UiConstructor
    public UrgencySelectorView(boolean advanced) {
        initWidget(uiBinder.createAndBindUi(this));

        labelColumn4.setVisible(advanced);
        buttonColumn4.setVisible(advanced);
        creditsColumn4.setVisible(advanced);
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
        int daysBetween = CalendarUtil.getDaysBetween(now, validTo);
        if (daysBetween < 0) {
            urgency4.setValue(Boolean.TRUE);
        } else if (daysBetween <= Constants.DAYS_URGENCY_HIGH) {
            urgency3.setValue(Boolean.TRUE);
        } else if (daysBetween <= Constants.DAYS_URGENCY_HIGHER) {
            urgency2.setValue(Boolean.TRUE);
        } else {
            urgency1.setValue(Boolean.TRUE);
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
        } else if (urgency1.getValue()) {
            CalendarUtil.addMonthsToDate(validTo, Constants.MONTHS_URGENCY_NORMAL);
        } else {
            CalendarUtil.addDaysToDate(validTo, -1);
        }
        return validTo;
    }

    @Override
    public boolean isValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        urgency1.setValue(true);
    }

    /** CheckBoxes. **/
    /**
     * @return the first urgency radio button
     */
    public RadioButton getUrgency1() {
        return urgency1;
    }

    /**
     * @return the second urgency radio button
     */
    public RadioButton getUrgency2() {
        return urgency2;
    }

    /**
     * @return the third urgency radio button
     */
    public RadioButton getUrgency3() {
        return urgency3;
    }

    /**
     * @return the fourth urgency radio button
     */
    public RadioButton getUrgency4() {
        return urgency4;
    }
}