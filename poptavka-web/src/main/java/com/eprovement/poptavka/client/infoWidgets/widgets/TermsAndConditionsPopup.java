/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * TermsAndConditionsPopup is used to show terms and conditions in a popup
 * at the end of Become Professional procedure.
 *
 * @author Jaroslav
 */
@Singleton
public final class TermsAndConditionsPopup extends Modal {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static TermsAndConditionsPopupUiBinder uiBinder =
            GWT.create(TermsAndConditionsPopupUiBinder.class);

    interface TermsAndConditionsPopupUiBinder extends UiBinder<Widget, TermsAndConditionsPopup> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField Button closeButton;

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("closeButton")
    public void closeButtonClickHandler(ClickEvent e) {
        hide();
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates contact us popup view's components.
     */
    public TermsAndConditionsPopup() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().termsAndConditions());
        setBackdrop(BackdropType.STATIC);

        setDynamicSafe(true);
    }
}