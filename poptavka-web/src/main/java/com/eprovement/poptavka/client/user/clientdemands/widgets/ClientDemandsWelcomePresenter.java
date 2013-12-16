/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDashboardDetail;
import com.github.gwtbootstrap.client.ui.FluidRow;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

@Presenter(view = ClientDemandsWelcomeView.class)
public class ClientDemandsWelcomePresenter extends LazyPresenter<
        ClientDemandsWelcomePresenter.ClientDemandsWelcomeViewInterface, ClientDemandsModuleEventBus> {

    public interface ClientDemandsWelcomeViewInterface extends LazyView, IsWidget {

        HTML getMyDemandsUnreadMessages();

        HTML getOfferedDemandsUnreadMessages();

        HTML getAssignedDemandsUnreadMessages();

        HTML getClosedDemandsUnreadMessages();

        FluidRow getMyDemandsRow();

        FluidRow getOfferedDemandsRow();

        FluidRow getAssignedDemandsRow();

        FluidRow getClosedDemandsRow();

        IsWidget getWidgetView();
    }

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getMyDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_DEMANDS);
            }
        }, ClickEvent.getType());
        view.getOfferedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_OFFERED_DEMANDS);
            }
        }, ClickEvent.getType());
        view.getAssignedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        }, ClickEvent.getType());
        view.getClosedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_CLOSED_DEMANDS);
            }
        }, ClickEvent.getType());
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
        view.getMyDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesMyDemandsCount()))
                    .append(Storage.MSGS.inMyDemands()).toSafeHtml()));
        view.getOfferedDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesOfferedDemandsCount()))
                    .append(Storage.MSGS.inOfferedDemands()).toSafeHtml()));
        view.getAssignedDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesAssignedDemandsCount()))
                    .append(Storage.MSGS.inAssignedDemands())).toSafeHtml());
        view.getClosedDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesClosedDemandsCount()))
                    .append(Storage.MSGS.inClosedDemands())).toSafeHtml());
    }

    private SafeHtml getNumberIntoString(int number) {
        if (number == 0) {
            return Storage.MSGS.noMessage();
        } else if (number == 1) {
            return Storage.MSGS.oneMessage();
        } else {
            return Storage.MSGS.manyMessages(Integer.toString(number));
        }
    }
}