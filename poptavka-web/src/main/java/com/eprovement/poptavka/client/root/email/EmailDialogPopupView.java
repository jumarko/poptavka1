package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter.IEmailDialogPopupView;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashSet;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;

/**
 * This class represents popup, shown when someone wants to contacts us via email.
 *
 * @author ivlcek, Martin Slavkovsky (validation)
 *
 */
public class EmailDialogPopupView extends ReverseCompositeView<EmailDialogPopupPresenter>
    implements IEmailDialogPopupView, ProvidesValidate, Editor<EmailDialogDetail> {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static EmailDialogPopupUiBinder uiBinder = GWT.create(EmailDialogPopupUiBinder.class);

    interface EmailDialogPopupUiBinder extends UiBinder<Widget, EmailDialogPopupView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    interface Driver extends SimpleBeanEditorDriver<EmailDialogDetail, EmailDialogPopupView> {
    }
    private EmailDialogPopupView.Driver driver = GWT.create(EmailDialogPopupView.Driver.class);
    private Validator validator = null;
    private EmailDialogDetail emailDialogDetail = new EmailDialogDetail();
    private Set<Integer> valid = new HashSet<Integer>();
    /**************************************************************************/
    /* OTHER ATTRIBUTES                                                       */
    /**************************************************************************/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final ValidationMessages MSGS_VALIDATION = GWT.create(ValidationMessages.class);
    @UiField Modal popupPanel;
    @UiField
    TextBox emailFrom;
    @UiField
    @Ignore
    TextBox reEmailFrom;
    @UiField(provided = true)
    @Ignore
    ListBox subject;
    @UiField
    TextArea message;
    @UiField
    @Ignore
    Button sendButton, closeButton;
    @UiField
    @Ignore
    Label emailFromErrorLabel, reEmailFromErrorLabel, messageErrorLabel;
    //Constants
    private final static String NORMAL_STYLE = StyleResource.INSTANCE.common().emptyStyle();
    private final static String ERROR_STYLE = StyleResource.INSTANCE.common().errorField();
    private final static int EMAIL_FROM = 0;
    private final static int RE_EMAIL_FROM = 1;
    private final static int MESSAGE = 2;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        // set values for subjectListBox
        subject = new ListBox();
        subject.insertItem(MSGS.emailDialogSubjectGeneralQuestion(), Constants.SUBJECT_GENERAL_QUESTION);
        subject.insertItem(MSGS.emailDialogSubjectHelp(), Constants.SUBJECT_HELP);
        subject.insertItem(MSGS.emailDialogSubjectPartnership(), Constants.SUBJECT_PARTNERSHIP);
        subject.insertItem(MSGS.emailDialogSubjectReportIssue(), Constants.SUBJECT_REPORT_ISSUE);
        subject.insertItem(MSGS.emailDialogSubjectReportUser(), Constants.SUBJECT_REPORT_USER);

        //create widget
        initWidget(uiBinder.createAndBindUi(this));
        // set values from Storage object if user is logged in
        if (Storage.getUser() != null) {
            // user is logged in so we can retrieve his email address
            emailFrom.setText(Storage.getUser().getEmail());
            reEmailFrom.setText(Storage.getUser().getEmail());
        }
        //validation
        this.driver.initialize(this);
        this.driver.edit(emailDialogDetail);
        //style
        StyleResource.INSTANCE.layout().ensureInjected();
        StyleResource.INSTANCE.common().ensureInjected();
        StyleResource.INSTANCE.modal().ensureInjected();
        //popup
        popupPanel.show();
    }

    /**************************************************************************/
    /* UIHANDLERS for validation                                              */
    /* Each field that we want to validate has its own blurEventHandler to    */
    /* allow us validate form per field right after user inputs,              */
    /* not only at the end - during submiting form                            */
    /**************************************************************************/
    @UiHandler("emailFrom")
    public void validateEmailFrom(BlurEvent e) {
        EmailDialogDetail emailDialog = driver.flush();
        Set<ConstraintViolation<EmailDialogDetail>> violations = validator.validateValue(
                EmailDialogDetail.class, "emailFrom", emailDialog.getEmailFrom(), Default.class);
        this.displayErrors(EMAIL_FROM, violations);
    }

    @UiHandler("reEmailFrom")
    public void validateReEmailFrom(BlurEvent e) {
        if (reEmailFrom.getText().isEmpty()) {
            setError(RE_EMAIL_FROM, ERROR_STYLE, MSGS_VALIDATION.reRmailDialogNotBlankEmail());
            valid.add(RE_EMAIL_FROM);
        } else {
            if (reEmailFrom.getText().equals(emailFrom.getText())) {
                setError(RE_EMAIL_FROM, NORMAL_STYLE, "");
                valid.remove(RE_EMAIL_FROM);
            } else {
                setError(RE_EMAIL_FROM, ERROR_STYLE, MSGS_VALIDATION.reEmailDialogEmail());
                valid.add(RE_EMAIL_FROM);
            }
        }
    }

    @UiHandler("message")
    public void validateMessage(BlurEvent e) {
        EmailDialogDetail emailDialog = driver.flush();
        Set<ConstraintViolation<EmailDialogDetail>> violations = validator.validateValue(
                EmailDialogDetail.class, "message", emailDialog.getMessage(), Default.class);
        this.displayErrors(MESSAGE, violations);
    }

    /**************************************************************************/
    /* VALIDATION helper methods                                              */
    /**************************************************************************/
    private void displayErrors(int item, Set<ConstraintViolation<EmailDialogDetail>> violations) {
        for (ConstraintViolation<EmailDialogDetail> violation : violations) {
            setError(item, ERROR_STYLE, violation.getMessage());
            valid.add(item);
            return;
        }
        setError(item, NORMAL_STYLE, "");
        valid.remove(item);
    }

    /**
     * Set style and error message to given item.
     *
     * @param item - use class constant CITY, STATE, STREET, ZIP
     * @param style - user class constant NORMAL_STYLE, ERROR_STYLE
     * @param errorMessage - message of item's ErrorLabel
     */
    private void setError(int item, String style, String errorMessage) {
        switch (item) {
            case EMAIL_FROM:
                this.emailFrom.setStyleName(style);
                this.emailFromErrorLabel.setText(errorMessage);
                break;
            case RE_EMAIL_FROM:
                this.reEmailFrom.setStyleName(style);
                this.reEmailFromErrorLabel.setText(errorMessage);
                break;
            case MESSAGE:
                this.message.setStyleName(style);
                this.messageErrorLabel.setText(errorMessage);
                break;
            default:
                break;
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
    public ListBox getSubjectListBox() {
        return subject;
    }

    @Override
    public EmailDialogDetail getEmailDialogDetail() {
        return emailDialogDetail;
    }

    @Override
    public boolean isValid() {
        //validate form before anouncing validation state
        //moze byt urobene aj inak, ze sa zrobi metoda na validaciu celeho objektu,
        //nie len jednotlivych properties, ale moze sa pouzit len tu.
        validateEmailFrom(null);
        validateReEmailFrom(null);
        validateMessage(null);
        return valid.isEmpty();
    }
}
