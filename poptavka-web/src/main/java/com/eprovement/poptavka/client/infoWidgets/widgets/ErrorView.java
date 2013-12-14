/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView;
import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView.IErrorPresenter;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.github.gwtbootstrap.client.ui.Heading;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Error view used for displaying error messages for HTTP status codes:
 * 400, 401, 403, 404, 503, 5xx
 * @author ivlcek
 */
public class ErrorView extends ReverseCompositeView<IErrorPresenter> implements IErrorView {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ErrorViewUiBinder uiBinder = GWT.create(ErrorViewUiBinder.class);

    interface ErrorViewUiBinder extends UiBinder<Widget, ErrorView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = false) Heading errorTitle;
    @UiField(provided = false) Label errorDescription;
    @UiField(provided = false) VerticalPanel messagesList = new VerticalPanel();
    @UiField(provided = false) Button reportButton;
    /** Class attributes. **/
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private int errorResponseCode;
    private String errorId;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates error view's compontents.
     */
    public ErrorView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

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
    }

    /**
     * Sets erro id.
     * @param errorId string
     */
    @Override
    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @return the report button hasClickHandler
     */
    @Override
    public HasClickHandlers getReportinButton() {
        return reportButton;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Sets forbidden/access denied message for HTTPS status code 403.
     */
    private void setForbiddenMessage() {
        errorTitle.setText(MSGS.errorMsgAccessDenied());
        errorDescription.setText(MSGS.errorMsgAccessDeniedDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckAccount()));
        messagesList.add(new Label(MSGS.errorTipTryRegistration()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**
     * Sets unauthorized message for HTTPS status code 401.
     */
    private void setUnauthorizedMessage() {
        errorTitle.setText(MSGS.errorMsgNotAuthorized());
        errorDescription.setText(MSGS.errorMsgNotAuthorizedDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckAccount()));
        messagesList.add(new Label(MSGS.errorTipTryRegistration()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**
     * Sets bad request message for HTTPS status code 400.
     */
    private void setBadRequestMessage() {
        errorTitle.setText(MSGS.errorMsgBadRequest());
        errorDescription.setText(MSGS.errorMsgBadRequestDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckWebAddress()));
        messagesList.add(new Label(MSGS.errorTipTryFromHome()));
        messagesList.add(new Label(MSGS.errorTipTrySearchBox()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**
     * Sets not found message for HTTPS status code 404.
     */
    private void setNotFoundMessage() {
        errorTitle.setText(MSGS.errorMsgPageNotFound());
        errorDescription.setText(MSGS.errorMsgPageNotFoundDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckWebAddress()));
        messagesList.add(new Label(MSGS.errorTipTryFromHome()));
        messagesList.add(new Label(MSGS.errorTipTrySearchBox()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**
     * Sets internal server error message for HTTPS status code 503.
     */
    private void setInternalServerErrorMessage() { // all other errors
        errorTitle.setText(MSGS.errorMsgInternalError());
        errorDescription.setText(MSGS.errorMsgInternalErrorDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipTryWaiting()));
        messagesList.add(new Label(MSGS.errorTipTryOtherBrowser()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**
     * Sets service unavailable message for HTTPS status code 5xx.
     */
    private void setServiceUnavailableMessage() {
        errorTitle.setText(MSGS.errorMsgServiceUnavailable());
        errorDescription.setText(MSGS.errorMsgServiceUnavailableDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipTryWaiting()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }
}
