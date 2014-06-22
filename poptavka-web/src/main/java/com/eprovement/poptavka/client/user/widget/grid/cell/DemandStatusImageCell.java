/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Clickable cell displaying star status of message.
 * Not all States are supported yet.
 *
 * NEW
 * * There are two meanings for this state. Brand new Client created Demand during registration
 * * and he must confirm email activation link. The other meaning is when Demand came from external system
 * * and we are waiting for approval to show Demand from non-registered Client.
 * * Until we receive link confirmation/approval this Demand is in state TEMPORARY.

 * TEMPORARY
 * * Registered/non-registered Client confirmed/approved TEMPORARY Demand.
 * * Operator must check this Demand and switch it to another state.
 *
 * TO_BE_CHECKED
 * * Operator checked the Demand which needs to be changed. Either some information is missing or it is a spam.
 *
 * INVALID
 * * Demand is properly described by Client and Operator switched it to ACTIVE state.
 *
 * ACTIVE
 * * No supplier were chosen for this demand and the validity of the Demand has expired.
 * * This Demand can be re-activated by Client.
 * * After Client re-activates the Demand it will go to the state of TO_BE_CHECKED.
 *
 * INACTIVE
 * * A Supplier is assigned to this Demand and is working on it.
 *
 * ASSIGNED
 * * A Supplier is assigned to this Demand and is working on it.
 *
 * FINNISHED
 * * A Supplier finished the realization of Demand and switched it to state FINISHED.
 *
 * CLOSED
 * * A Client checked FINISHED Demand and closed Demand if it was Ok.
 * * Otherwise Client switches back to ASSIGNED and Supplier has to rework it.
 *
 * CANCELED
 * * A Client or Operator canceled Demand on which the work could being done
 * * or the work has never stared for some reason.
 *
 * @author beho
 * @author Martin Slavkovsky
 * @param <C>
 *
 */
public class DemandStatusImageCell extends AbstractCell<DemandStatus> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static ImageResourceRenderer renderer;
    private PopupPanel popup = new PopupPanel(true);
    private boolean displayed;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Created DemandStatusImageCell.
     */
    public DemandStatusImageCell() {
        super(BrowserEvents.MOUSEOVER, BrowserEvents.MOUSEOUT);
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
            DemandStatus value, SafeHtmlBuilder sb) {

        setImage(value, sb);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, DemandStatus value, NativeEvent event,
            ValueUpdater<DemandStatus> valueUpdater) {
        if (BrowserEvents.MOUSEOVER.equals(event.getType())) {
            displayPopup(event, value);
        }
        if (BrowserEvents.MOUSEOUT.equals(event.getType())) {
            hidePopup();
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Displays tooltip popup.
     */
    private void displayPopup(NativeEvent event, DemandStatus demandStauts) {
        if (displayed) {
            return;
        }
        displayed = true;

        HorizontalPanel holder = new HorizontalPanel();
        SimplePanel arrow = new SimplePanel();
        arrow.addStyleName("arrow-left");

        HTMLPanel panel = new HTMLPanel(getExplanationText(demandStauts));
        panel.addStyleName("panel");

        holder.add(arrow);
        holder.add(panel);

        popup.clear();
        popup.add(holder);
        popup.addStyleName(StyleResource.INSTANCE.modal().tooltip());
        popup.setPopupPosition(event.getClientX() + 32, event.getClientY());
        popup.show();
    }

    /**
     * Hides popup
     */
    private void hidePopup() {
        displayed = false;
        popup.hide();
    }

    /**
     * Sets cell's image.
     * @param value - demand status value
     */
    private void setImage(DemandStatus value, SafeHtmlBuilder sb) {
        if (value == null) { //header
            sb.append(renderer.render(Storage.RSCS.images().demadStatus()));
            return;
        }
        switch (value) {
            case NEW:
                sb.append(renderer.render(Storage.RSCS.images().newDemand()));
                break;
            case ACTIVE:
                sb.append(renderer.render(Storage.RSCS.images().acceptIcon16()));
                break;
            case OFFERED:
                sb.append(renderer.render(Storage.RSCS.images().offeredDemand()));
                break;
            default:
                break;
        }
    }

    /**
     * Converts DemandStatus to readable string.
     * @param value - demand status value
     * @return string
     */
    private String getExplanationText(DemandStatus value) {
        if (value == null) { //header
            return Storage.MSGS.demandStatusHeader();
        }
        switch (value) {
            case NEW:
                return Storage.MSGS.demandStatusNew();
            case ACTIVE:
                return Storage.MSGS.demandStatusActive();
            case OFFERED:
                return Storage.MSGS.demandStatusOffered();
            default:
                return "";
        }
    }
}
