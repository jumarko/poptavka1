/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.serviceSelector;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.user.widget.grid.cell.RadioCell;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
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

        void initTableColumns(Column<ServiceDetail, Boolean> radioBtnColumn);

        void displayServices(ArrayList<ServiceDetail> services);

    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private FieldUpdater fieldUpdater;
    private ServiceDetail selected;

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
     * @param serviceType - the ServiceType
     * @param embedToWidget - holder panel
     */
    public void onInitServicesWidget(ServiceType serviceType, SimplePanel embedToWidget) {
        switch (serviceType) {
            case CLIENT:
                eventBus.requestServices(ServiceType.CLIENT);
                break;
            case SUPPLIER:
                eventBus.requestServices(ServiceType.SUPPLIER);
                break;
            default:
                break;
        }
        embedToWidget.setWidget(view);
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Creates FieldUpdater for table to handle selection.
     * Cannot use selection model because selection manager is used to manages radio buttons.
     */
    @Override
    public void bindView() {
        fieldUpdater = new FieldUpdater<ServiceDetail, Boolean>() {
            @Override
            public void update(int index, ServiceDetail object, Boolean value) {
                if (value) {
                    selected = object;
                }
            }
        };
        Column<ServiceDetail, Boolean> radioButton = new Column<ServiceDetail, Boolean>(new RadioCell()) {
            @Override
            public Boolean getValue(ServiceDetail object) {
                return object.equals(selected);
            }
        };
        radioButton.setFieldUpdater(fieldUpdater);
        view.initTableColumns(radioButton);
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
        view.displayServices(services);
        //set default selected value
        this.onSelectService(services.get(0));
    }

    /**
     * Select service by updating fieldUpdater
     * @param service
     */
    public void onSelectService(ServiceDetail service) {
        fieldUpdater.update(0, service, true);
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
        services.add(selected);
    }

    /**************************************************************************/
    /* Getter                                                                 */
    /**************************************************************************/
    /**
     * @return the selected service detail
     */
    public ServiceDetail getSelected() {
        return selected;
    }
}