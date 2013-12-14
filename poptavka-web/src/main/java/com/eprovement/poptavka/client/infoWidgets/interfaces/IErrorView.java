/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.infoWidgets.interfaces;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

/**
 * Error view interface.
 * @author ivlcek
 */
public interface IErrorView extends IsWidget {

    /**
     * Error presenter interface.
     */
    public interface IErrorPresenter {
    }

    void setErrorResponseCode(int errorResponseCode);

    void setErrorId(String errorId);

    HasClickHandlers getReportinButton();

    Widget getWidgetView();
}