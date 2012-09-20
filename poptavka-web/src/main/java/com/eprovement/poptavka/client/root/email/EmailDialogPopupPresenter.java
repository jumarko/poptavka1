/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author ivlcek
 */
@Presenter(view = EmailDialogPopupView.class, multiple = true)
public class EmailDialogPopupPresenter
        extends LazyPresenter<EmailDialogPopupPresenter.IEmailDialogPopupView, RootEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private MailRPCServiceAsync mailService;
    private long errorId;
    private int subjectId;

    public interface IEmailDialogPopupView extends LazyView {
        HasClickHandlers getSendButton();
        HasClickHandlers getCloseButton();
        TextArea getTextArea();
        ListBox getSubjectListBox();
        TextBox getReEmailTextBox();
        TextBox getEmailTextBox();
        EmailDialogPopupPresenter getPresenter();
        void hidePopup();
    }

    @Inject
    void setMailService(MailRPCServiceAsync service) {
        mailService = service;
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    @Override
    public void bindView() {
        view.getSendButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                // TODO Martin - add validation for all fields in Contact Us form.
                String recipient = "pras3xer@gmail.com";
                String subject = view.getSubjectListBox().getValue(view.getSubjectListBox().getSelectedIndex());
                String message = extendMessageBody() + view.getTextArea().getText();
                String from = view.getEmailTextBox().getText();
                mailService.sendMail(recipient, message, subject, from, new SecuredAsyncCallback<Boolean>(eventBus) {

                    @Override
                    public void onSuccess(Boolean result) {
                        // TODO ivlcek - display success message about sending
                        GWT.log("Message has been sent to customer support");
                        hideView();
                    }
                });
            }
        });

        view.getCloseButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                hideView();
            }
        });
    }


    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onFillContactUsValues(int subjectId, long errorId) {
        this.errorId = errorId;
        this.subjectId = subjectId;
        view.getSubjectListBox().setSelectedIndex(subjectId);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/

    /**
     * Hides the EmailDialogPopupView.
     */
    public void hideView() {
        eventBus.removeHandler(view.getPresenter());
        view.hidePopup();
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/

    private String extendMessageBody() {
        if (Storage.getUser() != null) {
            return "errorId=" + errorId + "\nuserId=" + Storage.getUser().getUserId() + "\n";
        }
        return "errorId=" + errorId + "\n";
    }
}
