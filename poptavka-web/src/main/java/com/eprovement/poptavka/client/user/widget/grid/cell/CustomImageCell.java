/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * Image cell with tooltip.
 * TODO LATER Martin - create TooltipCell
 *
 * @author beho
 * @author Martin Slavkovsky
 * @param <C>
 *
 */
public class CustomImageCell extends AbstractCell<ImageResource> {

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
     * Creates CustomImageCell.
     * @param explanationText - tooltip text
     */
    public CustomImageCell(String explanationText) {
        super("click", "keydown", "mouseover", "mouseout");
        this.explanationText = explanationText;
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
            ImageResource value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.append(renderer.render(value));
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, ImageResource value, NativeEvent event,
            ValueUpdater<ImageResource> valueUpdater) {
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
            ImageResource value, NativeEvent event, ValueUpdater<ImageResource> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Displays tooltip in popup.
     */
    private void displayPopup(Element parent, NativeEvent event) {
        if (displayed) {
            return;
        }
        popup.setWidth("150px");
        popup.setHeight("150px");
        StringBuilder sb = new StringBuilder();
        sb.append(explanationText);
        popup.getElement().setInnerHTML(sb.toString());
        popup.setPopupPosition(event.getClientX() + 32, event.getClientY() + 32);
        popup.show();
        displayed = true;
    }

    /**
     * Hides popup.
     */
    private void hidePopup() {
        displayed = false;
        popup.hide();
    }
}
