/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.creditAnnouncer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Jaro
 */
public class CreditStatusAnnouncerView extends Composite
    implements CreditStatusAnnouncerPresenter.ICreditStatusAnnouncerView {

    private static CreditStatusAnnouncerViewUiBinder uiBinder = GWT.create(CreditStatusAnnouncerViewUiBinder.class);

    interface CreditStatusAnnouncerViewUiBinder extends UiBinder<Widget, CreditStatusAnnouncerView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField
    Label rechargeLabel;
    @UiField
    HTMLPanel rechargeBattery;
    @UiField
    Button rechargeButton;

    /** Class attributes. **/
    public static final int GREEN_CREDIT_LIMIT  = 10;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates CreditStatusAnnouncerView view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * @return the recharge button
     */
    @Override
    public Button getRechargeButton() {
        return rechargeButton;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
     /**
     * Depending on the user's current credit sets the correct widget design.
     * @param credits - user's credit
     */
    @Override
    public void setCreditStatus(int credits) {
        if (credits == 0) {
            setRedCreditStatus();
        } else if (credits < GREEN_CREDIT_LIMIT) {
            setOrangeCreditStatus();
        } else {
            setGreenCreditStatus();
        }
    }
    /**
     * Sets green credit design.
     */
    private void setGreenCreditStatus() {
        rechargeBattery.setStyleName("recharge-battery green");
    }

    /**
     * Sets orange credit design.
     */
    private void setOrangeCreditStatus() {
        rechargeBattery.setStyleName("recharge-battery orange");
    }

    /**
     * Sets red credit design.
     */
    private void setRedCreditStatus() {
        rechargeBattery.setStyleName("recharge-battery red");
    }

}