/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
     * Thank you popup name tells more than enough. Popup is shown for a certain
     * period (THANK_YOU_POPUP_DISPLAY_TIME constant) of time displaying given message.
     * After that, a given additional action is performed.
     *
     * @param message that is shown during popup show time.
     * @param additionalAction additional action after hide, provide null if no additional action is needed.
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
