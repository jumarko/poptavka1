/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * The cell used to render categories and localities.
 *
 * @author Martin, Jaro
 */
public class CatLogSimpleCell extends AbstractCell<ICatLocDetail> {

    public CatLogSimpleCell() {
        super(BrowserEvents.MOUSEOVER, BrowserEvents.MOUSEOUT);
        CssInjector.INSTANCE.ensureModalStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private String tooltip;
    private PopupPanel popup = new PopupPanel(true);
    private boolean displayed;

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void render(Cell.Context context, ICatLocDetail value,
            SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendEscaped(value.toString());
            if (value.getParentName() != null) {
                StringBuilder tooltipBuilder = new StringBuilder();
                tooltipBuilder.append(value.getName()).append(" (");
                tooltipBuilder.append(value.getParentName());
                tooltipBuilder.append(")");
                this.tooltip = tooltipBuilder.toString();
            }
        }
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, ICatLocDetail value, NativeEvent event,
            ValueUpdater<ICatLocDetail> valueUpdater) {
        if (BrowserEvents.MOUSEOVER.equals(event.getType())) {
            displayPopup(event, parent);
        }
        if (BrowserEvents.MOUSEOUT.equals(event.getType())) {
            hidePopup();
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Displays tooltip popup only when ICatLocDetail has a parent.
     */
    private void displayPopup(NativeEvent event, Element parent) {
        if (displayed) {
            return;
        }
        if (tooltip != null) {
            displayed = true;

            VerticalPanel holder = new VerticalPanel();
            SimplePanel arrow = new SimplePanel();
            arrow.addStyleName("arrow-bottom");

            HTMLPanel panel = new HTMLPanel(tooltip);
            panel.addStyleName("panel");

            holder.add(panel);
            holder.add(arrow);

            popup.clear();
            popup.add(holder);
            popup.addStyleName(StyleResource.INSTANCE.modal().tooltip());
            popup.show();
            popup.setPopupPosition(parent.getAbsoluteLeft() + (parent.getOffsetWidth() - popup.getOffsetWidth()) / 2,
                    parent.getAbsoluteTop() - 35);
        }
    }

    /**
     * Hides tooltip popup.
     */
    private void hidePopup() {
        displayed = false;
        popup.hide();
    }
}