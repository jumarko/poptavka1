/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.github.gwtbootstrap.client.ui.WellForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class FeedbackPopupView extends Composite {

    private static FeedbackPopupViewUiBinder uiBinder = GWT.create(FeedbackPopupViewUiBinder.class);

    interface FeedbackPopupViewUiBinder extends UiBinder<Widget, FeedbackPopupView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField Modal popupFeedback;
    @UiField FluidRow rateRow1, rateRow2, rateRow3, rateRow4, rateRow5;
    @UiField WellForm rateWell1, rateWell2, rateWell3, rateWell4, rateWell5;
    @UiField Label clientLabel, supplierLabel;
    @UiField Label displayName;
    /** Rate. **/
    @UiField TextArea commentArea;
    @UiField Button rateBtn;
    private int rating;
    private String comment;
    public static final int CLIENT = 0;
    public static final int SUPPLIER = 1;

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    public FeedbackPopupView(int rateWhat) {
        initWidget(uiBinder.createAndBindUi(this));
        popupFeedback.show();
        clientLabel.setVisible(rateWhat == CLIENT);
        supplierLabel.setVisible(rateWhat == SUPPLIER);
        bindHandlers();
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
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
    }

    /**************************************************************************/
    /* GETTERS & SETTERS                                                      */
    /**************************************************************************/
    public Modal getPopupFeedback() {
        return popupFeedback;
    }

    public Button getRateBtn() {
        return rateBtn;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        if (commentArea.getText().isEmpty()) {
            return comment;
        } else {
            return comment + "\nAddition:" + commentArea.getText();
        }
    }

    public void setDisplayName(String displayName) {
        this.displayName.setText(displayName + " ?");
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void setRate(WellForm rateWell, int rating, String comment) {
        unSelectRateOptions();
        rateWell.addStyleName(Constants.ACT);
        rateBtn.setVisible(true);
        this.rating = rating;
        this.comment = comment;
    }

    private void unSelectRateOptions() {
        rateBtn.setVisible(false);
        rateWell1.removeStyleName(Constants.ACT);
        rateWell2.removeStyleName(Constants.ACT);
        rateWell3.removeStyleName(Constants.ACT);
        rateWell4.removeStyleName(Constants.ACT);
        rateWell5.removeStyleName(Constants.ACT);
    }
}
