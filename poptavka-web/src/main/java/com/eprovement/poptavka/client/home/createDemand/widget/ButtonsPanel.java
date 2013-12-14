/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.widget;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Represents <b>back</b> and <b>next</b> buttons pair in either demand creation or supplier creation widgets.
 *
 * @author Martin Slavkovsky
 */
public class ButtonsPanel extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ButtonsPanelUiBinder uiBinder = GWT.create(ButtonsPanelUiBinder.class);

    interface ButtonsPanelUiBinder extends UiBinder<Widget, ButtonsPanel> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField FlowPanel buttonsPanel;
    @UiField HTMLPanel separator;
    @UiField Button backBtn, nextBtn;
    @UiField Tooltip nextBtnTooltip;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @UiConstructor
    public ButtonsPanel(String backBtn, String nextBtn, String nextBtnTooltip, String size) {
        initWidget(uiBinder.createAndBindUi(this));
        this.backBtn.setText(backBtn);
        this.nextBtn.setText(nextBtn);
        this.nextBtnTooltip.setText(nextBtnTooltip);

        this.buttonsPanel.addStyleName(size.toLowerCase());
        this.backBtn.addStyleName("left-".concat(size.toLowerCase()));
        this.nextBtn.addStyleName("right-".concat(size.toLowerCase()));
        this.separator.setVisible(!size.equalsIgnoreCase("SMALL"));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the back button
     */
    public Button getBackBtn() {
        return backBtn;
    }

    /**
     * @return the next button
     */
    public Button getNextBtn() {
        return nextBtn;
    }

    /**
     * @return the next button's tooltip.
     */
    public Tooltip getNextBtnTooltip() {
        return nextBtnTooltip;
    }
}
