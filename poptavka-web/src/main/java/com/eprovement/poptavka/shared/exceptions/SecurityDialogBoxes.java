/**
 *
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.mvp4g.client.event.EventBusWithLookup;

/**
 * @author dmartin
 */
public abstract class SecurityDialogBoxes {

    private SecurityDialogBoxes() {
    }

    public static class AlertBox extends DialogBox {

        private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
        private static final String TITLE = MSGS.alert();

        public AlertBox(EventBusWithLookup eventBusWithLookup, String message) {
            super();
            this.setAutoHideEnabled(true);
            this.setAnimationEnabled(true);
            this.setModal(true);
            this.setGlassEnabled(true);
            this.center();

            this.setTitle(TITLE);
            this.setText(TITLE);
            VerticalPanel vp = new VerticalPanel();
            vp.add(new HTML(message));
            vp.add(SecurityDialogBoxes.getReportButton(eventBusWithLookup, message));

            this.setWidget(vp);
        }
    }

    public static class AccessDeniedBox extends DialogBox {

        private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
        private static final String TITLE = MSGS.securityError();

        public AccessDeniedBox(EventBusWithLookup eventBusWithLookup) {
            super();
            this.setAutoHideEnabled(true);
            this.setAnimationEnabled(true);
            this.setModal(true);
            this.setGlassEnabled(true);
            this.center();

            this.setTitle(TITLE);
            this.setText(TITLE);

            VerticalPanel vp = new VerticalPanel();
            vp.add(new HTML(MSGS.accessDenied()));
            vp.add(SecurityDialogBoxes.getReportButton(eventBusWithLookup, MSGS.accessDenied()));

            this.setWidget(vp);
        }
    }

    protected static Anchor getReportButton(final EventBusWithLookup eventBus, final String errorId) {
        Anchor reportButton = new Anchor(Storage.MSGS.report());
        reportButton.setStyleName("font-size:1.0em");
        if (eventBus instanceof BaseChildEventBus) {
            reportButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ((BaseChildEventBus) eventBus).sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, errorId);
                }
            });
        } else if (eventBus instanceof RootEventBus) {
            reportButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ((RootEventBus) eventBus).sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, errorId);
                }
            });
        }
        return reportButton;
    }
}
