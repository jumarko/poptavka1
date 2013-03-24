package com.eprovement.poptavka.client.common;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;

public class LoadingPopupView extends Composite
        implements LoadingPopupPresenter.LoadingPopupViewInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static LoadingPopupViewUiBinder uiBinder = GWT.create(LoadingPopupViewUiBinder.class);

    interface LoadingPopupViewUiBinder extends UiBinder<Widget, LoadingPopupView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiField attributes. **/
    @UiField Modal modal;
    @UiField Label loadingMessage;
    @UiField SimpleIconLabel loader;

    /**************************************************************************/
    /* INITIALIZATIONS                                                        */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        loader.setImageResource(StyleResource.INSTANCE.images().loaderIcon33());
        StyleResource.INSTANCE.modal().ensureInjected();
    }

    public void show(String message) {
        loadingMessage.setText(message);
        modal.show();
    }

    public void hide() {
        modal.hide();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/

    @Override
    public LoadingPopupView getWidget() {
        return this;
    }
}
