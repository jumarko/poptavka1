package cz.poptavka.sample.client.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.SimpleIconLabel;
import cz.poptavka.sample.client.resources.StyleResource;

public class LoadingPopup extends Composite {

    private static LoadingPopupUiBinder uiBinder = GWT.create(LoadingPopupUiBinder.class);
    interface LoadingPopupUiBinder extends UiBinder<Widget, LoadingPopup> {    }

    @UiField SpanElement loadingMessage;
    @UiField SimpleIconLabel loader;

    public LoadingPopup(String message) {
        initWidget(uiBinder.createAndBindUi(this));
        loadingMessage.setInnerText(message);
        loader.setImageResource(StyleResource.INSTANCE.images().loadIcon32());
    }

    public void setMessage(String message) {
        loadingMessage.setInnerText(message);
    }

}
