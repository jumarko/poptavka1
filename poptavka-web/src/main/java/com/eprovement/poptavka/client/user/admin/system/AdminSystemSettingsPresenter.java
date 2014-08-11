/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.system;

import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminSystemSettings;
import com.eprovement.poptavka.domain.enums.LogType;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.eprovement.poptavka.shared.domain.adminModule.LogDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import java.util.List;

/**
 * SystemSettings widget is part of Settings module widgets.
 * Displays system settings.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AdminSystemSettingsView.class)
public class AdminSystemSettingsPresenter extends LazyPresenter<IAdminSystemSettings.View, AdminEventBus>
    implements IAdminSystemSettings.Presenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Constants. **/
    private static final int REFRESH_INTERVAL = 120000;
    /** Class Attribtutes. **/
    private ClickHandler changeHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            PropertyItemView propertyView = (PropertyItemView) event.getSource();
            eventBus.requestUpdateSystemProperties(propertyView.getPropertiesDetail());
        }
    };
    private Timer demandCountsRefresher = new Timer() {

        @Override
        public void run() {
            eventBus.requestJobProgress(LogType.DEMAND_COUNTS);
        }
    };
    private Timer supplierCountsRefresher = new Timer() {

        @Override
        public void run() {
            eventBus.requestJobProgress(LogType.SUPPLIER_COUNTS);
        }
    };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void onInitAdminSystemSettings() {
        eventBus.requestSystemProperties();
        demandCountsRefresher.scheduleRepeating(REFRESH_INTERVAL);
        supplierCountsRefresher.scheduleRepeating(REFRESH_INTERVAL);
        eventBus.displayView(view);
    }

    /**
     * Bind handlers
     */
    @Override
    public void bindView() {
        view.getDemandCountsBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getDemandCountsBtn().setEnabled(false);
                eventBus.requestCalculateDemandCounts();
                eventBus.responseJobProgress(LogType.DEMAND_COUNTS, new LogDetail());
            }
        });
        view.getSupplierCountsBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                view.getSupplierCountsBtn().setEnabled(false);
                eventBus.requestCalculateSupplierCounts();
                eventBus.responseJobProgress(LogType.SUPPLIER_COUNTS, new LogDetail());
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    @Override
    public void onResponseSystemProperties(List<PropertiesDetail> properties) {
        view.getPropertiesPanel().clear();
        for (PropertiesDetail property : properties) {
            PropertyItemView propertyView = new PropertyItemView(property);
            propertyView.addClickHandler(changeHandler);
            view.getPropertiesPanel().add(propertyView);
        }
    }

    @Override
    public void onResponseJobProgress(LogType job, LogDetail result) {
        switch (job) {
            case DEMAND_COUNTS:
                view.setDemandCountsProgress(result);
                break;
            case SUPPLIER_COUNTS:
                view.setSupplierCountsProgress(result);
                break;
            default:
                break;
        }
    }
}
