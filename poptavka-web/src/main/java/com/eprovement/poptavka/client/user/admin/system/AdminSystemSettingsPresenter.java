/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.system;

import com.eprovement.poptavka.client.user.admin.AdminEventBus;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminSystemSettings;
import com.eprovement.poptavka.shared.domain.PropertiesDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
    private ClickHandler changeHandler = new ClickHandler() {

        @Override
        public void onClick(ClickEvent event) {
            PropertyItemView propertyView = (PropertyItemView) event.getSource();
            eventBus.requestUpdateSystemProperties(propertyView.getPropertiesDetail());
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
        eventBus.displayView(view);
    }

    /**
     * Bind handlers
     */
    @Override
    public void bindView() {
        view.getCalcDemandCountsBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestCalculateDemandCounts();
            }
        });
        view.getCalcSupplierCountsBtn().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestCalculateSupplierCounts();
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onResponseSystemProperties(List<PropertiesDetail> properties) {
        view.getPropertiesPanel().clear();
        for (PropertiesDetail property : properties) {
            PropertyItemView propertyView = new PropertyItemView(property);
            propertyView.addClickHandler(changeHandler);
            view.getPropertiesPanel().add(propertyView);
        }
    }

    public void onResponseCalculateDemandCounts(Boolean result) {
        //cancel loading
    }

    public void onResponseCalculateSupplierCounts(Boolean result) {
        ////cancel loading
    }
}
