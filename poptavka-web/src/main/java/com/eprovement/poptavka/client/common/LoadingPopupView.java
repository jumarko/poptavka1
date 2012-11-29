package com.eprovement.poptavka.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.resources.StyleResource;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class LoadingPopupView extends PopupPanel
        implements LoadingPopupPresenter.LoadingPopupViewInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static LoadingPopupViewUiBinder uiBinder = GWT.create(LoadingPopupViewUiBinder.class);

    interface LoadingPopupViewUiBinder extends UiBinder<Widget, LoadingPopupView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //Constants
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 35;
    //UiField attributes
    @UiField
    Label loadingMessage;
    @UiField
    SimpleIconLabel loader;
    @UiField
    Anchor reportAnchor;

    /**************************************************************************/
    /* INITIALIZATIONS                                                        */
    /**************************************************************************/
    @Override
    public void createView() {
        setWidget(uiBinder.createAndBindUi(this));

        loader.setImageResource(StyleResource.INSTANCE.images().loadIcon32());
        //robi problemy - sposobuje prekrytie obsahu loading widgetu glass panelom
//        setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());

        setModal(true);
        setGlassEnabled(true);
        center();
    }

    public void show(String message) {
//        setPopupPosition((Window.getClientWidth() / 2) - OFFSET_X,
//                (Window.getClientHeight() / 2) - OFFSET_Y);
        loadingMessage.setText(message);
        show();
    }

    public void show(String message, Widget anchor) {
        loadingMessage.setText(message);

        int top = anchor.getAbsoluteTop() + (anchor.getOffsetHeight() / 2);
        int left = anchor.getAbsoluteLeft() + (anchor.getOffsetWidth() / 2)
                - OFFSET_X;
        showRelativeTo(anchor);
        GWT.log("AbsoluteLeft: " + anchor.getAbsoluteLeft() + " OffsetWidth: "
                + (anchor.getOffsetWidth()));
        GWT.log("AbsoluteTop: " + anchor.getAbsoluteTop() + " Offsetheight: "
                + (anchor.getOffsetHeight()));

        GWT.log("L: " + left + " T: " + top);
        show();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public HasClickHandlers getReportButton() {
        return reportAnchor;
    }

    @Override
    public LoadingPopupView getWidget() {
        return this;
    }
}
