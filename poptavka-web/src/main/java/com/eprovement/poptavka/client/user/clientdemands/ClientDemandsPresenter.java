/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands;

import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientDemandsView.class)
public class ClientDemandsPresenter
        extends LazyPresenter<ClientDemandsPresenter.ClientDemandsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientDemandsLayoutInterface extends LazyView, IsWidget {

        IsWidget getWidgetView();
    }

    /** *********************************************************************** */
    /* General Module events */
    /** *********************************************************************** */
    public void onStart() {
        // nothing
    }

    public void onForward() {
        // nothing
    }

    /** *********************************************************************** */
    /* Navigation events */
    /** *********************************************************************** */
    public void onGoToClientDemandsModule(SearchModuleDataHolder searchModuleDataHolder, int loadWidget) {
    }
    /** *********************************************************************** */
    /* Business events handled by presenter */
    /** *********************************************************************** */
    /** *********************************************************************** */
    /* Business events handled by eventbus or RPC */
    /** *********************************************************************** */
}