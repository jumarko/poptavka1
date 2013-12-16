package com.eprovement.poptavka.client.common.forms;

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
    public RatingInfoForm() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setRating(Integer rating) {
        ratingText.setText(rating.toString());
        ratingIndicator.setWidth(rating.toString() + "%");
    }
}