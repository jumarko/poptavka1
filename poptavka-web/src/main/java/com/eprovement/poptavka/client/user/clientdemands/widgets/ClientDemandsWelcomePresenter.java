/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientDemandsWelcomeView.class)
public class ClientDemandsWelcomePresenter extends LazyPresenter<
        ClientDemandsWelcomePresenter.ClientDemandsWelcomeViewInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsWelcomeViewInterface extends LazyView, IsWidget {

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
    public void onInitClientDemandsWelcome() {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS_WELCOME);
        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        eventBus.createTokenForHistory();
    }
}