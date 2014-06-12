/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.infoWidgets.InfoWidgetsEventBus;
import com.eprovement.poptavka.resources.StyleResource;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;
import java.util.Date;

/**
 * Alert box popup is used to notify user if something unexpected happened.
 * Contains only error message and report button.
 *
 * @author ivan
 * @author Martin Slavkovsky
 */
@Singleton
public final class AlertBoxPopup extends Modal {

    /**************************************************************************/
    /*  UiBinder                                                              */
    /**************************************************************************/
    private SecurityDialogBoxesUiBinder uiBinder = GWT.create(SecurityDialogBoxesUiBinder.class);

    interface SecurityDialogBoxesUiBinder extends UiBinder<Widget, AlertBoxPopup> {
    }

    /**************************************************************************/
    /*  CSS                                                                   */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureCommonStylesInjected();
        CssInjector.INSTANCE.ensureModalStylesInjected();
    }

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    /*  UiBinder attributes.  */
    @UiField Label header;
    @UiField Label text;
    @UiField Button reportBtn;

    /**************************************************************************/
    /*  Initialization.                                                       */
    /**************************************************************************/
    /**
     * Creates alert box popup view's compontents.
     */
    public AlertBoxPopup() {
        add(uiBinder.createAndBindUi(this));

        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        setDynamicSafe(true);
    }

    /**************************************************************************/
    /*  Popup                                                                 */
    /**************************************************************************/
    /**
     * Creates popup with given error message.
     * @param eventBus needed for report button
     * @param message that is shown to user
     */
    public void create(InfoWidgetsEventBus eventBus, String message) {
        header.setText(Storage.MSGS.errorMsgAlert());
        text.setText(message);
        addReportBtnClickHandler(reportBtn, eventBus);
        show();
    }

    /**************************************************************************/
    /*  Helper methods                                                        */
    /**************************************************************************/
    /**
     * Bind report button handler.
     * This method can be overriden to use additional action along default report button fuctionality.
     * @param button - report button
     * @param eventBus - particular eventbus
     */
    protected static void addReportBtnClickHandler(Button button, final InfoWidgetsEventBus eventBus) {
        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
            }
        });
    }
}