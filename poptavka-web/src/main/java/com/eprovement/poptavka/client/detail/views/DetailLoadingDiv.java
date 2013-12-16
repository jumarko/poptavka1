package com.eprovement.poptavka.client.detail.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class DetailLoadingDiv extends UIObject {

    private static LoadingDivUiBinder uiBinder = GWT.create(LoadingDivUiBinder.class);

    interface LoadingDivUiBinder extends UiBinder<Element, DetailLoadingDiv> {
    }

    @UiField DivElement loading;

    public DetailLoadingDiv() {
        setElement(uiBinder.createAndBindUi(this));
    }

    public DetailLoadingDiv(Widget holderWidget) {
        setElement(uiBinder.createAndBindUi(this));
        holderWidget.getElement().appendChild(this.getElement());
    }

}
