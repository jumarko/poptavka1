/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.error.interfaces;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.IsWidget;

/**
 *
 * @author ivlcek
 */
public interface IErrorView extends IsWidget {

    public interface IErrorPresenter {
    }

    void setErrorResponseCode(int errorResponseCode);

    void setErrorId(long errorId);

    HasClickHandlers getReportinButton();
}