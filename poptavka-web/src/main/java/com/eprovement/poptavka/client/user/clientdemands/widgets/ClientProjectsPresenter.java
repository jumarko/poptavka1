/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsEventBus;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientProjectsView.class)
public class ClientProjectsPresenter
        extends LazyPresenter<ClientProjectsPresenter.ClientProjectsLayoutInterface, ClientDemandsEventBus> {

    public interface ClientProjectsLayoutInterface extends LazyView, IsWidget {

        IsWidget getWidgetView();
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
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitClientProjects(SearchModuleDataHolder filter) {
        view.getWidgetView().asWidget().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgetView());
    }
    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**************************************************************************/
    /* Business events handled by eventbus or RPC */
    /**************************************************************************/
}