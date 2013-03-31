/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.email;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.ListBox;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author ivlcek, Martin Slavkovsky (validation)
 */
@Presenter(view = EmailDialogPopupView.class, multiple = true)
public class EmailDialogPopupPresenter
        extends LazyPresenter<EmailDialogPopupPresenter.IEmailDialogPopupView, RootEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private MailRPCServiceAsync mailService;
    private String errorId;
    private int subjectId;

    public interface IEmailDialogPopupView extends LazyView {

        //Getters
        EmailDialogPopupPresenter getPresenter();

        HasClickHandlers getSendButton();

        HasClickHandlers getCloseButton();

        ListBox getSubjectListBox();

        EmailDialogDetail getEmailDialogDetail();

        boolean isValid();

        //Setter
        void hidePopup();
    }

    /**************************************************************************/
    /* Inject RPC service                                                     */
    /**************************************************************************/
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

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getSendButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (view.isValid()) {
                    EmailDialogDetail dialogDetail = view.getEmailDialogDetail();
                    dialogDetail.setRecipient("pras3xer@gmail.com");
                    dialogDetail.setMessage(extendMessageBody() + dialogDetail.getMessage());
                    dialogDetail.setSubject(view.getSubjectListBox().getValue(
                            view.getSubjectListBox().getSelectedIndex()));
                    mailService.sendMail(dialogDetail, new SecuredAsyncCallback<Boolean>(eventBus) {
                        @Override
                        public void onSuccess(Boolean result) {
                            // TODO ivlcek - display success message about sending
                            GWT.log("Message has been sent to customer support");
                            hideView();
                        }
                    });
                }
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
    /**
     * Contact us popup will will be prefilled with values as subject and errorId.
     *
     * @param subject - predefined subject i.e. report issue that was invoked by user from Error Module
     * @param errorId - the error ID what was genereated for reported issue
     */
    public void fillContactUsValues(int subjectId, String errorId) {
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
