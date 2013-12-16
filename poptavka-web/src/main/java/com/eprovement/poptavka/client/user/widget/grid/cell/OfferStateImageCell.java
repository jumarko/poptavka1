/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Cell for displaying offer states.
 * @author Martin Slavkovsky
 */
public class OfferStateImageCell extends AbstractCell<OfferStateType> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static ImageResourceRenderer renderer;
    private PopupPanel popup = new PopupPanel(true);
    private boolean displayed;
    private String explanationText;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates OfferStateImage cell.
     */
    public OfferStateImageCell() {
        super("click", "keydown", "mouseover", "mouseout");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            OfferStateType value, SafeHtmlBuilder sb) {

        setImageAndExplanationText(value, sb);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, OfferStateType value, NativeEvent event,
            ValueUpdater<OfferStateType> valueUpdater) {
        if (("click".equals(event.getType())) || ("keydown".equals(event.getType()))) {
            onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
        if ("mouseover".equals(event.getType())) {
            displayPopup(parent, event);
        }
        if ("mouseout".equals(event.getType())) {
            hidePopup();
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    protected void onEnterKeyDown(
            com.google.gwt.cell.client.Cell.Context context, Element parent,
            OfferStateType value, NativeEvent event, ValueUpdater<OfferStateType> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Displays tooltip popup.
     */
    private void displayPopup(Element parent, NativeEvent event) {
        if (displayed) {
            return;
        }
        popup.setWidth("150px");
        popup.setHeight("150px");
        StringBuilder sb = new StringBuilder();
        sb.append("<center>Some explanation text?</center>");
        popup.getElement().setInnerHTML(sb.toString());
        popup.setPopupPosition(event.getClientX() + 32, event.getClientY() + 32);
        popup.show();
        displayed = true;
    }

    /**
     * Hides tooltip popup.
     */
    private void hidePopup() {
        displayed = false;
        popup.hide();
    }

    /**
     * Sets cell's image and tooltip text.
     * @param value - offer state type
     */
    private void setImageAndExplanationText(OfferStateType value, SafeHtmlBuilder sb) {
        switch (value) {
            case ACCEPTED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                explanationText = Storage.MSGS.offerStateAccepted();
                break;
            case PENDING:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                explanationText = Storage.MSGS.offerStatePending();
                break;
            case DECLINED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                explanationText = Storage.MSGS.offerStateDeclined();
                break;
            default:
                break;
        }
    }
}
