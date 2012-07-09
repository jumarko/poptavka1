/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsEventBus;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = SupplierProjectsView.class)
public class SupplierProjectsPresenter
        extends LazyPresenter<SupplierProjectsPresenter.SupplierProjectsLayoutInterface, SupplierDemandsEventBus> {

    public interface SupplierProjectsLayoutInterface extends LazyView, IsWidget {

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
    public void onInitSupplierProjects(SearchModuleDataHolder filter) {
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