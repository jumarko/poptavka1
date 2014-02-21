/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.github.gwtbootstrap.client.ui.constants.ControlGroupType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * Popup for entering feedback information.
 *
 * @author Martin Slavkovsky
 */
public class FeedbackPopupView extends Modal implements ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static FeedbackPopupViewUiBinder uiBinder = GWT.create(FeedbackPopupViewUiBinder.class);

    interface FeedbackPopupViewUiBinder extends UiBinder<Widget, FeedbackPopupView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureModalStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField FluidRow rateRow1, rateRow2, rateRow3, rateRow4, rateRow5;
    @UiField WellForm rateWell1, rateWell2, rateWell3, rateWell4, rateWell5;
    @UiField Label clientLabel, supplierLabel;
    @UiField Label displayName;
    @UiField TextArea commentArea;
    @UiField Button rateBtn;
    @UiField ControlGroup controlGroup;
    @UiField HTMLPanel errorPanel;
    /** Class attributes. **/
    private int rating;
    private String comment;
    public static final int CLIENT = 0;
    public static final int SUPPLIER = 1;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates FeedbackPopup view's components.
     * @param rateWhat CLIENT, SUPPLIER constants
     */
    public FeedbackPopupView(int rateWhat) {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().feedbackModal());
        setBackdrop(BackdropType.STATIC);
        setKeyboard(false);
        setDynamicSafe(true);
        show();

        clientLabel.setVisible(rateWhat == CLIENT);
        supplierLabel.setVisible(rateWhat == SUPPLIER);

        bindHandlers();
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    /**
     * Binds handlers for popup buttons.
     */
    public void bindHandlers() {
        rateRow1.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setRate(rateWell1, Constants.RATE_1, Storage.MSGS.feedbackComment1());
            }
        }, ClickEvent.getType());
        rateRow2.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setRate(rateWell2, Constants.RATE_2, Storage.MSGS.feedbackComment2());
            }
        }, ClickEvent.getType());
        rateRow3.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setRate(rateWell3, Constants.RATE_3, Storage.MSGS.feedbackComment3());
            }
        }, ClickEvent.getType());
        rateRow4.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setRate(rateWell4, Constants.RATE_4, Storage.MSGS.feedbackComment4());
            }
        }, ClickEvent.getType());
        rateRow5.addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                setRate(rateWell5, Constants.RATE_5, Storage.MSGS.feedbackComment5());
            }
        }, ClickEvent.getType());
        commentArea.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                if (commentArea.getText().length() >= Constants.FEEDBACK_COMMENT_MAX_LENGTH) {
                    controlGroup.setType(ControlGroupType.ERROR);
                    errorPanel.setVisible(true);
                } else {
                    controlGroup.setType(ControlGroupType.NONE);
                    errorPanel.setVisible(false);
                }
            }
        });
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets display name text.
     * @param displayName text
     */
    public void setDisplayName(String displayName) {
        this.displayName.setText(displayName + " ?");
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void reset() {
        controlGroup.setType(ControlGroupType.NONE);
        errorPanel.setVisible(false);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the rate button
     */
    public Button getRateBtn() {
        return rateBtn;
    }

    /**
     * @return the rating value
     */
    public int getRating() {
        return rating;
    }

    /**
     * @return the rating comment
     */
    public String getComment() {
        if (commentArea.getText().isEmpty()) {
            return comment;
        } else {
            return comment + "\nAddition:" + commentArea.getText();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return !errorPanel.isVisible();
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Sets rate.
     * @param rateWell - one of default rate choices
     * @param rating value
     * @param comment of rating
     */
    private void setRate(WellForm rateWell, int rating, String comment) {
        unSelectRateOptions();
        rateWell.addStyleName(Constants.ACT);
        rateBtn.setVisible(true);
        this.rating = rating;
        this.comment = comment;
    }

    /**
     * Unselects rate options.
     */
    private void unSelectRateOptions() {
        rateBtn.setVisible(false);
        rateWell1.removeStyleName(Constants.ACT);
        rateWell2.removeStyleName(Constants.ACT);
        rateWell3.removeStyleName(Constants.ACT);
        rateWell4.removeStyleName(Constants.ACT);
        rateWell5.removeStyleName(Constants.ACT);
    }
}
