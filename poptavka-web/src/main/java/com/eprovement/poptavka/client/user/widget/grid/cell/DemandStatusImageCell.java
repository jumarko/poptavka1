package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.domain.demand.DemandStatus;

/**
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
 * Clickable cell displaying star status of message.
 *
 * @author beho
 * @param <C>
 *
 */
public class DemandStatusImageCell extends AbstractCell<DemandStatus> {

    private static ImageResourceRenderer renderer;
    private PopupPanel popup = new PopupPanel(true);
    private boolean displayed;

    public DemandStatusImageCell() {
        super("click", "keydown", "mouseover", "mouseout");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            DemandStatus value, SafeHtmlBuilder sb) {

        if (value == null) {
            return;
        }

        switch (value) {
            case ACTIVE:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case ASSIGNED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case CANCELED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case CLOSED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case FINISHED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case INACTIVE:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case INVALID:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case NEW:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case CRAWLED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            case TO_BE_CHECKED:
                sb.append(renderer.render(Storage.RSCS.images().statusWork()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
            Element parent, DemandStatus value, NativeEvent event,
            ValueUpdater<DemandStatus> valueUpdater) {
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

    @Override
    protected void onEnterKeyDown(
            com.google.gwt.cell.client.Cell.Context context, Element parent,
            DemandStatus value, NativeEvent event, ValueUpdater<DemandStatus> valueUpdater) {
        if (valueUpdater != null) {
            valueUpdater.update(value);
        }
    }

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

    private void hidePopup() {
        displayed = false;
        popup.hide();
    }
}
