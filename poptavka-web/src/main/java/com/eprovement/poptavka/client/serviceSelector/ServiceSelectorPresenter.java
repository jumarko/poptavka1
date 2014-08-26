/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.serviceSelector.interfaces.IServiceSelectorModule;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
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
        view.getDataProvider().setList(services);
        //set default selected value
        if (!services.isEmpty()) {
            this.onSelectService(services.get(0));
        }
        view.getTable().setHeight((services.size() * 45 + 50) + "px");
    }

    /**
     * Select service by updating fieldUpdater
     * @param service
     */
    @Override
    public void onSelectService(ServiceDetail service) {
        view.getSelectionModel().setSelected(service, true);
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
        services.add(view.getSelectionModel().getSelectedObject());
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    /**
     * @return the selected service detail
     */
    public ServiceDetail getSelected() {
        return view.getSelectionModel().getSelectedObject();
    }
}
