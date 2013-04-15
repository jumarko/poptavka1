/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDashboardDetail;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientDemandsWelcomeView.class)
public class ClientDemandsWelcomePresenter extends LazyPresenter<
        ClientDemandsWelcomePresenter.ClientDemandsWelcomeViewInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsWelcomeViewInterface extends LazyView, IsWidget {

        IsWidget getWidgetView();
        Label getMyDemandsUnreadMessages();
        Label getOfferedDemandsUnreadMessages();
        Label getAssignedDemandsUnreadMessages();
        Label getClosedDemandsUnreadMessages();
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
        eventBus.getClientDashboardDetail();
        eventBus.clientDemandsMenuStyleChange(Constants.CLIENT_DEMANDS_WELCOME);
        eventBus.createTokenForHistory();
        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
    }

    /**
     * Load all data into dashboard.
     * @param dashboard
     */
    public void onLoadClientDashboardDetail(ClientDashboardDetail dashboard) {
        view.getMyDemandsUnreadMessages().setText(Storage.MSGS.youHave() + " "
                + getNumberIntoString(dashboard.getUnreadMessagesMyDemandsCount()) + " "
                + Storage.MSGS.inMyDemands());
        view.getOfferedDemandsUnreadMessages().setText(Storage.MSGS.youHave() + " "
                + getNumberIntoString(dashboard.getUnreadMessagesOfferedDemandsCount()) + " "
                + Storage.MSGS.inOfferedDemands());
        view.getAssignedDemandsUnreadMessages().setText(Storage.MSGS.youHave() + " "
                + getNumberIntoString(dashboard.getUnreadMessagesAssignedDemandsCount()) + " "
                + Storage.MSGS.inAssignedDemands());
        view.getClosedDemandsUnreadMessages().setText(Storage.MSGS.youHave() + " "
                + getNumberIntoString(dashboard.getUnreadMessagesClosedDemandsCount()) + " "
                + Storage.MSGS.inClosedDemands());
    }

    private String getNumberIntoString(int number) {
        if (number == 0) {
            return Storage.MSGS.noMessage();
        } else if (number == 1) {
            return Storage.MSGS.oneMessage();
        } else {
            return Storage.MSGS.manyMessages(Integer.toString(number));
        }
    }
}