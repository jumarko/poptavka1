/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.infoWidgets.InfoWidgetsEventBus;
import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView;
import com.eprovement.poptavka.client.infoWidgets.interfaces.IErrorView.IErrorPresenter;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 * Displays all erroneous states on body page.
 * <b><i>Note:</i></b>
 * This widget is used to display error message in body if something unexpected happens.
 * Unlike AlertBoxPopup this allow us showing more information.
 * Used for displaying error messages for HTTP status codes: 400, 401, 403, 404, 503, 5xx
 *
 * @author ivlcek, Martin Slavkovsky
 */
@Presenter(view = ErrorView.class)
public class ErrorPresenter extends BasePresenter<IErrorView, InfoWidgetsEventBus> implements
    IErrorPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private String errorId;

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Bind report button hadnler.
     */
    @Override
    public void bind() {
        view.getReportinButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                // display popup for reporting error to customer support
                eventBus.sendUsEmail(Constants.SUBJECT_REPORT_ISSUE, errorId);
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Displays error.
     * @param errorResponseCode HTTP status code
     * @param errorId error id
     */
    public void onDisplayError(int errorResponseCode, String errorId) {
        this.errorId = errorId;
        view.setErrorResponseCode(errorResponseCode);
        view.setErrorId(errorId);
        eventBus.setBody(view.getWidgetView());
        eventBus.setToolbarContent("Error", null);
    }
}