/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 * Thank you popup is a bootstrap modal for displaying info messages about workflow.
 * Popup is shown for a certain period (THANK_YOU_POPUP_DISPLAY_TIME constant)
 * of time displaying given message.
 * After that, a given additional action is performed.
 *
 * @author Martin Slavkovsky
 */
@Singleton
public final class ThankYouPopup extends Modal {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ThankYouPopupUiBinder uiBinder = GWT.create(ThankYouPopupUiBinder.class);

    interface ThankYouPopupUiBinder extends UiBinder<Widget, ThankYouPopup> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField HTML messageThankYou;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates thank you view's compotents.
     */
    public ThankYouPopup() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().thankYouPopupStyle());
        setBackdrop(BackdropType.STATIC);
        setKeyboard(false);
        setAnimation(true);
        setDynamicSafe(true);
    }

    /**************************************************************************/
    /* Popup                                                                  */
    /**************************************************************************/
    /**
     * Creates thank you popup with given message and timer.
     * @param message that is shown during popup show time.
     * @param additionalAction define how long will be popup displayed and following action.
     * Provide null if default timer with no action is enough.
     */
    public void create(SafeHtml message, Timer additionalAction) {
        messageThankYou.setHTML(message);
        show();
        Timer togglebuttonTimer = new Timer() {
            @Override
            public void run() {
                hide();
            }
        };
        togglebuttonTimer.schedule(Constants.THANK_YOU_POPUP_DISPLAY_TIME);
        if (additionalAction != null) {
            additionalAction.schedule(Constants.THANK_YOU_POPUP_DISPLAY_TIME);
        }
    }
}
