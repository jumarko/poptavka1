package cz.poptavka.sample.client.user.widget.grid.cell;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.ImageResourceRenderer;
import com.google.gwt.user.client.ui.PopupPanel;

import cz.poptavka.sample.client.main.Storage;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;

public class StatusImageCell extends AbstractCell<DemandStatusType> {

    private static ImageResourceRenderer renderer;

    private PopupPanel popup = new PopupPanel(true);

    private boolean displayed;

    public StatusImageCell() {
        super("click", "keydown", "mouseover", "mouseout");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            DemandStatusType value, SafeHtmlBuilder sb) {

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
            case TEMPORARY:
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
            Element parent, DemandStatusType value, NativeEvent event,
            ValueUpdater<DemandStatusType> valueUpdater) {
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
            DemandStatusType value, NativeEvent event, ValueUpdater<DemandStatusType> valueUpdater) {
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
