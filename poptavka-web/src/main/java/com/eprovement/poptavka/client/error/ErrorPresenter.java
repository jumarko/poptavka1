/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.error;

import com.eprovement.poptavka.client.error.interfaces.IErrorView;
import com.eprovement.poptavka.client.error.interfaces.IErrorView.IErrorPresenter;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

/**
 *
 * @author ivlcek
 */
@Presenter(view = ErrorView.class)
public class ErrorPresenter extends BasePresenter<IErrorView, ErrorEventBus> implements
        IErrorPresenter {

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // TODO ivlcek - is the line bellow necessary? Ask Martin
//        eventBus.setUpSearchBar(null, false, false, false);
    }


    /**
     * Bind objects and their action handlers.
     */
//    @Override
//    public void bind() {
//    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    public void onDisplayError(int errorResponseCode) {
        view.setErrorResponseCode(errorResponseCode);
    }

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/


    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/

}

