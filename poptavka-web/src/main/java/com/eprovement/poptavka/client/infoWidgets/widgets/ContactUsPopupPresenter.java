/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.ui.WSListBox;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.infoWidgets.InfoWidgetsEventBus;
import com.eprovement.poptavka.client.service.demand.MailRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.message.EmailDialogDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 *
 * @author ivlcek, Martin Slavkovsky
 */
@Presenter(view = ContactUsPopupView.class)
public class ContactUsPopupPresenter
        extends LazyPresenter<ContactUsPopupPresenter.IContactUsPopupView, InfoWidgetsEventBus> {

    private MailRPCServiceAsync mailService;
    private String errorId;
    private int subjectId;

    public interface IContactUsPopupView extends LazyView, IsWidget {

        //Getters
        HasClickHandlers getSendButton();

        HasClickHandlers getCloseButton();

        WSListBox getSubjectListBox();

        EmailDialogDetail getEmailDialogDetail();

        boolean isValid();

        //Setter
        ContactUsPopupView getWidgetView();

        void reset();
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
                    if (subjectId == Constants.SUBJECT_REPORT_ISSUE) {
                        dialogDetail.setMessage(extendMessageBodyByErroId() + dialogDetail.getMessage());
                    } else {
                        dialogDetail.setMessage(dialogDetail.getMessage());
                    }
                    mailService.sendMail(dialogDetail, new SecuredAsyncCallback<Boolean>(eventBus) {
                        @Override
                        public void onSuccess(Boolean result) {
                            GWT.log("Message has been sent to customer support");
                            hideView();
                            eventBus.showThankYouPopup(Storage.MSGS.thankYouContactUs(), null);
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
    public void onSendUsEmail(int subjectId, String errorId) {
        view.reset();
        this.errorId = errorId;
        this.subjectId = subjectId;
        view.getSubjectListBox().setSelectedByKey(subjectId);
        view.getWidgetView().show();
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    /**
     * Hides the EmailDialogPopupView.
     */
    public void hideView() {
        view.getWidgetView().hide();
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    private String extendMessageBodyByErroId() {
        if (Storage.getUser() != null) {
            return "errorId=" + errorId + "\nuserId=" + Storage.getUser().getUserId() + "\n";
        }
        return "errorId=" + errorId + "\n";
    }
}