/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.smallPopups;

import com.eprovement.poptavka.client.common.session.Constants;
import static com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.getInstance;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public final class ThankYouPopup extends Composite {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ThankYouPopupUiBinder uiBinder = GWT.create(ThankYouPopupUiBinder.class);

    interface ThankYouPopupUiBinder extends UiBinder<Widget, ThankYouPopup> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    static @UiField Modal popupThankYou;
    static @UiField Label messageThankYou;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    private ThankYouPopup() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Singleton definition                                                   */
    /**************************************************************************/
    private static ThankYouPopup instance = null;

    public static ThankYouPopup getInstance() {
        if (instance == null) {
            instance = new ThankYouPopup();
        }
        return instance;
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
    public static void create(String message, Timer additionalAction) {
        getInstance();
        messageThankYou.setText(message);
        popupThankYou.show();
        Timer togglebuttonTimer = new Timer() {
            @Override
            public void run() {
                popupThankYou.hide();
            }
        };
        togglebuttonTimer.schedule(Constants.THANK_YOU_POPUP_DISPLAY_TIME);
        if (additionalAction != null) {
            additionalAction.schedule(Constants.THANK_YOU_POPUP_DISPLAY_TIME);
        }
    }
}
