/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.detail;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author mato
 */
public class FeedbackPopupView extends PopupPanel {

    private static FeedbackPopupViewUiBinder uiBinder = GWT.create(FeedbackPopupViewUiBinder.class);

    interface FeedbackPopupViewUiBinder extends UiBinder<Widget, FeedbackPopupView> {
    }
    @UiField HTMLPanel clientPanel, supplierPanel;
    /** SupplierPanel. **/
    @UiField Label supplierName;
    /** ClientPanel. **/
    @UiField Label clientName;
    /** Rate. **/
    @UiField ListBox rating;
    @UiField TextArea comment;
    @UiField Button rateBtn;
    public static final int CLIENT = 0;
    public static final int SUPPLIER = 1;

    public FeedbackPopupView(int rateWhat) {
        super();
        this.setAutoHideEnabled(true);
        this.setAnimationEnabled(true);
        this.setModal(true);
        this.setGlassEnabled(true);
        this.center();
        this.setWidget(uiBinder.createAndBindUi(this));
        switch (rateWhat) {
            case CLIENT:
                supplierPanel.getElement().getStyle().setDisplay(Style.Display.NONE);
                clientPanel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                break;
            case SUPPLIER:
                clientPanel.getElement().getStyle().setDisplay(Style.Display.NONE);
                supplierPanel.getElement().getStyle().setDisplay(Style.Display.BLOCK);
                break;
            default:
                break;
        }
    }

    public Button getRateBtn() {
        return rateBtn;
    }

    public int getRating() {
        return Integer.parseInt(rating.getItemText(rating.getSelectedIndex()));
    }

    public String getComment() {
        return comment.getText();
    }

    public void setSupplierName(String supplierName) {
        this.supplierName.setText(supplierName);
    }

    public void setClientName(String clientName) {
        this.clientName.setText(clientName);
    }
}
