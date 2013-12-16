package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.ui.WSListBox;
import com.eprovement.poptavka.client.common.ui.WSListBoxData;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.infoWidgets.widgets.ContactUsPopupPresenter.IContactUsPopupView;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.github.gwtbootstrap.client.ui.Modal;
import com.github.gwtbootstrap.client.ui.constants.BackdropType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;

/**
 * This class represents popup, shown when someone wants to contacts us via email.
 *
 * @author ivlcek, Martin Slavkovsky (validation)
 *
 */
public class ContactUsPopupView extends Modal implements IContactUsPopupView, ProvidesValidate {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static ContactUsPopupUiBinder uiBinder = GWT.create(ContactUsPopupUiBinder.class);

    interface ContactUsPopupUiBinder extends UiBinder<Widget, ContactUsPopupView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureModalStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) WSListBox subject;
    @UiField(provided = true) ValidationMonitor emailMonitor, msgBodyMonitor;
    @UiField Button sendButton, closeButton;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        // set values for subjectListBox
        createSubjectListBox();

        initValidationMonitors();
        add(uiBinder.createAndBindUi(this));
        // set values from Storage object if user is logged in
        if (Storage.getUser() != null) {
            // user is logged in so we can retrieve his email address
            emailMonitor.setValue(Storage.getUser().getEmail());
        }
        addStyleName(StyleResource.INSTANCE.modal().commonModalStyle());
        addStyleName(StyleResource.INSTANCE.modal().contactUsModal());
        setBackdrop(BackdropType.STATIC);
        setAnimation(true);
        setDynamicSafe(true);
    }

    @Override
    public void reset() {
        emailMonitor.setValue("");
        msgBodyMonitor.setValue("");
    }
    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        emailMonitor = new ValidationMonitor<EmailDialogDetail>(
                EmailDialogDetail.class, EmailDialogDetail.Field.EMAIL_FROM.getValue());
        msgBodyMonitor = new ValidationMonitor<EmailDialogDetail>(
                EmailDialogDetail.class, EmailDialogDetail.Field.MESSAGE.getValue());
    }

    private void createSubjectListBox() {
        WSListBoxData subjectData = new WSListBoxData();
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectGeneralQuestion(), Constants.SUBJECT_GENERAL_QUESTION);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectHelp(), Constants.SUBJECT_HELP);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectPartnership(), Constants.SUBJECT_PARTNERSHIP);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectReportIssue(), Constants.SUBJECT_REPORT_ISSUE);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectReportUser(), Constants.SUBJECT_REPORT_USER);
        subject = WSListBox.createListBox(subjectData, 0);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    @Override
    public HasClickHandlers getSendButton() {
        return sendButton;
    }

    @Override
    public HasClickHandlers getCloseButton() {
        return closeButton;
    }

    @Override
    public WSListBox getSubjectListBox() {
        return subject;
    }

    @Override
    public EmailDialogDetail getEmailDialogDetail() {
        EmailDialogDetail detail = new EmailDialogDetail();
        detail.setRecipient(Storage.MSGS.supportWantSomethingEmail());
        detail.setSubject(subject.getSelected());
        detail.setEmailFrom((String) emailMonitor.getValue());
        detail.setMessage((String) msgBodyMonitor.getValue());
        return detail;
    }

    @Override
    public boolean isValid() {
        //Need to do it this way because we need all monitors perform isValid method.
        boolean valid = true;
        valid = emailMonitor.isValid() && valid;
        valid = msgBodyMonitor.isValid() && valid;
        return valid;
    }

    @Override
    public ContactUsPopupView getWidgetView() {
        return this;
    }
}