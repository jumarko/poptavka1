package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.ValidationMonitor;
import com.eprovement.poptavka.client.common.myListBox.MyListBox;
import com.eprovement.poptavka.client.common.myListBox.MyListBoxData;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter.IEmailDialogPopupView;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import javax.validation.groups.Default;

/**
 * This class represents popup, shown when someone wants to contacts us via email.
 *
 * @author ivlcek, Martin Slavkovsky (validation)
 *
 */
public class EmailDialogPopupView extends ReverseCompositeView<EmailDialogPopupPresenter>
        implements IEmailDialogPopupView, ProvidesValidate {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static EmailDialogPopupUiBinder uiBinder = GWT.create(EmailDialogPopupUiBinder.class);

    interface EmailDialogPopupUiBinder extends UiBinder<Widget, EmailDialogPopupView> {
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) MyListBox subject;
    @UiField(provided = true) ValidationMonitor emailMonitor, reEmailMonitor, msgBodyMonitor;
    @UiField TextBox reEmailFrom;
    @UiField Modal popupPanel;
    @UiField Button sendButton, closeButton;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        // set values for subjectListBox
        createSubjectListBox();

        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
        // set values from Storage object if user is logged in
        if (Storage.getUser() != null) {
            // user is logged in so we can retrieve his email address
            emailMonitor.setValue(Storage.getUser().getEmail());
            reEmailMonitor.setValue(Storage.getUser().getEmail());
        }
        //style
        StyleResource.INSTANCE.layout().ensureInjected();
        StyleResource.INSTANCE.common().ensureInjected();
        StyleResource.INSTANCE.modal().ensureInjected();
        //popup
        popupPanel.show();
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        emailMonitor = new ValidationMonitor<EmailDialogDetail>(
                EmailDialogDetail.class, Default.class, EmailDialogDetail.Field.EMAIL_FROM.getValue());
        reEmailMonitor = new ValidationMonitor<EmailDialogDetail>(
                EmailDialogDetail.class, Default.class, EmailDialogDetail.Field.EMAIL_FROM.getValue());
        msgBodyMonitor = new ValidationMonitor<EmailDialogDetail>(
                EmailDialogDetail.class, Default.class, EmailDialogDetail.Field.MESSAGE.getValue());
    }

    private void createSubjectListBox() {
        MyListBoxData subjectData = new MyListBoxData();
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectGeneralQuestion(), Constants.SUBJECT_GENERAL_QUESTION);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectHelp(), Constants.SUBJECT_HELP);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectPartnership(), Constants.SUBJECT_PARTNERSHIP);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectReportIssue(), Constants.SUBJECT_REPORT_ISSUE);
        subjectData.insertItem(Storage.MSGS.emailDialogSubjectReportUser(), Constants.SUBJECT_REPORT_USER);
        subject = MyListBox.createListBox(subjectData, 0);
    }

    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/
    @UiHandler("reEmailFrom")
    public void validateReEmailFrom(BlurEvent e) {
        if (reEmailMonitor.getValue() == null) {
            reEmailMonitor.setValidationStyles(false, Storage.VMSGS.reRmailDialogNotBlankEmail());
        } else {
            if (reEmailMonitor.getValue().equals(emailMonitor.getValue())) {
                reEmailMonitor.setValidationStyles(true, "");
            } else {
                reEmailMonitor.setValidationStyles(false, Storage.VMSGS.reEmailDialogEmail());
            }
        }
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void hidePopup() {
        popupPanel.hide();
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
    public MyListBox getSubjectListBox() {
        return subject;
    }

    @Override
    public EmailDialogDetail getEmailDialogDetail() {
        EmailDialogDetail detail = new EmailDialogDetail();
        detail.setRecipient("pras3xer@gmail.com");
        detail.setSubject(subject.getSelected());
        detail.setEmailFrom((String) emailMonitor.getValue());
        detail.setMessage((String) msgBodyMonitor.getValue());
        return detail;
    }

    @Override
    public boolean isValid() {
        //Need to do it this way because we need all monitors perform isValid method.
        boolean valid = false;
        valid = emailMonitor.isValid() && valid;
        valid = reEmailMonitor.isValid() && valid;
        valid = msgBodyMonitor.isValid() && valid;
        return valid;
    }
}
