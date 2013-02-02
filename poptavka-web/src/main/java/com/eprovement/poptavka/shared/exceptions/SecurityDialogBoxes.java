package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.BaseChildEventBus;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.ModalFooter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.Date;

/**
 * @author ivan
 * @author Martin Slavkovsky (2.2.2013 added uiBinder)
 */
public final class SecurityDialogBoxes extends Composite {

    /**************************************************************************/
    /**  UiBinder.                                                           **/
    /**************************************************************************/
    private SecurityDialogBoxesUiBinder uiBinder = GWT.create(SecurityDialogBoxesUiBinder.class);

    interface SecurityDialogBoxesUiBinder extends UiBinder<Widget, SecurityDialogBoxes> {
    }

    /**************************************************************************/
    /**  Initialization.                                                     **/
    /**************************************************************************/
    private SecurityDialogBoxes() {
        super();
        initWidget(uiBinder.createAndBindUi(this));
    }
    /**************************************************************************/
    /**  Singleton definition.                                               **/
    /**************************************************************************/
    private static SecurityDialogBoxes instance = null;

    public static SecurityDialogBoxes getInstance() {
        if (instance == null) {
            instance = new SecurityDialogBoxes();
        }
        return instance;
    }
    /**************************************************************************/
    /**  showAlertBox.                                                           **/
    /**************************************************************************/
    /** UiBinder attributes. **/
    static @UiField Modal alertBoxModal;
    static @UiField ModalFooter alertBoxFooter;
    static @UiField Label alertBoxLabel;
    static @UiField Button alertBoxReportBtn;

    /** Setup bootstrap modal popup. **/
    public static void showAlertBox(EventBusWithLookup eventBusWithLookup, String message) {
        //initialize SecurityDialogBox - need to call his constructor to create uiBinder.
        getInstance();
        //set up popup attributes
        alertBoxLabel.setText(message);
        addReportBtnClickHandler(alertBoxReportBtn, eventBusWithLookup);
        //show alert box popup
        alertBoxModal.show();
    }
    /**************************************************************************/
    /**  showAccessDeniedBox.                                                    **/
    /**************************************************************************/
    /** UiBinder attributes. **/
    static @UiField Modal accessDeniedBoxModal;
    static @UiField ModalFooter accessDeniedBoxFooter;
    static @UiField Label accessDeniedBoxLabel;
    static @UiField Button accessDeniedBoxReportBtn;

    /** Setup bootstrap modal popup. **/
    public static void showAccessDeniedBox(EventBusWithLookup eventBusWithLookup) {
        //initialize SecurityDIalogBox - need to call his constructor to create uiBinder.
        getInstance();
        //set up popup attributes
        accessDeniedBoxModal.setTitle(Storage.MSGS.errorMsgSecurityError());
        accessDeniedBoxLabel.setText(Storage.MSGS.errorMsgAccessDenied());
        addReportBtnClickHandler(accessDeniedBoxReportBtn, eventBusWithLookup);
        //show access denied box popup
        accessDeniedBoxModal.show();
    }

    /**************************************************************************/
    /*  Helper methods                                                        */
    /**************************************************************************/
    protected static void addReportBtnClickHandler(Button button, final EventBusWithLookup eventBus) {
        if (eventBus instanceof BaseChildEventBus) {
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ((BaseChildEventBus) eventBus).sendUsEmail(
                            Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
                }
            });
        } else if (eventBus instanceof RootEventBus) {
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    ((RootEventBus) eventBus).sendUsEmail(
                            Constants.SUBJECT_REPORT_ISSUE, (new Date()).toString());
                }
            });
        }
    }
}
