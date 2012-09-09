package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.eprovement.poptavka.client.root.interfaces.IEmailDialogPopupView;
import com.eprovement.poptavka.client.root.interfaces.IEmailDialogPopupView.IEmailDialogPopupPresenter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;



/**
 * This class represents popup, shown when someone wants to contacts us via email.
 *
 * @author ivlcek
 *
 */
public class EmailDialogPopupView extends ReverseCompositeView<IEmailDialogPopupPresenter>
        implements IEmailDialogPopupView {

    // UiBinder
    private static EmailDialogPopupViewUiBinder uiBinder = GWT.create(EmailDialogPopupViewUiBinder.class);
    private IEmailDialogPopupPresenter presenter;

    interface EmailDialogPopupViewUiBinder extends UiBinder<Widget, EmailDialogPopupView> {
    }
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    @UiField
    TextBox emailTextBox;
    @UiField
    TextBox reEmailTextBox;
    @UiField
    ListBox subjectListBox;
    @UiField
    TextArea textArea;
    @UiField
    Button sendButton;
    @UiField
    SimplePanel panel;

    public EmailDialogPopupView() {
    }

    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
//       set values from Storage
        if (Storage.getUser() != null) {
            // user is logged in so we can retrieve his email address
            getEmailTextBox().setText(Storage.getUser().getEmail());
            getReEmailTextBox().setText(Storage.getUser().getEmail());
        }
        // set values for subjectListBox
        getSubjectListBox().insertItem(MSGS.subjectGeneralQuestion(), Constants.SUBJECT_GENERAL_QUESTION);
        getSubjectListBox().insertItem(MSGS.subjectHelp(), Constants.SUBJECT_HELP);
        getSubjectListBox().insertItem(MSGS.subjectPartnership(), Constants.SUBJECT_PARTNERSHIP);
        getSubjectListBox().insertItem(MSGS.subjectReportIssue(), Constants.SUBJECT_REPORT_ISSUE);
        getSubjectListBox().insertItem(MSGS.subjectReportUser(), Constants.SUBJECT_REPORT_USER);

    }

    @Override
    public HasClickHandlers getSendButton() {
        return sendButton;
    }

    @Override
    public void setPresenter(IEmailDialogPopupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public IEmailDialogPopupPresenter getPresenter() {
        return presenter;
    }

    /**
     * @return the emailTextBox
     */
    @Override
    public TextBox getEmailTextBox() {
        return emailTextBox;
    }

    /**
     * @param emailTextBox the emailTextBox to set
     */
    public void setEmailTextBox(TextBox emailTextBox) {
        this.emailTextBox = emailTextBox;
    }

    /**
     * @return the reEmailTextBox
     */
    @Override
    public TextBox getReEmailTextBox() {
        return reEmailTextBox;
    }

    /**
     * @return the subjectListBox
     */
    @Override
    public ListBox getSubjectListBox() {
        return subjectListBox;
    }

    /**
     * @return the textArea
     */
    @Override
    public TextArea getTextArea() {
        return textArea;
    }

    /**
     * @return the panel
     */
    @Override
    public SimplePanel getPanel() {
        return panel;
    }

    /**
     * @param panel the panel to set
     */
    public void setPanel(SimplePanel panel) {
        this.panel = panel;
    }
}
