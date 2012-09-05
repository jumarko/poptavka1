/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.error;

import com.eprovement.poptavka.client.error.interfaces.IErrorView;
import com.eprovement.poptavka.client.error.interfaces.IErrorView.IErrorPresenter;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author ivlcek
 */
public class ErrorView extends ReverseCompositeView<IErrorPresenter> implements IErrorView {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static ErrorViewUiBinder uiBinder = GWT.create(ErrorViewUiBinder.class);

    interface ErrorViewUiBinder extends UiBinder<Widget, ErrorView> {
    }

    public ErrorView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField(provided = false)
    Label errorTitle;
    @UiField(provided = false)
    Label errorDescription;
    @UiField(provided = false)
    VerticalPanel messagesList = new VerticalPanel();
    private int errorResponseCode;


    /**************************************************************************/
    /* UiHandlers                                                             */
    /**************************************************************************/

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/

    /**
     * Method sets the complete output message for user based on the error response code value.
     * @param errorResponseCode the errorResponseCode to set
     */
    @Override
    public void setErrorResponseCode(int errorResponseCode) {
        this.errorResponseCode = errorResponseCode;
        messagesList.clear();
        switch (errorResponseCode) {
            case Response.SC_FORBIDDEN:      // HTTP 403
                setForbiddenMessage();
                break;
            case Response.SC_UNAUTHORIZED:   // HTTP 401
                setUnauthorizedMessage();
                break;
            case Response.SC_BAD_REQUEST:    // HTTP 400
                setBadRequestMessage();
                break;
            case Response.SC_NOT_FOUND:      // HTTP 404
                setNotFoundMessage();
                break;
            case Response.SC_SERVICE_UNAVAILABLE:   // HTTP 503
                setServiceUnavailableMessage();
                break;
            default:
                setInternalServerErrorMessage();    // HTTP 5xx
                break;
        }
        // TODO add button to send us email about technical issue
    }

    private void setForbiddenMessage() { // also called Access Denied
        errorTitle.setText(MSGS.accessDenied());
        errorDescription.setText(MSGS.accessDeniedDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.checkAccount()));
        messagesList.add(new Label(MSGS.tryRegistration()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }
    private void setUnauthorizedMessage() {
        errorTitle.setText(MSGS.notAuthorized());
        errorDescription.setText(MSGS.notAuthorizedDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.checkAccount()));
        messagesList.add(new Label(MSGS.tryRegistration()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }

    private void setBadRequestMessage() {
        errorTitle.setText(MSGS.badRequest());
        errorDescription.setText(MSGS.badRequestDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.checkWebAddress()));
        messagesList.add(new Label(MSGS.tryFromHome()));
        messagesList.add(new Label(MSGS.trySearchBox()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }

    private void setNotFoundMessage() {
        errorTitle.setText(MSGS.pageNotFound());
        errorDescription.setText(MSGS.pageNotFoundDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.checkWebAddress()));
        messagesList.add(new Label(MSGS.tryFromHome()));
        messagesList.add(new Label(MSGS.trySearchBox()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }

    private void setInternalServerErrorMessage() { // all other errors
        errorTitle.setText(MSGS.internalError());
        errorDescription.setText(MSGS.internalErrorDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.tryWaiting()));
        messagesList.add(new Label(MSGS.tryOtherBrowser()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }

    private void setServiceUnavailableMessage() {
        errorTitle.setText(MSGS.serviceUnavailable());
        errorDescription.setText(MSGS.serviceUnavailableDesc());
        messagesList.add(new Label(MSGS.pleaseTryFollowing()));
        messagesList.add(new Label(MSGS.tryWaiting()));
        messagesList.add(new Label(MSGS.reportIssue()));
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/


}
