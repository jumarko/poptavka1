/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.forms;

import com.eprovement.poptavka.client.common.session.Storage;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * User registration widget represent user's registration form.
 * It creates BusinessUserDetail. Provides field validation.
 * @author Martin Slavkovsky
 */
public class RatingInfoForm extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static RatingInfoFormUiBinder uiBinder = GWT.create(RatingInfoFormUiBinder.class);

    interface RatingInfoFormUiBinder extends UiBinder<Widget, RatingInfoForm> {
    }

    /**************************************************************************/
    /* Attribute                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField Label ratingText;
    @UiField HTMLPanel ratingIndicator;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    /**
     * Creates RatingInfoForm view's compotnents.
     */
    public RatingInfoForm() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets rating data.
     * @param rating value
     */
    public void setRating(Integer rating) {
        if (rating == 0) {
            ratingText.setText(Storage.MSGS.commonNotRanked());
        } else {
            ratingText.setText(rating.toString());
        }
        ratingIndicator.setWidth(rating.toString() + "%");
    }
}