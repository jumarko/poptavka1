/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.TextArea;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ToggleButton;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author mato
 */
public class FeedbackPopupView extends Composite {

    private static FeedbackPopupViewUiBinder uiBinder = GWT.create(FeedbackPopupViewUiBinder.class);

    interface FeedbackPopupViewUiBinder extends UiBinder<Widget, FeedbackPopupView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField Modal popupFeedback, popupThankYou;
    @UiField HTMLPanel clientPanel, supplierPanel;
    /** SupplierPanel. **/
    @UiField Label supplierName;
    /** ClientPanel. **/
    @UiField Label clientName;
    /** Rate. **/
    @UiField Anchor anchorComment;
    @UiField TextArea commentArea;
    @UiField ToggleButton starBtn1, starBtn2, starBtn3, starBtn4, starBtn5;
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
        switch (rateWhat) {
            case CLIENT:
                clientPanel.setVisible(true);
                break;
            case SUPPLIER:
                supplierPanel.setVisible(true);
                break;
            default:
                break;
        }
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("anchorComment")
    public void anchorCommentClickHandler(ClickEvent e) {
        commentArea.setVisible(!commentArea.isVisible());
    }

    @UiHandler("starBtn1")
    public void ratingButton1ClickHandler(ClickEvent e) {
        unToggleOtherButtons(starBtn1);
        rating = Constants.RATE_1;
        comment = Storage.MSGS.feedbackComment1();
    }

    @UiHandler("starBtn2")
    public void ratingButton2ClickHandler(ClickEvent e) {
        unToggleOtherButtons(starBtn2);
        rating = Constants.RATE_2;
        comment = Storage.MSGS.feedbackComment2();
    }

    @UiHandler("starBtn3")
    public void ratingButton3ClickHandler(ClickEvent e) {
        unToggleOtherButtons(starBtn3);
        rating = Constants.RATE_3;
        comment = Storage.MSGS.feedbackComment3();
    }

    @UiHandler("starBtn4")
    public void ratingButton45ClickHandler(ClickEvent e) {
        unToggleOtherButtons(starBtn4);
        rating = Constants.RATE_4;
        comment = Storage.MSGS.feedbackComment4();
    }

    @UiHandler("starBtn5")
    public void ratingButton5ClickHandler(ClickEvent e) {
        unToggleOtherButtons(starBtn5);
        rating = Constants.RATE_5;
        comment = Storage.MSGS.feedbackComment5();
    }

    /**************************************************************************/
    /* GETTERS & SETTERS                                                      */
    /**************************************************************************/
    public Modal getPopupFeedback() {
        return popupFeedback;
    }

    public Modal getPopupThankYou() {
        return popupThankYou;
    }

    public Button getRateBtn() {
        return rateBtn;
    }

    public boolean isToogleRating() {
        return starBtn1.isDown()
                || starBtn2.isDown()
                || starBtn3.isDown()
                || starBtn4.isDown()
                || starBtn5.isDown();
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

    public void setSupplierName(String supplierName) {
        this.supplierName.setText(supplierName);
    }

    public void setClientName(String clientName) {
        this.clientName.setText(clientName);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void unToggleOtherButtons(ToggleButton button) {
        starBtn1.setDown(button.equals(starBtn1));
        starBtn2.setDown(button.equals(starBtn2));
        starBtn3.setDown(button.equals(starBtn3));
        starBtn4.setDown(button.equals(starBtn4));
        starBtn5.setDown(button.equals(starBtn5));
    }
}
