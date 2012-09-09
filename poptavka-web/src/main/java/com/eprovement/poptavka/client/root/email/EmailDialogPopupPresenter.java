/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.root.interfaces.IEmailDialogPopupView;
import com.eprovement.poptavka.client.root.interfaces.IEmailDialogPopupView.IEmailDialogPopupPresenter;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 *
 * @author ivlcek
 */
@Presenter(view = EmailDialogPopupView.class)
public class EmailDialogPopupPresenter
        extends LazyPresenter<IEmailDialogPopupView, RootEventBus> implements IEmailDialogPopupPresenter {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private MailRPCServiceAsync mailService;
    private long errorId;
    private int subjectId;

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

                String recipient = "pras3xer@gmail.com";
                String subject = view.getSubjectListBox().getValue(view.getSubjectListBox().getSelectedIndex());
                String message = extendMessageBody() + view.getTextArea().getText();
                String from = view.getEmailTextBox().getText();
                mailService.sendMail(recipient, message, subject, from, new SecuredAsyncCallback<Boolean>(eventBus) {

                    @Override
                    public void onSuccess(Boolean result) {
                        // TODO ivlcek - forward user to homeModule or Supplier/Client module if loggedIn
                        if (Storage.getUser() != null) {
                            // TODO ivlcek - display success message about sending
                            eventBus.goToClientDemandsModule(null, 0);
                        } else {
                            eventBus.goToHomeWelcomeModule(null);
                        }
                    }
                });
            }
        });
    }


    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onSendUsEmail(int subjectId, long errorId) {
        this.errorId = errorId;
        this.subjectId = subjectId;
        view.getSubjectListBox().setSelectedIndex(subjectId);
        eventBus.setBody((IsWidget) view);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/


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
