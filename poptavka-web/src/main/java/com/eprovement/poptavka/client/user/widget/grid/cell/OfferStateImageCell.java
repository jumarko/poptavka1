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

public class OfferStateImageCell extends AbstractCell<OfferStateType> {

    private static ImageResourceRenderer renderer;
    private PopupPanel popup = new PopupPanel(true);
    private boolean displayed;

    public OfferStateImageCell() {
        super("click", "keydown", "mouseover", "mouseout");
        if (renderer == null) {
            renderer = new ImageResourceRenderer();
        }
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context context,
            OfferStateType value, SafeHtmlBuilder sb) {

        if (value == OfferStateType.ACCEPTED) {
            sb.append(renderer.render(Storage.RSCS.images().statusWork()));
        }
        if (value == OfferStateType.PENDING) {
            sb.append(renderer.render(Storage.RSCS.images().statusWork()));
        }
        if (value == OfferStateType.DECLINED) {
            sb.append(renderer.render(Storage.RSCS.images().statusWork()));
        }
    }

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

    @Override
    protected void onEnterKeyDown(
            com.google.gwt.cell.client.Cell.Context context, Element parent,
            OfferStateType value, NativeEvent event, ValueUpdater<OfferStateType> valueUpdater) {
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
