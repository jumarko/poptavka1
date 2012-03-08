package cz.poptavka.sample.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.domain.demand.DemandStatus;

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
public class StateCell extends AbstractCell<DemandStatus> {

    private static ImageResourceRenderer renderer;
    //constants
    private static final ImageResource NEW = Storage.RSCS.images().starGold();
    private static final ImageResource TEMPORARY = Storage.RSCS.images().starSilver();
    private static final ImageResource TO_BE_CHECKED = Storage.RSCS.images().starSilver();
    private static final ImageResource INVALID = Storage.RSCS.images().starSilver();
    private static final ImageResource ACTIVE = Storage.RSCS.images().starSilver();
    private static final ImageResource INACTIVE = Storage.RSCS.images().starSilver();
    private static final ImageResource ASSIGNED = Storage.RSCS.images().starSilver();
    private static final ImageResource FINISHED = Storage.RSCS.images().starSilver();
    private static final ImageResource CLOSED = Storage.RSCS.images().starSilver();
    private static final ImageResource CANCELED = Storage.RSCS.images().starSilver();

    public StateCell() {
        super("click", "keydown");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            DemandStatus value, SafeHtmlBuilder sb) {
        if (value != null) {
            if (value == DemandStatus.NEW) {
                sb.append(renderer.render(NEW));
            } else if (value == DemandStatus.TEMPORARY) {
                sb.append(renderer.render(TEMPORARY));
            } else if (value == DemandStatus.TO_BE_CHECKED) {
                sb.append(renderer.render(TO_BE_CHECKED));
            } else if (value == DemandStatus.INVALID) {
                sb.append(renderer.render(INVALID));
            } else if (value == DemandStatus.ACTIVE) {
                sb.append(renderer.render(ACTIVE));
            } else if (value == DemandStatus.INACTIVE) {
                sb.append(renderer.render(INACTIVE));
            } else if (value == DemandStatus.ASSIGNED) {
                sb.append(renderer.render(ASSIGNED));
            } else if (value == DemandStatus.FINISHED) {
                sb.append(renderer.render(FINISHED));
            } else if (value == DemandStatus.CLOSED) {
                sb.append(renderer.render(CLOSED));
            } else if (value == DemandStatus.CANCELED) {
                sb.append(renderer.render(CANCELED));
            }
        }
    }
//    @Override
//    public void onBrowserEvent(com.google.gwt.cell.client.Cell.Context context,
//            Element parent, DemandStatus value, NativeEvent event,
//            ValueUpdater<Boolean> valueUpdater) {
//        if (("click".equals(event.getType())) || ("keydown".equals(event.getType()))) {
//            onEnterKeyDown(context, parent, value, event, valueUpdater);
//        }
//    }
//
//    @Override
//    protected void onEnterKeyDown(
//            com.google.gwt.cell.client.Cell.Context context, Element parent,
//            DemandStatus value, NativeEvent event, ValueUpdater<Boolean> valueUpdater) {
//        if (valueUpdater != null) {
//            valueUpdater.update(value);
//        }
//    }
}
