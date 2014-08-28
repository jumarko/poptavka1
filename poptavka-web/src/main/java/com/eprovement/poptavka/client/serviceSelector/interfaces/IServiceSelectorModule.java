/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserServiceDetail;
import com.google.gwt.dom.client.FormElement;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
 * Module management.
 *
 * @author Martin Slavkovsky
 */
public interface IServiceSelectorModule {

    /**
     * Gateway interface for Service selector module.
     * Defines which methods are accessible to the rest of application.
     */
    public interface Gateway {

        @Event(forwardToParent = true)
        void initServicesWidget(SimplePanel embedToWidget);

        @Event(forwardToParent = true)
        void initServicesWidget2(SimplePanel embedToWidget, String infoLabel);

        @Event(forwardToParent = true)
        void selectService(ServiceDetail service);

        @Event(forwardToParent = true)
        void fillServices(List<ServiceDetail> service);

        @Event(forwardToParent = true)
        void requestCreateUserService(long userId, ServiceDetail serviceDetail);
    }

    public interface Handler {

        void onRequestServices(ServiceType... serviceType);

        void onRequestCreateUserService(long userId, ServiceDetail serviceDetail);
    }

    public interface Presenter {

        void onInitServicesWidget(SimplePanel embedToWidget);

        void onInitServicesWidget2(SimplePanel embedToWidget, String infoLabel);

        void onResponseCreateUserService(UserServiceDetail userServiceDetail);

        void onDisplayServices(ArrayList<ServiceDetail> services);

        void onSelectService(ServiceDetail service);

        void onFillServices(List<ServiceDetail> services);
    }

    public interface View extends LazyView, ProvidesValidate, IsWidget {

        void setInfoLabel(String text);

        void setPaymentDetails(String returnRul, UserServiceDetail userServiceDetail);

        DataGrid getTable();

        SingleSelectionModel<ServiceDetail> getSelectionModel();

        ListDataProvider<ServiceDetail> getDataProvider();

        FormElement getPaymentForm();
    }
}
