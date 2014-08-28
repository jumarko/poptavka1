/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.creditAnnouncer;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.root.RootEventBus;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jaro
 */
@Presenter(view = CreditStatusAnnouncerView.class)
public class CreditStatusAnnouncerPresenter extends
    LazyPresenter<CreditStatusAnnouncerPresenter.ICreditStatusAnnouncerView, RootEventBus> {

    private SimpleConfirmPopup popup = new SimpleConfirmPopup();

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface ICreditStatusAnnouncerView extends LazyView, IsWidget {

        IsWidget getWidgetView();

        Button getRechargeButton();

        void setCreditStatus(int credits);
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Binds events for updating categories and localities - edit, submit, close button handlers.
     */
    @Override
    public void bindView() {
        view.getRechargeButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initServicesWidget(popup.getSelectorPanel());
                popup.show();
            }
        });
        popup.getSubmitBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<ServiceDetail> serviceDetail = new ArrayList<ServiceDetail>();
                eventBus.fillServices(serviceDetail);
                eventBus.requestCreateUserService(Storage.getUser().getUserId(),
                    serviceDetail.isEmpty() ? null : serviceDetail.get(0));
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Sets credit announcer view to given panel.
     * @param creditAnnouncerPanel - holder panel
     */
    public void onSetCreditAnnouncer(SimplePanel creditAnnouncerPanel) {
        creditAnnouncerPanel.setWidget(view.getWidgetView());
        eventBus.requestCreditCount(Storage.getUser().getUserId());
    }

    /**
     * Sets credit widget status
     * @param credit - user's current credits.
     */
    public void onResponseCreditCount(Integer credit) {
        if (credit == null) {
            view.setCreditStatus(0);
        } else {
            view.setCreditStatus(credit);
        }
    }
}
