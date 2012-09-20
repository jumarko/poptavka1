package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupPresenter.IEmailDialogPopupView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.view.ReverseViewInterface;

/**
 * This class represents popup, shown when someone wants to contacts us via email.
 *
 * @author ivlcek
 *
 */
public class EmailDialogPopupView extends PopupPanel
        implements ReverseViewInterface<EmailDialogPopupPresenter>, IEmailDialogPopupView {

    private EmailDialogPopupPresenter presenter;
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private TextBox emailTextBox;
    private TextBox reEmailTextBox;
    private ListBox subjectListBox;
    private TextArea textArea;
    private Button sendButton;
    private Button closeButton;

    public EmailDialogPopupView() {
    }

    @Override
    public void createView() {
        // init widgets
        VerticalPanel vp = new VerticalPanel();

        HorizontalPanel hp1 = new HorizontalPanel();
        Label to = new Label(MSGS.to());
        Label customerSupport = new Label(MSGS.customerSupport());
        hp1.add(to);
        hp1.add(customerSupport);

        Label subject = new Label(MSGS.subject());
        subjectListBox = new ListBox();
        Label enterYourEmail = new Label(MSGS.enverYourEmail());
        emailTextBox = new TextBox();
        Label reEnterYourEmail = new Label(MSGS.reEnterYourEmail());
        reEmailTextBox = new TextBox();
        Label qustionOrConcern = new Label(MSGS.questionOrConcern());
        textArea = new TextArea();
        textArea.setWidth("400px");
        textArea.setHeight("150px");
        Label maximumChars = new Label(MSGS.maximumChars());

        HorizontalPanel hp2 = new HorizontalPanel();
        sendButton = new Button(MSGS.sendButton());
        closeButton = new Button(MSGS.close());
        hp2.add(sendButton);
        hp2.add(closeButton);

        vp.add(hp1);
        vp.add(subject);
        vp.add(subjectListBox);
        vp.add(enterYourEmail);
        vp.add(emailTextBox);
        vp.add(reEnterYourEmail);
        vp.add(reEmailTextBox);
        vp.add(qustionOrConcern);
        vp.add(textArea);
        vp.add(maximumChars);
        vp.add(hp2);

        // set values from Storage object if user is logged in
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

        setWidget(vp);
        this.setModal(true);
        this.setGlassEnabled(true);
        this.center();
        this.show();
    }

    @Override
    public HasClickHandlers getSendButton() {
        return sendButton;
    }

    @Override
    public HasClickHandlers getCloseButton() {
        return closeButton;
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

    @Override
    public void setPresenter(EmailDialogPopupPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public EmailDialogPopupPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void hidePopup() {
        this.hide();
    }
}
