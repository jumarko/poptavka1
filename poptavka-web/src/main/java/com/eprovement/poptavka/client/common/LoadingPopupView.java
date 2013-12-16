package com.eprovement.poptavka.client.common;

import com.eprovement.poptavka.client.common.LoadingPopupPresenter.LoadingPopupViewInterface;
import com.eprovement.poptavka.resources.StyleResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;

import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.user.client.ui.Label;

public class LoadingPopupView extends Modal implements LoadingPopupViewInterface {

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
    @UiField Label loadingMessage;
    @UiField SimpleIconLabel loader;

    /**************************************************************************/
    /* INITIALIZATIONS                                                        */
    /**************************************************************************/
    @Override
    public void createView() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.initial().loaderModal());
        setWidth(200);
        setBackdrop(BackdropType.STATIC);
        setKeyboard(false);
        setDynamicSafe(true);
        setHideOthers(false);

        loader.setImageResource(StyleResource.INSTANCE.images().loaderIcon33());
    }

    public void show(String message) {
        loadingMessage.setText(message);
        show();
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/

    @Override
    public LoadingPopupView getWidget() {
        return this;
    }
}