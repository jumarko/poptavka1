/**
 *
 */
package com.eprovement.poptavka.shared.exceptions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;

/**
 * @author dmartin
 */
public abstract class SecurityDialogBoxes {

    private SecurityDialogBoxes() {
    }

    public static class AlertBox extends DialogBox {

        private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
        private static final String TITLE = MSGS.alert();

        public AlertBox(String message) {
            super();
            this.setAutoHideEnabled(true);
            this.setAnimationEnabled(true);
            this.setModal(true);
            this.setGlassEnabled(true);
            this.center();

            this.setTitle(TITLE);
            this.setText(TITLE);
            final HTML label = new HTML(message);
            this.add(label);
        }
    }

    public static class AccessDeniedBox extends DialogBox {

        private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
        private static final String TITLE = MSGS.securityError();

        public AccessDeniedBox() {
            super();
            this.setAutoHideEnabled(true);
            this.setAnimationEnabled(true);
            this.setModal(true);
            this.setGlassEnabled(true);
            this.center();

            this.setTitle(TITLE);
            this.setText(TITLE);
            final HTML label = new HTML(MSGS.accessDenied());
            this.add(label);
        }
    }

}
