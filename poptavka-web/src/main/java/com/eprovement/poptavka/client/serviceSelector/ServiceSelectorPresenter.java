/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.client.serviceSelector.serviceItem.ServiceItem;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceSelector presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ServiceSelectorView.class)
public class ServiceSelectorPresenter extends LazyPresenter<IServiceSelectorModule.View, ServiceSelectorEventBus>
    implements IServiceSelectorModule.Presenter {

    private ServiceDetail selectedServiceDetail;
    private ClickHandler clickHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            view.reset();
            ServiceItem serviceItem = (ServiceItem) event.getSource();
            serviceItem.setSelected(true);
            selectedServiceDetail = serviceItem.getServiceDetail();
        }
    };

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        //nothing by default
    }

    public void onForward() {
        //nothing by default
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Initialize ServiceSelector widget.
     * @param embedToWidget - holder panel
     */
    @Override
    public void onInitServicesWidget(SimplePanel embedToWidget) {
        if (Storage.getUser() == null) {
            eventBus.requestServices(ServiceType.PROMOTION, ServiceType.RECHARGE);
        } else {
            eventBus.requestServices(ServiceType.RECHARGE);
        }
        embedToWidget.setWidget(view);
    }

    /**
     * Initialize ServiceSelector widget.
     * @param embedToWidget - holder panel
     * @param infoLabel text displayed above the widget
     */
    @Override
    public void onInitServicesWidget2(SimplePanel embedToWidget, String infoLabel) {
        view.setInfoLabel(infoLabel);
        eventBus.initServicesWidget(embedToWidget);
    }

    @Override
    public void onResponseCreateUserService(UserServiceDetail userServiceDetail) {
        view.setPaymentDetails("https://devel.want-something.com:8443", userServiceDetail);
        eventBus.loadingHide();
        eventBus.loadingShow("Forwarding to paypal");
        view.getPaymentForm().submit();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Set retrieved services and select the first one.
     *
     * @param services
     */
    @Override
    public void onDisplayServices(ArrayList<ServiceDetail> services) {
        boolean selectFirst = true;
        int item = 1;
        for (ServiceDetail serviceDetail : services) {
            ServiceItem serviceItem = new ServiceItem(serviceDetail);
            serviceItem.addClickHandler(clickHandler);
            serviceItem.setSelected(selectFirst);
            if (selectFirst) {
                serviceItem.addStyleName("first");
            }
            selectFirst = false;
            serviceItem.getIcon().addStyleName("icon" + item);
            item++;
            view.getServicesHolder().add(serviceItem);
        }
    }

    /**
     * Select service by updating fieldUpdater
     * @param service
     */
    @Override
    public void onSelectService(ServiceDetail service) {
        for (int i = 0; i < view.getServicesHolder().getWidgetCount(); i++) {
            ServiceItem serviceItem = (ServiceItem) view.getServicesHolder().getWidget(i);
            if (serviceItem.getServiceDetail().equals(service)) {
                serviceItem.setSelected(true);
            }
        }
    }

    /**
     * Set selected services into given list.
     * If given list null, initialize it first.
     * @param services
     */
    @Override
    public void onFillServices(List<ServiceDetail> services) {
        if (services == null) {
            services = new ArrayList<ServiceDetail>();
        } else {
            services.clear();
        }
        services.add(selectedServiceDetail);
    }
}
