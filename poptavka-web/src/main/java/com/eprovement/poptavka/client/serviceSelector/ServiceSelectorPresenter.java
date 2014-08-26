/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
 * ServiceSelector presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = ServiceSelectorView.class)
public class ServiceSelectorPresenter extends LazyPresenter<
        ServiceSelectorPresenter.SupplierServiceInterface, ServiceSelectorEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface SupplierServiceInterface extends LazyView, ProvidesValidate, IsWidget {

        DataGrid getTable();

        SingleSelectionModel<ServiceDetail> getSelectionModel();

        ListDataProvider<ServiceDetail> getDataProvider();

    }

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
    public void onInitServicesWidget(SimplePanel embedToWidget) {
        if (Storage.getUser() == null) {
            eventBus.requestServices(ServiceType.PROMOTION, ServiceType.RECHARGE);
        } else {
            eventBus.requestServices(ServiceType.RECHARGE);
        }
        embedToWidget.setWidget(view);
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Set retrieved services and select the first one.
     *
     * @param services
     */
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
    public void onSelectService(ServiceDetail service) {
        view.getSelectionModel().setSelected(service, true);
    }

    /**
     * Set selected services into given list.
     * If given list null, initialize it first.
     * @param services
     */
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
