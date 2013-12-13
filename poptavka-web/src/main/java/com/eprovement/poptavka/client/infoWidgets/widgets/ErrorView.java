/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    Heading errorTitle;
    @UiField(provided = false)
    Label errorDescription;
    @UiField(provided = false)
    VerticalPanel messagesList = new VerticalPanel();
    @UiField(provided = false)
    Button reportButton;
    private int errorResponseCode;
    private String errorId;


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
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }

    private void setForbiddenMessage() { // also called Access Denied
        errorTitle.setText(MSGS.errorMsgAccessDenied());
        errorDescription.setText(MSGS.errorMsgAccessDeniedDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckAccount()));
        messagesList.add(new Label(MSGS.errorTipTryRegistration()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }
    private void setUnauthorizedMessage() {
        errorTitle.setText(MSGS.errorMsgNotAuthorized());
        errorDescription.setText(MSGS.errorMsgNotAuthorizedDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckAccount()));
        messagesList.add(new Label(MSGS.errorTipTryRegistration()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    private void setBadRequestMessage() {
        errorTitle.setText(MSGS.errorMsgBadRequest());
        errorDescription.setText(MSGS.errorMsgBadRequestDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckWebAddress()));
        messagesList.add(new Label(MSGS.errorTipTryFromHome()));
        messagesList.add(new Label(MSGS.errorTipTrySearchBox()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    private void setNotFoundMessage() {
        errorTitle.setText(MSGS.errorMsgPageNotFound());
        errorDescription.setText(MSGS.errorMsgPageNotFoundDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipCheckWebAddress()));
        messagesList.add(new Label(MSGS.errorTipTryFromHome()));
        messagesList.add(new Label(MSGS.errorTipTrySearchBox()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    private void setInternalServerErrorMessage() { // all other errors
        errorTitle.setText(MSGS.errorMsgInternalError());
        errorDescription.setText(MSGS.errorMsgInternalErrorDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipTryWaiting()));
        messagesList.add(new Label(MSGS.errorTipTryOtherBrowser()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    private void setServiceUnavailableMessage() {
        errorTitle.setText(MSGS.errorMsgServiceUnavailable());
        errorDescription.setText(MSGS.errorMsgServiceUnavailableDesc());
        messagesList.add(new Label(MSGS.errorTipPleaseTryFollowing()));
        messagesList.add(new Label(MSGS.errorTipTryWaiting()));
        messagesList.add(new Label(MSGS.errorTipReportIssue()));
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/

    @Override
    public HasClickHandlers getReportinButton() {
        return reportButton;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
