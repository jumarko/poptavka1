/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierDashboardDetail;
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

@Presenter(view = SupplierDemandsWelcomeView.class)
public class SupplierDemandsWelcomePresenter extends LazyPresenter<
        SupplierDemandsWelcomePresenter.SupplierDemandsWelcomeViewInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierDemandsWelcomeViewInterface extends LazyView, IsWidget {

        HTML getPotentialDemandsUnreadMessages();

        HTML getOfferedDemandsUnreadMessages();

        HTML getAssignedDemandsUnreadMessages();

        HTML getClosedDemandsUnreadMessages();

        FluidRow getPotentialDemandsRow();

        FluidRow getOfferedDemandsRow();

        FluidRow getAssignedDemandsRow();

        FluidRow getClosedDemandsRow();

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
        view.getPotentialDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_POTENTIAL_DEMANDS);
            }
        }, ClickEvent.getType());
        view.getOfferedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_OFFERS);
            }
        }, ClickEvent.getType());
        view.getAssignedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        }, ClickEvent.getType());
        view.getClosedDemandsRow().addDomHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_CLOSED_DEMANDS);
            }
        }, ClickEvent.getType());
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierDemandsWelcome() {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_DEMANDS_WELCOME);
        eventBus.getSupplierDashboardDetail();
        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_DEMANDS_WELCOME);
        eventBus.displayView(view.getWidgetView());
        eventBus.createTokenForHistory();
    }

    /**
     * Load all data into dashboard.
     *
     * @param dashboard
     */
    public void onLoadSupplierDashboardDetail(SupplierDashboardDetail dashboard) {
        view.getPotentialDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesPotentialDemandsCount()))
                    .append(Storage.MSGS.inPotentialDemands())).toSafeHtml());
        view.getOfferedDemandsUnreadMessages().setHTML(((new SafeHtmlBuilder())
                .append(Storage.MSGS.youHave())
                    .append(getNumberIntoString(dashboard.getUnreadMessagesOfferedDemandsCount()))
                    .append(Storage.MSGS.inOfferedDemands())).toSafeHtml());
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